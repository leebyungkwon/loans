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

	//????????? ????????????
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//??????????????? API ????????????
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;
	
	//????????? ????????????
	@Value("${email.apply}")
	public boolean emailApply;
	
	//????????? ?????? ??? ?????? > ?????????
	@Transactional(readOnly=true)
	public List<NewRecruitDomain> selectNewRecruitList(NewRecruitDomain recruitDomain){
		
		UtilMask mask = new UtilMask();
		
		MemberDomain memberDomain = new MemberDomain();
		MemberDomain result = commonService.getMemberDetail(memberDomain);
		recruitDomain.setCreGrp(result.getCreGrp());
		
		// ???????????? ??? ???????????? ????????? ??? ??????
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
					// 2021-09-30 ????????????????????? ???????????? ????????? ??????
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
	
	//????????? ?????? ??? ?????? > ?????? : ??????
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitIndvDetail(NewRecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewRecruitDomain recruitInfo = recruitRepository.getNewRecruitDetail(recruitDomain);
		UsersDomain usersDomain = new UsersDomain();
		
		// user_seq??? ???????????? ????????? ??????
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
		}else {
			recruitInfo.setDisVal("");
		}
		
		
		//???????????? ??? ????????? ????????? ??????(?????? ???????????? ?????? ???????????? ??????)
		recruitDomain.setSearchPlMName(recruitInfo.getPlMName());
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone("");
		
		//???????????? - ??????
		NewRecruitDomain recruitHistInfoName = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		if(recruitHistInfoName != null) {
			if(!recruitHistInfoName.getPlMName().equals(recruitInfo.getPlMName())) {
				recruitInfo.setHistPlMName(recruitHistInfoName.getPlMName());
				recruitInfo.setHistNameSeq(recruitHistInfoName.getMasterHistSeq());
			}
		}
		
		//???????????? ??? ????????? ????????? ??????(?????? ???????????? ?????? ???????????? ??????)
		recruitDomain.setSearchPlMName("");
		recruitDomain.setSearchPlMZId(recruitInfo.getPlMZId());
		recruitDomain.setSearchPlCellphone("");
		//???????????? - ????????????
		NewRecruitDomain recruitHistInfoZid = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		if(recruitHistInfoZid != null) {
			if(!recruitHistInfoZid.getPlMZId().equals(recruitInfo.getPlMZId())) {
				
				// ???????????? ????????? ??????		
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
		
		//???????????? ??? ????????? ????????? ??????(?????? ???????????? ?????? ???????????? ??????)
		recruitDomain.setSearchPlMName("");
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone(recruitInfo.getPlCellphone().replaceAll("-", ""));
		//???????????? - ?????????
		NewRecruitDomain recruitHistInfoPhone = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		if(recruitHistInfoPhone != null) {
			if(!recruitHistInfoPhone.getPlCellphone().equals(recruitInfo.getPlCellphone())) {
				recruitInfo.setHistPlCellphone(recruitHistInfoPhone.getPlCellphone());
				recruitInfo.setHistPhoneSeq(recruitHistInfoPhone.getMasterHistSeq());
			}
		}
		
		// ORIGIN ???????????? ????????? ??????
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
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
		
		//????????????
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
        			}else if(fileList.get(i).getFileType().equals("10")) { //???????????? ??? ????????????
        				recruitInfo.setFileType10(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("11")) { //???????????? ??? ????????????
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
        	
        	// ???????????? ??????
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
        			}else if(fileHistList.get(p).getFileType().equals("10")) { //???????????? ??? ????????????
        				recruitInfo.setHistFileType10(fileHistList.get(p));
        			}else if(fileHistList.get(p).getFileType().equals("11")) { //???????????? ??? ????????????
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
    	//????????????
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	//????????????
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
    	
    	//???????????????????????? ????????? ??????
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(userDomain);
    	
    	//??????
    	result.put("addrCodeList", addrCodeList);
    	result.put("recruitInfo", recruitInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
    	result.put("plProductDetailList", plProductDetailList);
    	
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpDetail(NewRecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewRecruitDomain recruitInfo 	= recruitRepository.getNewRecruitDetail(recruitDomain);
		
		UsersDomain usersDomain = new UsersDomain();
		
		// user_seq??? ???????????? ????????? ??????
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
		}else {
			recruitInfo.setDisVal("");
		}
		
		// ORIGIN ???????????? ????????? ??????
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
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
		
		//????????????
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
        	
        	// ???????????? ??????
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
    	//????????????
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	//????????????
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
		
    	//???????????????????????? ????????? ??????
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(userDomain);
    	
		//??????
		result.put("addrCodeList", addrCodeList);
		result.put("recruitInfo", recruitInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(????????? ??? ?????????????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpImwonDetail(RecruitImwonDomain recruitImwonDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();

		//??????(??????,??????) ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//???????????? ?????? ?????????
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("FTM001");
		List<CodeDtlDomain> fullTmStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//?????????????????? ?????? ?????????
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("EXP001");
		List<CodeDtlDomain> expertStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewRecruitDomain dtlParam			= new NewRecruitDomain();
		dtlParam.setMasterSeq(recruitImwonDomain.getMasterSeq());
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(dtlParam);
		
		// ORIGIN ???????????? ????????? ??????
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
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
		
		//????????? ??? ?????? ?????????
		List<RecruitImwonDomain> imwonList = recruitRepository.selectNewRecruitCorpImwonList(recruitImwonDomain);
		
		//????????????
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
				
				// ???????????? ????????? ??????		
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
		
		//??????
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("recruitInfo", recruitInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpExpertDetail(RecruitExpertDomain recruitExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????(??????,??????) ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewRecruitDomain dtlParam			= new NewRecruitDomain();
		dtlParam.setMasterSeq(recruitExpertDomain.getMasterSeq());
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(dtlParam);
		
		// ORIGIN ???????????? ????????? ??????
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
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
		
		//???????????? ?????????
		List<RecruitExpertDomain> expertList = recruitRepository.selectNewRecruitCorpExpertList(recruitExpertDomain);
		
		//????????????
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
				
				// ???????????? ????????? ??????		
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
		
		//??????
		result.put("careerTypList", careerTypList);
		result.put("recruitInfo", recruitInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpItDetail(RecruitItDomain recruitItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
		NewRecruitDomain dtlParam			= new NewRecruitDomain();
		dtlParam.setMasterSeq(recruitItDomain.getMasterSeq());
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(dtlParam);
		
		// ORIGIN ???????????? ????????? ??????
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
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
		
		//???????????? ?????????
		List<RecruitItDomain> itList 	= recruitRepository.selectNewRecruitCorpItList(recruitItDomain);
		
		//????????????
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
				
				// ???????????? ????????? ??????		
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
		
		//??????
		result.put("recruitInfo", recruitInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(?????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewRecruitCorpEtcDetail(NewRecruitDomain recruitDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
		NewRecruitDomain recruitInfo 		= recruitRepository.getNewRecruitDetail(recruitDomain);
		
		// ORIGIN ???????????? ????????? ??????
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
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
		
		//????????????
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
        	
        	// ???????????? ??????
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
    	
		//??????
		result.put("recruitInfo", recruitInfo);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ??????????????????
	@Transactional
	public ResponseMsg updateNewRecruitPlStat(NewRecruitDomain recruitDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", null,  "????????? ?????????????????????.");
		NewRecruitDomain statCheck = recruitRepository.getNewRecruitDetail(recruitDomain);
		
		//????????????
    	NewUserDomain userDomain = new NewUserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	userDomain.setVioNum("empty");
    	
    	// ??????????????? ????????????
    	List<NewUserDomain> violationRegList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	// ??????????????? ????????????
    	userDomain.setVioNum("notEmpty");
    	List<NewUserDomain> violationDelList = userRepo.selectNewUserViolationInfoList(userDomain);    	
    	
		
		// ?????? ??????????????? ????????? ?????? ???????????? ??????
		if(!recruitDomain.getOldPlStat().equals(statCheck.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}
		
		
		// API????????????
		if("3".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			if("01".equals(statCheck.getPlProduct()) || "05".equals(statCheck.getPlProduct())) {
				if(kfbApiApply) {
					
					// ???????????? ????????????
					recruitDomain.setPlStat("3");
					
					// ??????
					if("1".equals(statCheck.getPlClass())) {
						BatchDomain batchDomain = new BatchDomain();
						JSONObject jsonParam = new JSONObject();
						JSONObject jsonArrayParam = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						
						jsonParam.put("user_seq", statCheck.getUserSeq());
						jsonParam.put("master_seq", statCheck.getMasterSeq());
						jsonParam.put("lc_num", statCheck.getPlRegistNo());
						jsonParam.put("name", statCheck.getPlMName());
						
						// ??????
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("con_mobile", statCheck.getPlCellphone().replaceAll("-", ""));
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						batchDomain.setScheduleName("caseLoanUpd");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01("1"); //??????,?????? ?????????
						batchRepository.insertBatchPlanInfo(batchDomain);
						
						
						
					}else { // ??????
						
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
						
						// ??????
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						batchDomain.setScheduleName("caseLoanUpd");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01("2"); //??????,?????? ?????????
						batchRepository.insertBatchPlanInfo(batchDomain);
					}
					
					
					if(violationRegList.size() > 0) {
						// ???????????? ??????
						for(NewUserDomain regVio : violationRegList) {
							JSONObject jsonRegVioParam = new JSONObject();
							jsonRegVioParam.put("lc_num", statCheck.getPlRegistNo());					// ????????????
							jsonRegVioParam.put("vio_seq", regVio.getViolationSeq());					// ?????????????????????
							jsonRegVioParam.put("ssn", statCheck.getPlMZId());		// ??????????????????
							jsonRegVioParam.put("vio_fin_code", statCheck.getComCode());				// ??????????????????
							jsonRegVioParam.put("vio_code", regVio.getViolationCd());					// ??????????????????
							jsonRegVioParam.put("vio_date", regVio.getRegTimestamp());					// ?????????
						
							BatchDomain vioBatchDomain = new BatchDomain();
							vioBatchDomain.setScheduleName("violationReg");
							vioBatchDomain.setParam(jsonRegVioParam.toString());
							batchRepository.insertBatchPlanInfo(vioBatchDomain);

						}
					}
					
					if(violationDelList.size() > 0) {
						// ???????????? ??????
						for(NewUserDomain delVio : violationDelList) {
							JSONObject jsonDelVioParam = new JSONObject();
							jsonDelVioParam.put("vio_num", delVio.getVioNum());								// ??????????????????
							jsonDelVioParam.put("vio_seq", delVio.getViolationSeq());								// ?????????????????????
							
							BatchDomain vioBatchDomain = new BatchDomain();
							vioBatchDomain.setScheduleName("violationDel");
							vioBatchDomain.setParam(jsonDelVioParam.toString());
							batchRepository.insertBatchPlanInfo(vioBatchDomain);
						}
					}
				}
			}
			
		}else if("6".equals(recruitDomain.getPlStat())) {
			// ??????????????? ?????? ????????????
			
		}else{
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}
		
		//????????? ?????? ??????
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(recruitDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		
		//????????? ?????? ??????
		int result = recruitRepository.updateNewRecruitPlStat(recruitDomain);
		/*
		//????????? ??????
		if(emailApply) {
			emailResult = emailRepository.sendEmail(emailDomain);
		}else {
			emailResult = 1;
		}
		*/
		
		if(result > 0) {
			// ?????????????????????
			recruitRepository.insertNewMasterStep(recruitDomain);
			return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	//????????????
	@Transactional(readOnly=true)
	public NewRecruitDomain getNewRecruitHistDetail(NewRecruitDomain recruitDomain) {
		
		NewRecruitDomain histDetail = recruitRepository.getNewRecruitHistDetail(recruitDomain);
		
		// ???????????? ????????? ??????		
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
		
		// ???????????? ????????? ??????		
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
	
	
	
	//???????????? ?????? ??????
	@Transactional(readOnly=true)
	public Map<String,Object> getNewFileHistDetail(NewRecruitDomain recruitDomain){
		Map<String, Object> result = new HashMap<String, Object>();
		FileDomain fileParam = new FileDomain();
		fileParam.setUseYn("N");
		fileParam.setFileGrpSeq(recruitDomain.getFileGrpSeq());
		fileParam.setHistType(recruitDomain.getHistType());
    	List<FileDomain> fileList = commonService.selectFileList(fileParam);
    	//??????
    	result.put("fileList", fileList);
		return result;
	}
	
}
