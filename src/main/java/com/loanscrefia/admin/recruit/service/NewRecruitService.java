package com.loanscrefia.admin.recruit.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.recruit.domain.NewRecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.admin.recruit.repository.NewRecruitRepository;
import com.loanscrefia.admin.recruit.repository.RecruitRepository;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.repository.UsersRepository;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.ProductDtlDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.NewUserRepository;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.repository.BatchRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;
import com.loanscrefia.util.UtilMask;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class NewRecruitService {

	@Autowired private NewRecruitRepository recruitRepository;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private NewUserRepository userRepo;
	@Autowired private EmailRepository emailRepository;
	@Autowired private KfbApiRepository kfbApiRepository;
	@Autowired private KfbApiService kfbApiService;
	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private UsersRepository usersRepository;

	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//은행연합회 API 적용여부
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;
	
	//이메일 적용여부
	@Value("${email.apply}")
	public boolean emailApply;
	
	//모집인 조회 및 변경 > 리스트
	@Transactional(readOnly=true)
	public List<NewRecruitDomain> selectNewRecruitList(NewRecruitDomain recruitDomain){
		
		UtilMask mask = new UtilMask();
		
		MemberDomain memberDomain = new MemberDomain();
		MemberDomain result = commonService.getMemberDetail(memberDomain);
		recruitDomain.setCreGrp(result.getCreGrp());
		
		// 주민번호 및 법인번호 암호화 후 비교
		if(cryptoApply) {
			recruitDomain.setPlMerchantNo(CryptoUtil.encrypt(recruitDomain.getPlMerchantNo()));
			recruitDomain.setPlMZId(CryptoUtil.encrypt(recruitDomain.getPlMZId()));
		}else {
			recruitDomain.setPlMerchantNo(recruitDomain.getPlMerchantNo());
			recruitDomain.setPlMZId(recruitDomain.getPlMZId());
		}
		
		List<NewRecruitDomain> recruitResultList = recruitRepository.selectNewRecruitList(recruitDomain);
		String plMZId = "";
		for(NewRecruitDomain list : recruitResultList) {
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				if(cryptoApply) {
					plMZId 	= CryptoUtil.decrypt(list.getPlMZId());
				}else {
					plMZId 	= list.getPlMZId();
					recruitDomain.setPlMZId(plMZId);
				}
				if(StringUtils.isNotEmpty(plMZId)) {
					// 2021-09-30 엑셀다운로드시 주민번호 마스킹 해제
					if(!"false".equals(recruitDomain.getIsPaging())) {
						plMZId = mask.maskSSN(plMZId);
					}
				}
				plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
				list.setPlMZId(plMZId);
			}
			
			StringBuilder merchantNo = new StringBuilder();
			if(StringUtils.isNotEmpty(list.getPlMerchantNo())) {
				if(cryptoApply) {
					merchantNo.append(CryptoUtil.decrypt(list.getPlMerchantNo()));
				}else {
					merchantNo.append(list.getPlMerchantNo());
				}
				merchantNo.insert(6, "-");
				list.setPlMerchantNo(merchantNo.toString());
			}
		}
		return recruitResultList;
	}
	
	//모집인 조회 및 변경 > 상세 : 개인
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitIndvDetail(NewRecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewRecruitDomain recruitInfo = recruitRepository.getNewRecruitDetail(recruitDomain);
		UsersDomain usersDomain = new UsersDomain();
		
		// user_seq로 결격요건 테이블 조회
		usersDomain.setUserSeq(recruitInfo.getUserSeq());
		usersDomain.setPlClass(recruitInfo.getPlClass());
		List<UsersDomain> disList = usersRepository.selectUsersDisList(usersDomain);
		int disCnt = 0;
		if(disList.size() > 0) {
			for(UsersDomain dis : disList) {
				if(StringUtils.isNotEmpty(dis.getDisVal())) {
					if("Y".equals(dis.getDisVal())) {
						disCnt++;
					}
				}
			}
			if(disCnt > 0) {
				recruitInfo.setDisVal("Y");
			}else {
				recruitInfo.setDisVal("N");
			}
		}
		
		
		//이력조회 시 변경된 데이터 찾기(현재 데이터와 다른 데이터중 최신)
		recruitDomain.setSearchPlMName(recruitInfo.getPlMName());
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone("");
		
		//이력상세 - 이름
		NewRecruitDomain recruitHistInfoName = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		if(recruitHistInfoName != null) {
			if(!recruitHistInfoName.getPlMName().equals(recruitInfo.getPlMName())) {
				recruitInfo.setHistPlMName(recruitHistInfoName.getPlMName());
				recruitInfo.setHistNameSeq(recruitHistInfoName.getMasterHistSeq());
			}
		}
		
		//이력조회 시 변경된 데이터 찾기(현재 데이터와 다른 데이터중 최신)
		recruitDomain.setSearchPlMName("");
		recruitDomain.setSearchPlMZId(recruitInfo.getPlMZId());
		recruitDomain.setSearchPlCellphone("");
		//이력상세 - 주민번호
		NewRecruitDomain recruitHistInfoZid = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		if(recruitHistInfoZid != null) {
			if(!recruitHistInfoZid.getPlMZId().equals(recruitInfo.getPlMZId())) {
				
				// 주민번호 암호화 해제		
				StringBuilder histZid = new StringBuilder();
				if(StringUtils.isNotEmpty(recruitHistInfoZid.getPlMZId())) {
					if(cryptoApply) {
						histZid.append(CryptoUtil.decrypt(recruitHistInfoZid.getPlMZId()));
					}else {
						histZid.append(recruitHistInfoZid.getPlMZId());
					}
					histZid.insert(6, "-");
					recruitInfo.setHistPlMZId(histZid.toString());
					recruitInfo.setHistZidSeq(recruitHistInfoZid.getMasterHistSeq());
				}
			}
		}
		
		//이력조회 시 변경된 데이터 찾기(현재 데이터와 다른 데이터중 최신)
		recruitDomain.setSearchPlMName("");
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone(recruitInfo.getPlCellphone().replaceAll("-", ""));
		//이력상세 - 연락처
		NewRecruitDomain recruitHistInfoPhone = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		if(recruitHistInfoPhone != null) {
			if(!recruitHistInfoPhone.getPlCellphone().equals(recruitInfo.getPlCellphone())) {
				recruitInfo.setHistPlCellphone(recruitHistInfoPhone.getPlCellphone());
				recruitInfo.setHistPhoneSeq(recruitHistInfoPhone.getMasterHistSeq());
			}
		}
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(recruitInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(recruitInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			}else {
				zid.append(recruitInfo.getPlMZId());
			}
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//첨부파일
    	if(recruitInfo.getFileSeq() != null) {
    		FileDomain fileParam = new FileDomain();
    		
    		fileParam.setFileGrpSeq(recruitInfo.getFileSeq());
        	List<FileDomain> fileList = commonService.selectFileList(fileParam);
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				recruitInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				recruitInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				recruitInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				recruitInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				recruitInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				recruitInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("7")) {
        				recruitInfo.setFileType7(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("8")) {
        				recruitInfo.setFileType8(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("9")) {
        				recruitInfo.setFileType9(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("10")) { //변경요청 시 증빙서류
        				recruitInfo.setFileType10(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("11")) { //변경요청 시 증빙서류
        				recruitInfo.setFileType11(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("12")) {
        				recruitInfo.setFileType12(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("13")) {
        				recruitInfo.setFileType13(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("14")) {
        				recruitInfo.setFileType14(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				recruitInfo.setFileType15(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("16")) {
        				recruitInfo.setFileType16(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("17")) {
        				recruitInfo.setFileType17(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("18")) {
        				recruitInfo.setFileType18(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("19")) {
        				recruitInfo.setFileType19(fileList.get(i));
        			}
        		}
        	}
        	
        	// 첨부파일 이력
        	fileParam.setUseYn("N");
        	fileParam.setSeqDesc("Y");
        	List<FileDomain> fileHistList = commonService.selectFileList(fileParam);
        	
        	if(fileHistList.size() > 0) {
        		for(int p=0; p < fileHistList.size(); p++) {
        			if(fileHistList.get(p).getFileType().equals("1")) {
        				recruitInfo.setHistFileType1(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("2")) {
        				recruitInfo.setHistFileType2(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("3")) {
        				recruitInfo.setHistFileType3(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("4")) {
        				recruitInfo.setHistFileType4(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("5")) {
        				recruitInfo.setHistFileType5(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("6")) {
        				recruitInfo.setHistFileType6(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("7")) {
        				recruitInfo.setHistFileType7(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("8")) {
        				recruitInfo.setHistFileType8(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("9")) {
        				recruitInfo.setHistFileType9(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("10")) { //변경요청 시 증빙서류
        				recruitInfo.setHistFileType10(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("11")) { //변경요청 시 증빙서류
        				recruitInfo.setHistFileType11(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("12")) {
        				recruitInfo.setHistFileType12(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("13")) {
        				recruitInfo.setHistFileType13(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("14")) {
        				recruitInfo.setHistFileType14(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("15")) {
        				recruitInfo.setHistFileType15(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("16")) {
        				recruitInfo.setHistFileType16(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("19")) {
        				recruitInfo.setHistFileType19(fileHistList.get(p));
        			}
        		}
        	}
    	}
    	
    	NewUserDomain userDomain = new NewUserDomain();
    	userDomain.setUserSeq(recruitInfo.getUserSeq());
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	userDomain.setVioNum("notEmpty");
    	//위반이력
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
    	
    	//금융상품세부내용 리스트 조회
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(userDomain);
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("recruitInfo", recruitInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
    	result.put("plProductDetailList", plProductDetailList);
    	
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(등록정보 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpDetail(NewRecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewRecruitDomain recruitInfo 	= recruitRepository.getNewRecruitDetail(recruitDomain);
		
		UsersDomain usersDomain = new UsersDomain();
		
		// user_seq로 결격요건 테이블 조회
		usersDomain.setUserSeq(recruitInfo.getUserSeq());
		usersDomain.setPlClass(recruitInfo.getPlClass());
		List<UsersDomain> disList = usersRepository.selectUsersDisList(usersDomain);
		int disCnt = 0;
		if(disList.size() > 0) {
			for(UsersDomain dis : disList) {
				if(StringUtils.isNotEmpty(dis.getDisVal())) {
					if("Y".equals(dis.getDisVal())) {
						disCnt++;
					}
				}
			}
			if(disCnt > 0) {
				recruitInfo.setDisVal("Y");
			}else {
				recruitInfo.setDisVal("N");
			}
		}
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(recruitInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(recruitInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			}else {
				zid.append(recruitInfo.getPlMZId());
			}
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//첨부파일
		if(recruitInfo.getFileSeq() != null) {
			FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(recruitInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				recruitInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				recruitInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				recruitInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				recruitInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				recruitInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				recruitInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				recruitInfo.setFileType15(fileList.get(i));
					}else if(fileList.get(i).getFileType().equals("31")) {
        				recruitInfo.setFileType31(fileList.get(i));
					}else if(fileList.get(i).getFileType().equals("32")) {
        				recruitInfo.setFileType32(fileList.get(i));
					}
        		}
        	}
        	
        	// 첨부파일 이력
        	fileParam.setUseYn("N");
        	fileParam.setSeqDesc("Y");
        	List<FileDomain> fileHistList = commonService.selectFileList(fileParam);
        	
        	if(fileHistList.size() > 0) {
        		for(int p=0; p < fileHistList.size(); p++) {
        			if(fileHistList.get(p).getFileType().equals("1")) {
        				recruitInfo.setHistFileType1(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("2")) {
        				recruitInfo.setHistFileType2(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("3")) {
        				recruitInfo.setHistFileType3(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("4")) {
        				recruitInfo.setHistFileType4(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("5")) {
        				recruitInfo.setHistFileType5(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("6")) {
        				recruitInfo.setHistFileType6(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("15")) {
        				recruitInfo.setHistFileType15(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("31")) {
        				recruitInfo.setHistFileType31(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("32")) {
        				recruitInfo.setHistFileType32(fileHistList.get(p));
        			}
        		}
        	}
        	
		}
		
    	NewUserDomain userDomain = new NewUserDomain();
    	userDomain.setUserSeq(recruitInfo.getUserSeq());
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	userDomain.setVioNum("notEmpty");
    	//위반이력
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
		
    	//금융상품세부내용 리스트 조회
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(userDomain);
    	
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("recruitInfo", recruitInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(대표자 및 임원관련사항 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpImwonDetail(RecruitImwonDomain recruitImwonDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();

		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상근여부 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("FTM001");
		List<CodeDtlDomain> fullTmStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//전문인력여부 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("EXP001");
		List<CodeDtlDomain> expertStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewRecruitDomain dtlParam			= new NewRecruitDomain();
		dtlParam.setMasterSeq(recruitImwonDomain.getMasterSeq());
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(recruitInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(recruitInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			}else {
				zid.append(recruitInfo.getPlMZId());
			}
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//대표자 및 임원 리스트
		List<RecruitImwonDomain> imwonList = recruitRepository.selectNewRecruitCorpImwonList(recruitImwonDomain);
		
		//첨부파일
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
				
				// 주민번호 암호화 해제		
				StringBuilder imwonZid = new StringBuilder();
				if(StringUtils.isNotEmpty(imwonList.get(i).getPlMZId())) {
					if(cryptoApply) {
						imwonZid.append(CryptoUtil.decrypt(imwonList.get(i).getPlMZId()));
					}else {
						imwonZid.append(imwonList.get(i).getPlMZId());
					}
					imwonZid.insert(6, "-");
					imwonList.get(i).setPlMZId(imwonZid.toString());
				}
				
				if(imwonList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(imwonList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("7")) {
							imwonList.get(i).setFileType7(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("8")) {
							imwonList.get(i).setFileType8(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("9")) {
							imwonList.get(i).setFileType9(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("10")) {
							imwonList.get(i).setFileType10(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("11")) {
							imwonList.get(i).setFileType11(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("12")) {
							imwonList.get(i).setFileType12(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("13")) {
							imwonList.get(i).setFileType13(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("14")) {
							imwonList.get(i).setFileType14(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("27")) {
							imwonList.get(i).setFileType27(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("28")) {
							imwonList.get(i).setFileType28(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("30")) {
							imwonList.get(i).setFileType30(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("33")) {
							imwonList.get(i).setFileType33(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("34")) {
							imwonList.get(i).setFileType34(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("46")) {
							imwonList.get(i).setFileType46(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("47")) {
							imwonList.get(i).setFileType47(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("recruitInfo", recruitInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(전문인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpExpertDetail(RecruitExpertDomain recruitExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewRecruitDomain dtlParam			= new NewRecruitDomain();
		dtlParam.setMasterSeq(recruitExpertDomain.getMasterSeq());
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(recruitInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(recruitInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			}else {
				zid.append(recruitInfo.getPlMZId());
			}
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//전문인력 리스트
		List<RecruitExpertDomain> expertList = recruitRepository.selectNewRecruitCorpExpertList(recruitExpertDomain);
		
		//첨부파일
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
				
				// 주민번호 암호화 해제		
				StringBuilder expertZid = new StringBuilder();
				if(StringUtils.isNotEmpty(expertList.get(i).getPlMZId())) {
					if(cryptoApply) {
						expertZid.append(CryptoUtil.decrypt(expertList.get(i).getPlMZId()));
					}else {
						expertZid.append(expertList.get(i).getPlMZId());
					}
					expertZid.insert(6, "-");
					expertList.get(i).setPlMZId(expertZid.toString());
				}
				
				if(expertList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(expertList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("16")) {
							expertList.get(i).setFileType16(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("17")) {
							expertList.get(i).setFileType17(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("18")) {
							expertList.get(i).setFileType18(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("31")) {
							expertList.get(i).setFileType31(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("35")) {
							expertList.get(i).setFileType35(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("36")) {
							expertList.get(i).setFileType36(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("39")) {
							expertList.get(i).setFileType39(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("40")) {
							expertList.get(i).setFileType40(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("recruitInfo", recruitInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(전산인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpItDetail(RecruitItDomain recruitItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		NewRecruitDomain dtlParam			= new NewRecruitDomain();
		dtlParam.setMasterSeq(recruitItDomain.getMasterSeq());
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(recruitInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(recruitInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			}else {
				zid.append(recruitInfo.getPlMZId());
			}
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//전산인력 리스트
		List<RecruitItDomain> itList 	= recruitRepository.selectNewRecruitCorpItList(recruitItDomain);
		
		//첨부파일
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
				
				// 주민번호 암호화 해제		
				StringBuilder itZid = new StringBuilder();
				if(StringUtils.isNotEmpty(itList.get(i).getPlMZId())) {
					if(cryptoApply) {
						itZid.append(CryptoUtil.decrypt(itList.get(i).getPlMZId()));
					}else {
						itZid.append(itList.get(i).getPlMZId());
					}
					itZid.insert(6, "-");
					itList.get(i).setPlMZId(itZid.toString());
				}
				
				if(itList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(itList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("19")) {
							itList.get(i).setFileType19(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("20")) {
							itList.get(i).setFileType20(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("37")) {
							itList.get(i).setFileType37(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("38")) {
							itList.get(i).setFileType38(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("48")) {
							itList.get(i).setFileType48(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("49")) {
							itList.get(i).setFileType49(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("recruitInfo", recruitInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(기타 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpEtcDetail(NewRecruitDomain recruitDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(recruitDomain);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(recruitInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(recruitInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			}else {
				zid.append(recruitInfo.getPlMZId());
			}
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//첨부파일
    	if(recruitInfo.getFileSeq() != null) {
    		FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(recruitInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("21")) {
        				recruitInfo.setFileType21(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("22")) {
        				recruitInfo.setFileType22(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("23")) {
        				recruitInfo.setFileType23(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("24")) {
        				recruitInfo.setFileType24(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("25")) {
        				recruitInfo.setFileType25(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("26")) {
        				recruitInfo.setFileType26(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("29")) {
        				recruitInfo.setFileType29(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("41")) {
        				recruitInfo.setFileType41(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("42")) {
        				recruitInfo.setFileType42(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("43")) {
        				recruitInfo.setFileType43(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("44")) {
        				recruitInfo.setFileType44(fileList.get(i));
        			}
        		}
        	}
        	
        	// 첨부파일 이력
        	fileParam.setUseYn("N");
        	fileParam.setSeqDesc("Y");
        	List<FileDomain> fileHistList = commonService.selectFileList(fileParam);
        	
        	if(fileHistList.size() > 0) {
        		for(int p=0; p < fileHistList.size(); p++) {
        			if(fileHistList.get(p).getFileType().equals("21")) {
        				recruitInfo.setHistFileType21(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("22")) {
        				recruitInfo.setHistFileType22(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("23")) {
        				recruitInfo.setHistFileType23(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("24")) {
        				recruitInfo.setHistFileType24(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("25")) {
        				recruitInfo.setHistFileType25(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("26")) {
        				recruitInfo.setHistFileType26(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("29")) {
        				recruitInfo.setHistFileType29(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("41")) {
        				recruitInfo.setHistFileType41(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("42")) {
        				recruitInfo.setHistFileType42(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("43")) {
        				recruitInfo.setHistFileType43(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("44")) {
        				recruitInfo.setHistFileType44(fileHistList.get(p));
        			}
        		}
        	}
        	
    	}
    	
		//전달
		result.put("recruitInfo", recruitInfo);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상태변경처리
	@Transactional
	public ResponseMsg updateNewRecruitPlStat(NewRecruitDomain recruitDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", null,  "오류가 발생하였습니다.");
		NewRecruitDomain statCheck = recruitRepository.getNewRecruitDetail(recruitDomain);
		
		//위반이력
    	NewUserDomain userDomain = new NewUserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	userDomain.setVioNum("empty");
    	
    	// 등록해야할 위반이력
    	List<NewUserDomain> violationRegList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	// 삭제해야할 위반이력
    	userDomain.setVioNum("notEmpty");
    	List<NewUserDomain> violationDelList = userRepo.selectNewUserViolationInfoList(userDomain);    	
    	
		
		// 현재 승인상태와 화면에 있는 승인상태 비교
		if(!recruitDomain.getOldPlStat().equals(statCheck.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		
		// API성공여부
		if("3".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			if("01".equals(statCheck.getPlProduct()) || "05".equals(statCheck.getPlProduct())) {
				if(kfbApiApply) {
					
					// 배치에서 상태변경
					recruitDomain.setPlStat("3");
					
					// 개인
					if("1".equals(statCheck.getPlClass())) {
						BatchDomain batchDomain = new BatchDomain();
						JSONObject jsonParam = new JSONObject();
						JSONObject jsonArrayParam = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						
						jsonParam.put("user_seq", statCheck.getUserSeq());
						jsonParam.put("master_seq", statCheck.getMasterSeq());
						jsonParam.put("lc_num", statCheck.getPlRegistNo());
						jsonParam.put("name", statCheck.getPlMName());
						
						// 배열
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("con_mobile", statCheck.getPlCellphone().replaceAll("-", ""));
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						batchDomain.setScheduleName("caseLoanUpd");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01("1"); //개인,법인 구분값
						batchRepository.insertBatchPlanInfo(batchDomain);
						
						
						
					}else { // 법인
						
						BatchDomain batchDomain = new BatchDomain();
						JSONObject jsonParam = new JSONObject();
						JSONObject jsonArrayParam = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						
						jsonParam.put("user_seq", statCheck.getUserSeq());
						jsonParam.put("master_seq", statCheck.getMasterSeq());
						jsonParam.put("corp_lc_num", statCheck.getPlRegistNo());
						jsonParam.put("corp_name", statCheck.getPlMerchantName());
						jsonParam.put("corp_rep_name", statCheck.getPlCeoName());
						jsonParam.put("corp_rep_ssn", statCheck.getPlMZId());
						jsonParam.put("corp_rep_ci", statCheck.getCi());
						
						// 배열
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						batchDomain.setScheduleName("caseLoanUpd");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01("2"); //개인,법인 구분값
						batchRepository.insertBatchPlanInfo(batchDomain);
					}
					
					
					if(violationRegList.size() > 0) {
						// 위반이력 등록
						for(NewUserDomain regVio : violationRegList) {
							JSONObject jsonRegVioParam = new JSONObject();
							jsonRegVioParam.put("lc_num", statCheck.getPlRegistNo());					// 등록번호
							jsonRegVioParam.put("vio_seq", regVio.getViolationSeq());					// 위반이력시퀀스
							jsonRegVioParam.put("ssn", statCheck.getPlMZId());		// 주민등록번호
							jsonRegVioParam.put("vio_fin_code", statCheck.getComCode());				// 금융기관코드
							jsonRegVioParam.put("vio_code", regVio.getViolationCd());					// 위반사유코드
							jsonRegVioParam.put("vio_date", regVio.getRegTimestamp());					// 위반일
						
							BatchDomain vioBatchDomain = new BatchDomain();
							vioBatchDomain.setScheduleName("violationReg");
							vioBatchDomain.setParam(jsonRegVioParam.toString());
							batchRepository.insertBatchPlanInfo(vioBatchDomain);

						}
					}
					
					if(violationDelList.size() > 0) {
						// 위반이력 삭제
						for(NewUserDomain delVio : violationDelList) {
							JSONObject jsonDelVioParam = new JSONObject();
							jsonDelVioParam.put("vio_num", delVio.getVioNum());								// 위반이력번호
							jsonDelVioParam.put("vio_seq", delVio.getViolationSeq());								// 위반이력시퀀스
							
							BatchDomain vioBatchDomain = new BatchDomain();
							vioBatchDomain.setScheduleName("violationDel");
							vioBatchDomain.setParam(jsonDelVioParam.toString());
							batchRepository.insertBatchPlanInfo(vioBatchDomain);
						}
					}
				}
			}
			
		}else if("6".equals(recruitDomain.getPlStat())) {
			// 변경요청에 대한 보완요청
			
		}else{
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		//모집인 이력 저장
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(recruitDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		
		//모집인 상태 변경
		int result = recruitRepository.updateNewRecruitPlStat(recruitDomain);
		/*
		//이메일 전송
		if(emailApply) {
			emailResult = emailRepository.sendEmail(emailDomain);
		}else {
			emailResult = 1;
		}
		*/
		
		if(result > 0) {
			// 모집인단계이력
			recruitRepository.insertNewMasterStep(recruitDomain);
			return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	//변경사항
	@Transactional(readOnly=true)
	public NewRecruitDomain getNewRecruitHistDetail(NewRecruitDomain recruitDomain) {
		
		NewRecruitDomain histDetail = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(histDetail.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(histDetail.getPlMerchantNo()));
			}else {
				merchantNo.append(histDetail.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			histDetail.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(histDetail.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(histDetail.getPlMZId()));
			}else {
				zid.append(histDetail.getPlMZId());
			}
			zid.insert(6, "-");
			histDetail.setPlMZId(zid.toString());
		}
		
		return histDetail; 
	}
	
	
	
	//첨부파일 이력 조회
	@Transactional(readOnly=true)
	public Map<String,Object> getNewFileHistDetail(NewRecruitDomain recruitDomain){
		Map<String, Object> result = new HashMap<String, Object>();
		FileDomain fileParam = new FileDomain();
		fileParam.setUseYn("N");
		fileParam.setFileGrpSeq(recruitDomain.getFileGrpSeq());
		fileParam.setHistType(recruitDomain.getHistType());
    	List<FileDomain> fileList = commonService.selectFileList(fileParam);
    	//전달
    	result.put("fileList", fileList);
		return result;
	}
	
}
