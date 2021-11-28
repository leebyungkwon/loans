package com.loanscrefia.member.user.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.corp.service.CorpService;
import com.loanscrefia.admin.edu.service.EduService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.common.common.sms.domain.SmsDomain;
import com.loanscrefia.common.common.sms.repository.SmsRepository;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.ProductDtlDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
import com.loanscrefia.member.user.repository.NewUserRepository;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.repository.BatchRepository;
import com.loanscrefia.system.batch.service.BatchService;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;
import com.loanscrefia.util.UtilMask;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class NewUserService {

	@Autowired private NewUserRepository userRepo;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private SmsRepository smsRepository;
	@Autowired private BatchService batchService;
	
	//첨부파일 경로
	@Value("${upload.filePath}")
	public String uPath;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//은행연합회 API 적용여부
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;
	
	//이메일 적용여부
	@Value("${email.apply}")
	public boolean emailApply;
	
	//SMS 적용여부
	@Value("${sms.apply}")
	public boolean smsApply;
	
	@Autowired
	private BatchRepository batchRepository;

	// 2021-11-26 고도화 - 데이터 이관
	@Transactional
	public int switchPrevContractToFo(NewUserDomain newUserDomain) {
		return userRepo.switchPrevContractToFo(newUserDomain);
	}
	
	// 2021-10-12 고도화 - 모집인 확인처리 리스트(회원사)
	@Transactional(readOnly=true)
	public List<NewUserDomain> selectNewUserRegList(NewUserDomain newUserDomain){

		UtilMask mask = new UtilMask();
		
		//세션 정보
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		newUserDomain.setCreGrp(loginInfo.getCreGrp());
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(newUserDomain.getPlMZId())) {
			if(cryptoApply) {
				newUserDomain.setPlMZId(CryptoUtil.encrypt(newUserDomain.getPlMZId().replaceAll("-", "")));
			}else {
				newUserDomain.setPlMZId(newUserDomain.getPlMZId().replaceAll("-", ""));
			}
		}
		
		if(StringUtils.isNotEmpty(newUserDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				newUserDomain.setPlMerchantNo(CryptoUtil.encrypt(newUserDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				newUserDomain.setPlMerchantNo(newUserDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		
		//리스트
		List<NewUserDomain> userRegList = userRepo.selectNewUserRegList(newUserDomain);
		
		if(userRegList.size() > 0) {
			String plMZId 		= "";
			String plMerchantNo = "";
			for(int i = 0;i < userRegList.size();i++) {
				if(StringUtils.isNotEmpty(userRegList.get(i).getPlMZId())) {
					if(cryptoApply) {
						plMZId 	= CryptoUtil.decrypt(userRegList.get(i).getPlMZId());
					}else {
						plMZId 	= userRegList.get(i).getPlMZId();
						newUserDomain.setPlMZId(plMZId);
					}
					if(StringUtils.isNotEmpty(plMZId)) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
					userRegList.get(i).setPlMZId(plMZId);
				}
				if(StringUtils.isNotEmpty(userRegList.get(i).getPlMerchantNo())) {
					if(cryptoApply) {
						plMerchantNo 	= CryptoUtil.decrypt(userRegList.get(i).getPlMerchantNo());
					}else {
						plMerchantNo 	= userRegList.get(i).getPlMerchantNo();
					}
					plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
					userRegList.get(i).setPlMerchantNo(plMerchantNo);
				}
			}
		}
		return userRegList;
	}
	
	
	
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 개인
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegIndvDetail(NewUserDomain newUserDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//위반이력 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(newUserDomain);
		
		UtilMask mask = new UtilMask();
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			
			if(StringUtils.isNotEmpty(plMZId)) {
				plMZId = mask.maskSSN(plMZId);
			}
			
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
			// 2021-10-27 주민등록번호로 생년월일 및 성별 추출
			userRegInfo.setBirthDt(plMZId.substring(0, 6));
			String genderCheck = plMZId.substring(7, 8);
			if("1".equals(genderCheck) || "3".equals(genderCheck) || "5".equals(genderCheck) || "7".equals(genderCheck)) {
				userRegInfo.setGender("남성");
			}else {
				userRegInfo.setGender("여성");
			}
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//첨부파일
    	if(userRegInfo.getFileSeq() != null) {
    		FileDomain fileParam = new FileDomain();
    		
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList = commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				userRegInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				userRegInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				userRegInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				userRegInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				userRegInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				userRegInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("8")) {
        				userRegInfo.setFileType8(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("9")) {
        				userRegInfo.setFileType9(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("11")) {
        				userRegInfo.setFileType11(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("12")) {
        				userRegInfo.setFileType12(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("14")) {
        				userRegInfo.setFileType14(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				userRegInfo.setFileType15(fileList.get(i));
        			}
        		}
        	}
    	}
    	
    	
    	
    	
    	//위반이력
    	newUserDomain.setUserSeq(userRegInfo.getUserSeq());
    	List<NewUserDomain> violationInfoList 	= userRepo.selectNewUserViolationInfoList(newUserDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(newUserDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
    	
    	//금융상품세부내용 리스트 조회
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(newUserDomain);
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("violationCodeList", violationCodeList);
    	result.put("userRegInfo", userRegInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
    	result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	
	

	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 등록정보 탭
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpDetail(NewUserDomain newUserDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//위반이력 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(newUserDomain);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
			// 2021-10-27 주민등록번호로 생년월일 및 성별 추출
			userRegInfo.setBirthDt(plMZId.substring(0, 6));
			String genderCheck = plMZId.substring(7, 8);
			if("1".equals(genderCheck) || "3".equals(genderCheck) || "5".equals(genderCheck) || "7".equals(genderCheck)) {
				userRegInfo.setGender("남성");
			}else {
				userRegInfo.setGender("여성");
			}
			
		}
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//첨부파일
		if(userRegInfo.getFileSeq() != null) {
			FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				userRegInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				userRegInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				userRegInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				userRegInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				userRegInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				userRegInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				userRegInfo.setFileType15(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("31")) {
        				userRegInfo.setFileType31(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("32")) {
        				userRegInfo.setFileType32(fileList.get(i));
        			}
        		}
        	}
		}
		
		//위반이력
		newUserDomain.setUserSeq(userRegInfo.getUserSeq());
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(newUserDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(newUserDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
    	
    	//금융상품세부내용 리스트 조회
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(newUserDomain);
		
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("violationCodeList", violationCodeList);
		result.put("userRegInfo", userRegInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	

	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpImwonDetail(UserImwonDomain userImwonDomain){
		
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
		NewUserDomain dtlParam			= new NewUserDomain();
		dtlParam.setMasterSeq(userImwonDomain.getMasterSeq());
		NewUserDomain userRegInfo 		= userRepo.getNewUserRegDetail(dtlParam);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//대표자 및 임원 리스트
		List<UserImwonDomain> imwonList = userRepo.selectNewUserRegCorpImwonList(userImwonDomain);
		
		//첨부파일
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
				String imwonPlMZId 	= "";
				if(cryptoApply) {
					imwonPlMZId 	= CryptoUtil.decrypt(imwonList.get(i).getPlMZId());
				}else {
					imwonPlMZId 	= imwonList.get(i).getPlMZId();
				}
				imwonPlMZId 		= imwonPlMZId.substring(0, 6) + "-" + imwonPlMZId.substring(6);
				imwonList.get(i).setPlMZId(imwonPlMZId);
				
				if(imwonList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(imwonList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("7")) {
							imwonList.get(i).setFileType7(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("8")) {
							imwonList.get(i).setFileType8(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("10")) {
							imwonList.get(i).setFileType10(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("11")) {
							imwonList.get(i).setFileType11(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("12")) {
							imwonList.get(i).setFileType12(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("13")) {
							imwonList.get(i).setFileType13(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("27")) {
							imwonList.get(i).setFileType27(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("28")) {
							imwonList.get(i).setFileType28(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("33")) {
							imwonList.get(i).setFileType33(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("34")) {
							imwonList.get(i).setFileType34(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("userRegInfo", userRegInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(전문인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpExpertDetail(UserExpertDomain userExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		NewUserDomain dtlParam		= new NewUserDomain();
		dtlParam.setMasterSeq(userExpertDomain.getMasterSeq());
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(dtlParam);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//전문인력 리스트
		List<UserExpertDomain> expertList = userRepo.selectNewUserRegCorpExpertList(userExpertDomain);
		
		//첨부파일
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
				String expertPlMZId = "";
				if(cryptoApply) {
					expertPlMZId 	= CryptoUtil.decrypt(expertList.get(i).getPlMZId());
				}else {
					expertPlMZId 	= expertList.get(i).getPlMZId();
				}
				expertPlMZId 		= expertPlMZId.substring(0, 6) + "-" + expertPlMZId.substring(6);
				expertList.get(i).setPlMZId(expertPlMZId);
				
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
		result.put("userRegInfo", userRegInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(전산인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpItDetail(UserItDomain userItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		NewUserDomain dtlParam		= new NewUserDomain();
		dtlParam.setMasterSeq(userItDomain.getMasterSeq());
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(dtlParam);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//전산인력 리스트
		List<UserItDomain> itList 	= userRepo.selectNewUserRegCorpItList(userItDomain);
		
		//첨부파일
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
				if(StringUtils.isNotEmpty(itList.get(i).getPlMZId())) {
					String itPlMZId 	= "";
					if(cryptoApply) {
						itPlMZId 		= CryptoUtil.decrypt(itList.get(i).getPlMZId());
					}else {
						itPlMZId 		= itList.get(i).getPlMZId();
					}
					itPlMZId 			= itPlMZId.substring(0, 6) + "-" + itPlMZId.substring(6);
					itList.get(i).setPlMZId(itPlMZId);
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
						}
					}
				}
			}
		}
		
		//전달
		result.put("userRegInfo", userRegInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(기타 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpEtcDetail(NewUserDomain newUserDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(newUserDomain);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//첨부파일
    	if(userRegInfo.getFileSeq() != null) {
    		FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("21")) {
        				userRegInfo.setFileType21(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("22")) {
        				userRegInfo.setFileType22(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("23")) {
        				userRegInfo.setFileType23(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("25")) {
        				userRegInfo.setFileType25(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("26")) {
        				userRegInfo.setFileType26(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("29")) {
        				userRegInfo.setFileType29(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("41")) {
        				userRegInfo.setFileType41(fileList.get(i));
        			}
        		}
        	}
    	}
    	
		//전달
		result.put("userRegInfo", userRegInfo);
		
		return result;
	}
	
	
	
	// 2021-10-12 고도화 - 상태변경
	@Transactional
	public ResponseMsg newUserApply(NewUserDomain newUserDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", null,  "오류가 발생하였습니다.");
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(newUserDomain);
		
		if(!"2".equals(userRegInfo.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		// 모집인 이력 저장
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(newUserDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		// 모집인 상태변경
		int result = userRepo.newUserApply(newUserDomain);
		if(result > 0) {
			// 모집인 상태 단계별 이력 저장
			userRepo.insertNewMasterStep(newUserDomain);
			int smsResult = 0;
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("0220110770");
			smsDomain.setTranPhone(userRegInfo.getPlCellphone());
			smsDomain.setTranStatus("1");
			smsDomain.setTranEtc1("10070");
			String smsMsg = "";
			
			// 문자발송 추가
			if("15".equals(newUserDomain.getPlStat())) { // 승인

			}else if("16".equals(newUserDomain.getPlStat())) {
				
				// 금융회사 거절시 가등록 삭제 API 배치 등록
				BatchDomain batchDomain = new BatchDomain();
				JSONObject jsonParam 	= new JSONObject();
				jsonParam.put("master_seq", userRegInfo.getMasterSeq());
				jsonParam.put("pre_lc_num", userRegInfo.getPreLcNum());
				
				batchDomain.setScheduleName("preLoanDel");
				batchDomain.setParam(jsonParam.toString());
				
				if("1".equals(userRegInfo.getPlClass())) {
					batchDomain.setProperty01("1");
				}else {
					batchDomain.setProperty01("2");
				}
				
				batchService.insertBatchPlanInfo(batchDomain);
				
				if("1".equals(userRegInfo.getPlClass())) {
					smsMsg += userRegInfo.getPlMName()+"님은 "+userRegInfo.getComCodeNm()+" 대출성상품 모집인 등록 신청이 거절되었습니다";
				}else {
					smsMsg += userRegInfo.getPlMerchantName()+"은 "+userRegInfo.getComCodeNm()+" 대출성상품 모집인 등록 신청이 거절되었습니다";
				}
				smsDomain.setTranMsg(smsMsg);
				
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
			}
			
			// SMS 발송
			if(smsApply) {
				smsResult = smsRepository.sendSms(smsDomain);
			}else {
				smsResult = 1;
			}
			
			if(smsResult <= 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "상태변경은 완료되었으나\n문자발송에 실패하였습니다. 관리자에 문의해 주세요.");
			}
			
			return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "완료되었습니다.");
			
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 리스트
	@Transactional(readOnly=true)
	public List<NewUserDomain> selectNewConfirmList(NewUserDomain newUserDomain){

		UtilMask mask = new UtilMask();
		
		//세션 정보
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		newUserDomain.setCreGrp(loginInfo.getCreGrp());
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(newUserDomain.getPlMZId())) {
			if(cryptoApply) {
				newUserDomain.setPlMZId(CryptoUtil.encrypt(newUserDomain.getPlMZId().replaceAll("-", "")));
			}else {
				newUserDomain.setPlMZId(newUserDomain.getPlMZId().replaceAll("-", ""));
			}
		}
		
		if(StringUtils.isNotEmpty(newUserDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				newUserDomain.setPlMerchantNo(CryptoUtil.encrypt(newUserDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				newUserDomain.setPlMerchantNo(newUserDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		
		//리스트
		List<NewUserDomain> userRegList = userRepo.selectNewConfirmList(newUserDomain);
		
		if(userRegList.size() > 0) {
			String plMZId 		= "";
			String plMerchantNo = "";
			for(int i = 0;i < userRegList.size();i++) {
				if(StringUtils.isNotEmpty(userRegList.get(i).getPlMZId())) {
					if(cryptoApply) {
						plMZId 	= CryptoUtil.decrypt(userRegList.get(i).getPlMZId());
					}else {
						plMZId 	= userRegList.get(i).getPlMZId();
						newUserDomain.setPlMZId(plMZId);
					}
					if(StringUtils.isNotEmpty(plMZId)) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
					userRegList.get(i).setPlMZId(plMZId);
				}
				if(StringUtils.isNotEmpty(userRegList.get(i).getPlMerchantNo())) {
					if(cryptoApply) {
						plMerchantNo 	= CryptoUtil.decrypt(userRegList.get(i).getPlMerchantNo());
					}else {
						plMerchantNo 	= userRegList.get(i).getPlMerchantNo();
					}
					plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
					userRegList.get(i).setPlMerchantNo(plMerchantNo);
				}
			}
		}
		return userRegList;
	}
	
	
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 - 해지요청
	@Transactional
	public ResponseMsg newUserDropApply(NewUserDomain newUserDomain){
		
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		
		//상세
		NewUserDomain userRegInfo = userRepo.getNewUserRegDetail(newUserDomain);
		
		// 가등록 진행중인 계약건 확인
		List<NewUserDomain> preRegResult = userRepo.selectPreRegList(userRegInfo);
		if(preRegResult.size() > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "등록한 모집인 정보로 진행중인 계약건(가등록)이 존재하여 해지요청이 불가능 합니다.");
		}
		
		if(userRegInfo.getPlClass().equals("2")) { //법인은 하위에 등록된 데이터(법인사용인,임원 등 정보)가 있으면 해지요청 불가
			UserImwonDomain chkParam1 	= new UserImwonDomain();
			UserExpertDomain chkParam2 	= new UserExpertDomain();
			UserItDomain chkParam3 		= new UserItDomain();
			
			//임원
			chkParam1.setMasterSeq(newUserDomain.getMasterSeq());
			List<UserImwonDomain> imwonList = userRepo.selectNewUserRegCorpImwonList(chkParam1);
			
			//전문인력
			chkParam2.setMasterSeq(newUserDomain.getMasterSeq());
			List<UserExpertDomain> expertList = userRepo.selectNewUserRegCorpExpertList(chkParam2);
			
			//전산인력
			chkParam3.setMasterSeq(newUserDomain.getMasterSeq());
			List<UserItDomain> itList = userRepo.selectNewUserRegCorpItList(chkParam3);
			
			if(imwonList.size() > 0 || expertList.size() > 0 || itList.size() > 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "하위 데이터가 존재하여 해지요청이 불가능 합니다.");
			}
		}
		
		//기본 이력 저장*****
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(newUserDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		int result = userRepo.newUserDropApply(newUserDomain);
		
		
		// 정보 저장 후 
		NewUserDomain resultInfo = new NewUserDomain();
		resultInfo = userRepo.getNewUserRegDetail(newUserDomain);
		
		
		//배치 테이블 저장
		BatchDomain batchDomain = new BatchDomain();
		JSONObject jsonParam = new JSONObject();
		JSONObject jsonArrayParam = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		if("1".equals(userRegInfo.getPlClass())) {
			jsonParam.put("master_seq", resultInfo.getMasterSeq());
			jsonParam.put("lc_num", resultInfo.getPlRegistNo());
			
			// 배열
			jsonArrayParam.put("con_num", resultInfo.getConNum());
			jsonArrayParam.put("cancel_date", resultInfo.getComHaejiDate().replaceAll("-", ""));
			jsonArrayParam.put("cancel_code", resultInfo.getPlHistCd());
			
			jsonArray.put(jsonArrayParam);
			jsonParam.put("con_arr", jsonArray);
			
			batchDomain.setScheduleName("dropApply");
			batchDomain.setParam(jsonParam.toString());
			batchDomain.setProperty01("1"); //개인,법인 구분값
			batchDomain.setProperty02(Integer.toString(resultInfo.getUserSeq())); 				// 마지막 결과값 변경시에 사용될 user_seq
			batchDomain.setProperty03(Integer.toString(resultInfo.getMasterSeq())); 			// 마지막 결과값 변경시에 사용될 master_seq
			batchRepository.insertBatchPlanInfo(batchDomain);
			
		}else {
			
			jsonParam.put("master_seq", resultInfo.getMasterSeq());				
			jsonParam.put("corp_lc_num", resultInfo.getPlRegistNo());
			// 배열
			jsonArrayParam.put("con_num", resultInfo.getConNum());
			jsonArrayParam.put("cancel_date", resultInfo.getComHaejiDate().replaceAll("-", ""));
			jsonArrayParam.put("cancel_code", resultInfo.getPlHistCd());
			
			jsonArray.put(jsonArrayParam);
			jsonParam.put("con_arr", jsonArray);
			
			batchDomain.setScheduleName("dropApply");
			batchDomain.setParam(jsonParam.toString());
			batchDomain.setProperty01("2");														//개인,법인 구분값
			batchDomain.setProperty02(Integer.toString(resultInfo.getUserSeq())); 				// 마지막 결과값 변경시에 사용될 user_seq
			batchDomain.setProperty03(Integer.toString(resultInfo.getMasterSeq())); 			// 마지막 결과값 변경시에 사용될 master_seq
			batchRepository.insertBatchPlanInfo(batchDomain);
		}

		
		if(result > 0) {
			// 모집인 상태 단계별 이력 저장
			userRepo.insertNewMasterStep(newUserDomain);
			int smsResult = 0;
			
			if(smsApply) {
				// 문자발송 추가
				SmsDomain smsDomain = new SmsDomain();
				smsDomain.setTranCallback("0220110770");
				smsDomain.setTranPhone(resultInfo.getPlCellphone());
				smsDomain.setTranStatus("1");
				String smsMsg = "";
				smsMsg += resultInfo.getComCodeNm()+" 로부터 "+resultInfo.getPlMName()+"님의 대출성상품 모집인 등록 해지신청이 접수되었습니다.";
				smsDomain.setTranMsg(smsMsg);
				smsDomain.setTranEtc1("10070");
				smsResult = smsRepository.sendSms(smsDomain);
			}else {
				smsResult = 1;
			}
			
			if(smsResult > 0) {
				return new ResponseMsg(HttpStatus.OK, "success", "해지요청이 완료되었습니다.");
			}else {
				return new ResponseMsg(HttpStatus.OK, "success", "해지요청이 완료되었으나 SMS발송에 실패하였습니다.");
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	
	// 2021-10-25 고도화 - 모집인 조회 및 해지 - 해지요청취소
	@Transactional
	public ResponseMsg newUserDropApplyCancel(NewUserDomain newUserDomain){
		
		//기본 이력 저장*****
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(newUserDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		// 모집인 상태 변경 - 완료(해지요청취소에 따른 완료로 변환)
		int result = userRepo.newUserDropApplyCancel(newUserDomain);
		if(result > 0) {
			// 모집인 상태 단계별 이력 저장
			userRepo.insertNewMasterStep(newUserDomain);
			
			BatchDomain batchDomain = new BatchDomain();
			batchDomain.setScheduleName("dropApply");
			batchDomain.setProperty03(Integer.toString(newUserDomain.getMasterSeq()));			
			batchRepository.deleteBatchPlanInfo(batchDomain);
			
			return new ResponseMsg(HttpStatus.OK, "success", "해지요청이 취소되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//위반이력 삭제요청
	@Transactional
	public ResponseMsg newApplyDeleteViolationInfo(NewUserDomain newUserDomain){
		int updateResult = userRepo.newApplyDeleteViolationInfo(newUserDomain);
		if(updateResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "삭제요청되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	
	//위반이력 삭제 
	@Transactional
	public ResponseMsg newDeleteNewUserViolationInfo(NewUserDomain newUserDomain){
		
		//위반이력 삭제 배치 insert
		JSONObject jsonDelVioParam = new JSONObject();
		jsonDelVioParam.put("vio_num", newUserDomain.getVioNum());										// 위반이력번호
		jsonDelVioParam.put("vio_seq",  String.valueOf(newUserDomain.getViolationSeq()));
		
		BatchDomain vioBatchDomain = new BatchDomain();
		vioBatchDomain.setScheduleName("violationDel");
		vioBatchDomain.setParam(jsonDelVioParam.toString());
		batchRepository.insertBatchPlanInfo(vioBatchDomain);
		
		// 배치등록 후 삭제
		int deleteResult = userRepo.newDeleteNewUserViolationInfo(newUserDomain);
		if(deleteResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "위반이력이 삭제되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//위반이력 등록 및 배치 insert
	@Transactional
	public ResponseMsg newUpdateVio(NewUserDomain newUserDomain){
		
		String[] violationCdArr = newUserDomain.getViolationCdArr();
		int vioTotCnt = 0;
		int vioCnt = 0;
		
		if(violationCdArr != null && violationCdArr.length > 0) {
			vioTotCnt = violationCdArr.length;
			for(int i = 0;i < violationCdArr.length;i++) {
				if(violationCdArr[i] != null && !violationCdArr[i].equals("")) {
					newUserDomain.setViolationCd(violationCdArr[i]);
					userRepo.insertNewUserViolationInfo(newUserDomain);
					vioCnt++;
				}
			}
		}
		
		if(vioTotCnt == vioCnt) {
			
			NewUserDomain resultInfo = userRepo.getNewUserRegDetail(newUserDomain);
			
			//위반이력
	    	NewUserDomain userDomain = new NewUserDomain();
	    	userDomain.setUserSeq(newUserDomain.getUserSeq());
	    	
	    	// 등록해야할 위반이력
	    	List<NewUserDomain> violationRegList = userRepo.selectNewUserViolationInfoList(userDomain);
			if(violationRegList.size() > 0) {
				// 위반이력 등록
				for(NewUserDomain regVio : violationRegList) {
					JSONObject jsonRegVioParam = new JSONObject();
					jsonRegVioParam.put("lc_num", resultInfo.getPlRegistNo());							// 등록번호
					jsonRegVioParam.put("vio_seq", String.valueOf(regVio.getViolationSeq()));
					
					if("1".equals(resultInfo.getPlClass())) {
						jsonRegVioParam.put("ssn", resultInfo.getPlMZId());									// 주민등록번호
					}else {
						jsonRegVioParam.put("ssn", resultInfo.getPlMerchantNo());							// 법인은 법인번호
					}
					
					jsonRegVioParam.put("vio_fin_code", String.valueOf(resultInfo.getComCode()));		// 금융기관코드
					jsonRegVioParam.put("vio_code", regVio.getViolationCd());							// 위반사유코드
					jsonRegVioParam.put("vio_date", regVio.getRegTimestamp());							// 위반일
				
					BatchDomain vioBatchDomain = new BatchDomain();
					vioBatchDomain.setScheduleName("violationReg");
					vioBatchDomain.setParam(jsonRegVioParam.toString());
					batchRepository.insertBatchPlanInfo(vioBatchDomain);

				}
			}
			return new ResponseMsg(HttpStatus.OK, "success", "위반이력이 등록되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
		}
		
	}
	
	
}
