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
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
import com.loanscrefia.member.user.repository.NewUserRepository;
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
	@Autowired private CorpService corpService;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private EduService eduService;
	@Autowired private UtilFile utilFile;
	@Autowired private UtilExcel<T> utilExcel;
	@Autowired private KfbApiService kfbApiService; //은행연합회
	
	//첨부파일 경로
	@Value("${upload.filePath}")
	public String uPath;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//은행연합회 API 적용여부
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;

	
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
    	List<NewUserDomain> violationInfoList 	= userRepo.selectNewUserViolationInfoList(newUserDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(newUserDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("violationCodeList", violationCodeList);
    	result.put("userRegInfo", userRegInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
		
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
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(newUserDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(newUserDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
		
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("violationCodeList", violationCodeList);
		result.put("userRegInfo", userRegInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		
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
	
	
	
	// 2021-10-12 고도화 - 회원사 거절
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
		// 모집인 상태 변경 - 거절
		int result = userRepo.newUserApply(newUserDomain);
		if(result > 0) {
			// 모집인 상태 단계별 이력 저장
			userRepo.insertNewMasterStep(newUserDomain);
			
			
			// 문자발송 추가
			
			
			
			
			// 문자발송 끝
			
			
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
		
		//상세
		NewUserDomain userRegInfo = userRepo.getNewUserRegDetail(newUserDomain);
		
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
		// 모집인 상태 변경 - 거절
		int result = userRepo.newUserDropApply(newUserDomain);
		if(result > 0) {
			// 모집인 상태 단계별 이력 저장
			userRepo.insertNewMasterStep(newUserDomain);
			
			// 문자발송 추가
			
			
			
			
			// 문자발송 끝
			return new ResponseMsg(HttpStatus.OK, "success", "해지요청이 완료되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	
	
	
	
	
}
