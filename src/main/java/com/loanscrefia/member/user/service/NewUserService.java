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
	
	//???????????? ??????
	@Value("${upload.filePath}")
	public String uPath;
	
	//????????? ????????????
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//??????????????? API ????????????
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;
	
	//????????? ????????????
	@Value("${email.apply}")
	public boolean emailApply;
	
	//SMS ????????????
	@Value("${sms.apply}")
	public boolean smsApply;
	
	@Autowired
	private BatchRepository batchRepository;

	// 2021-11-26 ????????? - ????????? ??????
	@Transactional
	public int switchPrevContractToFo(NewUserDomain newUserDomain) {
		return userRepo.switchPrevContractToFo(newUserDomain);
	}
	
	// 2021-10-12 ????????? - ????????? ???????????? ?????????(?????????)
	@Transactional(readOnly=true)
	public List<NewUserDomain> selectNewUserRegList(NewUserDomain newUserDomain){

		UtilMask mask = new UtilMask();
		
		//?????? ??????
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		newUserDomain.setCreGrp(loginInfo.getCreGrp());
		
		//????????? ?????????
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
		
		//?????????
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
	
	
	
	
	// 2021-10-12 ????????? - ????????? ???????????? ?????? ????????? : ??????
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegIndvDetail(NewUserDomain newUserDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//???????????? ?????? ?????????
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
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
			
			// 2021-10-27 ????????????????????? ???????????? ??? ?????? ??????
			userRegInfo.setBirthDt(plMZId.substring(0, 6));
			String genderCheck = plMZId.substring(7, 8);
			if("1".equals(genderCheck) || "3".equals(genderCheck) || "5".equals(genderCheck) || "7".equals(genderCheck)) {
				userRegInfo.setGender("??????");
			}else {
				userRegInfo.setGender("??????");
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
		
		//????????????
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
    	
    	
    	
    	
    	//????????????
    	newUserDomain.setUserSeq(userRegInfo.getUserSeq());
    	List<NewUserDomain> violationInfoList 	= userRepo.selectNewUserViolationInfoList(newUserDomain);
    	
    	//????????????
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(newUserDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
    	
    	//???????????????????????? ????????? ??????
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(newUserDomain);
    	
    	//??????
    	result.put("addrCodeList", addrCodeList);
    	result.put("violationCodeList", violationCodeList);
    	result.put("userRegInfo", userRegInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
    	result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	
	

	// 2021-10-12 ????????? - ????????? ???????????? ?????? ????????? : ?????? > ???????????? ???
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpDetail(NewUserDomain newUserDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//???????????? ?????? ?????????
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
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
			
			// 2021-10-27 ????????????????????? ???????????? ??? ?????? ??????
			userRegInfo.setBirthDt(plMZId.substring(0, 6));
			String genderCheck = plMZId.substring(7, 8);
			if("1".equals(genderCheck) || "3".equals(genderCheck) || "5".equals(genderCheck) || "7".equals(genderCheck)) {
				userRegInfo.setGender("??????");
			}else {
				userRegInfo.setGender("??????");
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
		
		//????????????
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
		
		//????????????
		newUserDomain.setUserSeq(userRegInfo.getUserSeq());
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(newUserDomain);
    	
    	//????????????
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(newUserDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
    	
    	//???????????????????????? ????????? ??????
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(newUserDomain);
		
		//??????
		result.put("addrCodeList", addrCodeList);
		result.put("violationCodeList", violationCodeList);
		result.put("userRegInfo", userRegInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	

	// 2021-10-12 ????????? - ????????? ???????????? ?????? ????????? : ?????? > ????????? ??? ?????????????????? ???
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpImwonDetail(UserImwonDomain userImwonDomain){
		
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
		
		//????????? ??? ?????? ?????????
		List<UserImwonDomain> imwonList = userRepo.selectNewUserRegCorpImwonList(userImwonDomain);
		
		//????????????
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
		
		//??????
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("userRegInfo", userRegInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//????????? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpExpertDetail(UserExpertDomain userExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????(??????,??????) ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
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
		
		//???????????? ?????????
		List<UserExpertDomain> expertList = userRepo.selectNewUserRegCorpExpertList(userExpertDomain);
		
		//????????????
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
		
		//??????
		result.put("careerTypList", careerTypList);
		result.put("userRegInfo", userRegInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//????????? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpItDetail(UserItDomain userItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
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
		
		//???????????? ?????????
		List<UserItDomain> itList 	= userRepo.selectNewUserRegCorpItList(userItDomain);
		
		//????????????
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
		
		//??????
		result.put("userRegInfo", userRegInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//????????? ?????? > ?????? : ??????(?????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewUserRegCorpEtcDetail(NewUserDomain newUserDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
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
		
		//????????????
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
    	
		//??????
		result.put("userRegInfo", userRegInfo);
		
		return result;
	}
	
	
	
	// 2021-10-12 ????????? - ????????????
	@Transactional
	public ResponseMsg newUserApply(NewUserDomain newUserDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", null,  "????????? ?????????????????????.");
		NewUserDomain userRegInfo 	= userRepo.getNewUserRegDetail(newUserDomain);
		
		if(!"2".equals(userRegInfo.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}
		
		// ????????? ?????? ??????
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(newUserDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		// ????????? ????????????
		int result = userRepo.newUserApply(newUserDomain);
		if(result > 0) {
			// ????????? ?????? ????????? ?????? ??????
			userRepo.insertNewMasterStep(newUserDomain);
			int smsResult = 0;
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("0220110770");
			smsDomain.setTranPhone(userRegInfo.getPlCellphone());
			smsDomain.setTranStatus("1");
			smsDomain.setTranEtc1("10070");
			String smsMsg = "";
			
			// ???????????? ??????
			if("15".equals(newUserDomain.getPlStat())) { // ??????

			}else if("16".equals(newUserDomain.getPlStat())) {
				
				// ???????????? ????????? ????????? ?????? API ?????? ??????
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
					smsMsg += userRegInfo.getPlMName()+"?????? "+userRegInfo.getComCodeNm()+" ??????????????? ????????? ?????? ????????? ?????????????????????";
				}else {
					smsMsg += userRegInfo.getPlMerchantName()+"??? "+userRegInfo.getComCodeNm()+" ??????????????? ????????? ?????? ????????? ?????????????????????";
				}
				smsDomain.setTranMsg(smsMsg);
				
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
			}
			
			// SMS ??????
			if(smsApply) {
				smsResult = smsRepository.sendSms(smsDomain);
			}else {
				smsResult = 1;
			}
			
			if(smsResult <= 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ??????????????????\n??????????????? ?????????????????????. ???????????? ????????? ?????????.");
			}
			
			return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "?????????????????????.");
			
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	// 2021-10-12 ????????? - ????????? ?????? ??? ?????? ?????????
	@Transactional(readOnly=true)
	public List<NewUserDomain> selectNewConfirmList(NewUserDomain newUserDomain){

		UtilMask mask = new UtilMask();
		
		//?????? ??????
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		newUserDomain.setCreGrp(loginInfo.getCreGrp());
		
		//????????? ?????????
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
		
		//?????????
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
	
	
	
	// 2021-10-12 ????????? - ????????? ?????? ??? ?????? - ????????????
	@Transactional
	public ResponseMsg newUserDropApply(NewUserDomain newUserDomain){
		
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		
		//??????
		NewUserDomain userRegInfo = userRepo.getNewUserRegDetail(newUserDomain);
		
		// ????????? ???????????? ????????? ??????
		List<NewUserDomain> preRegResult = userRepo.selectPreRegList(userRegInfo);
		if(preRegResult.size() > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ????????? ????????? ??????/???????????? ?????? ???????????? ??????????????? ????????? ?????????.");
		}
		
		/*
		if(userRegInfo.getPlClass().equals("2")) { //????????? ????????? ????????? ?????????(???????????????,?????? ??? ??????)??? ????????? ???????????? ??????
			UserImwonDomain chkParam1 	= new UserImwonDomain();
			UserExpertDomain chkParam2 	= new UserExpertDomain();
			UserItDomain chkParam3 		= new UserItDomain();
			
			//??????
			chkParam1.setMasterSeq(newUserDomain.getMasterSeq());
			List<UserImwonDomain> imwonList = userRepo.selectNewUserRegCorpImwonList(chkParam1);
			
			//????????????
			chkParam2.setMasterSeq(newUserDomain.getMasterSeq());
			List<UserExpertDomain> expertList = userRepo.selectNewUserRegCorpExpertList(chkParam2);
			
			//????????????
			chkParam3.setMasterSeq(newUserDomain.getMasterSeq());
			List<UserItDomain> itList = userRepo.selectNewUserRegCorpItList(chkParam3);
			
			if(imwonList.size() > 0 || expertList.size() > 0 || itList.size() > 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "?????? ???????????? ???????????? ??????????????? ????????? ?????????.");
			}
		}
		*/
		
		//?????? ?????? ??????*****
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(newUserDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		int result = userRepo.newUserDropApply(newUserDomain);
		
		
		// ?????? ?????? ??? 
		NewUserDomain resultInfo = new NewUserDomain();
		resultInfo = userRepo.getNewUserRegDetail(newUserDomain);
		
		
		//?????? ????????? ??????
		BatchDomain batchDomain = new BatchDomain();
		JSONObject jsonParam = new JSONObject();
		JSONObject jsonArrayParam = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		if("1".equals(userRegInfo.getPlClass())) {
			jsonParam.put("master_seq", resultInfo.getMasterSeq());
			jsonParam.put("lc_num", resultInfo.getPlRegistNo());
			
			// ??????
			jsonArrayParam.put("con_num", resultInfo.getConNum());
			jsonArrayParam.put("cancel_date", resultInfo.getComHaejiDate().replaceAll("-", ""));
			jsonArrayParam.put("cancel_code", resultInfo.getPlHistCd());
			
			jsonArray.put(jsonArrayParam);
			jsonParam.put("con_arr", jsonArray);
			
			batchDomain.setScheduleName("dropApply");
			batchDomain.setParam(jsonParam.toString());
			batchDomain.setProperty01("1"); //??????,?????? ?????????
			batchDomain.setProperty02(Integer.toString(resultInfo.getUserSeq())); 				// ????????? ????????? ???????????? ????????? user_seq
			batchDomain.setProperty03(Integer.toString(resultInfo.getMasterSeq())); 			// ????????? ????????? ???????????? ????????? master_seq
			batchRepository.insertBatchPlanInfo(batchDomain);
			
		}else {
			
			jsonParam.put("master_seq", resultInfo.getMasterSeq());				
			jsonParam.put("corp_lc_num", resultInfo.getPlRegistNo());
			// ??????
			jsonArrayParam.put("con_num", resultInfo.getConNum());
			jsonArrayParam.put("cancel_date", resultInfo.getComHaejiDate().replaceAll("-", ""));
			jsonArrayParam.put("cancel_code", resultInfo.getPlHistCd());
			
			jsonArray.put(jsonArrayParam);
			jsonParam.put("con_arr", jsonArray);
			
			batchDomain.setScheduleName("dropApply");
			batchDomain.setParam(jsonParam.toString());
			batchDomain.setProperty01("2");														//??????,?????? ?????????
			batchDomain.setProperty02(Integer.toString(resultInfo.getUserSeq())); 				// ????????? ????????? ???????????? ????????? user_seq
			batchDomain.setProperty03(Integer.toString(resultInfo.getMasterSeq())); 			// ????????? ????????? ???????????? ????????? master_seq
			batchRepository.insertBatchPlanInfo(batchDomain);
		}

		
		if(result > 0) {
			// ????????? ?????? ????????? ?????? ??????
			userRepo.insertNewMasterStep(newUserDomain);
			int smsResult = 0;
			
			if(smsApply) {
				// ???????????? ??????
				SmsDomain smsDomain = new SmsDomain();
				smsDomain.setTranCallback("0220110770");
				smsDomain.setTranPhone(resultInfo.getPlCellphone());
				smsDomain.setTranStatus("1");
				String smsMsg = "";
				smsMsg += resultInfo.getComCodeNm()+" ????????? "+resultInfo.getPlMName()+"?????? ??????????????? ????????? ?????? ??????????????? ?????????????????????.";
				smsDomain.setTranMsg(smsMsg);
				smsDomain.setTranEtc1("10070");
				smsResult = smsRepository.sendSms(smsDomain);
			}else {
				smsResult = 1;
			}
			
			if(smsResult > 0) {
				return new ResponseMsg(HttpStatus.OK, "success", "??????????????? ?????????????????????.");
			}else {
				return new ResponseMsg(HttpStatus.OK, "success", "??????????????? ?????????????????? SMS????????? ?????????????????????.");
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
	}
	
	
	// 2021-10-25 ????????? - ????????? ?????? ??? ?????? - ??????????????????
	@Transactional
	public ResponseMsg newUserDropApplyCancel(NewUserDomain newUserDomain){
		
		//?????? ?????? ??????*****
		NewUserDomain param = new NewUserDomain();
		param.setMasterSeq(newUserDomain.getMasterSeq());
		userRepo.insertNewUserHistory(param);
		// ????????? ?????? ?????? - ??????(????????????????????? ?????? ????????? ??????)
		int result = userRepo.newUserDropApplyCancel(newUserDomain);
		if(result > 0) {
			// ????????? ?????? ????????? ?????? ??????
			userRepo.insertNewMasterStep(newUserDomain);
			
			BatchDomain batchDomain = new BatchDomain();
			batchDomain.setScheduleName("dropApply");
			batchDomain.setProperty03(Integer.toString(newUserDomain.getMasterSeq()));			
			batchRepository.deleteBatchPlanInfo(batchDomain);
			
			return new ResponseMsg(HttpStatus.OK, "success", "??????????????? ?????????????????????.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
	}
	
	//???????????? ????????????
	@Transactional
	public ResponseMsg newApplyDeleteViolationInfo(NewUserDomain newUserDomain){
		int updateResult = userRepo.newApplyDeleteViolationInfo(newUserDomain);
		if(updateResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "???????????????????????????.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
	}
	
	
	//???????????? ?????? 
	@Transactional
	public ResponseMsg newDeleteNewUserViolationInfo(NewUserDomain newUserDomain){
		
		//???????????? ?????? ?????? insert
		JSONObject jsonDelVioParam = new JSONObject();
		jsonDelVioParam.put("vio_num", newUserDomain.getVioNum());										// ??????????????????
		jsonDelVioParam.put("vio_seq",  String.valueOf(newUserDomain.getViolationSeq()));
		
		BatchDomain vioBatchDomain = new BatchDomain();
		vioBatchDomain.setScheduleName("violationDel");
		vioBatchDomain.setParam(jsonDelVioParam.toString());
		batchRepository.insertBatchPlanInfo(vioBatchDomain);
		
		// ???????????? ??? ??????
		int deleteResult = userRepo.newDeleteNewUserViolationInfo(newUserDomain);
		if(deleteResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "??????????????? ?????????????????????.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
	}
	
	//???????????? ?????? ??? ?????? insert
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
			
			//????????????
	    	NewUserDomain userDomain = new NewUserDomain();
	    	userDomain.setMasterSeq(resultInfo.getMasterSeq());
	    	userDomain.setUserSeq(newUserDomain.getUserSeq());
	    	userDomain.setVioNum("empty");
	    	
	    	// ??????????????? ????????????
	    	List<NewUserDomain> violationRegList = userRepo.selectNewUserViolationInfoList(userDomain);
			if(violationRegList.size() > 0) {
				// ???????????? ??????
				for(NewUserDomain regVio : violationRegList) {
					JSONObject jsonRegVioParam = new JSONObject();
					jsonRegVioParam.put("lc_num", resultInfo.getPlRegistNo());							// ????????????
					jsonRegVioParam.put("vio_seq", String.valueOf(regVio.getViolationSeq()));
					
					if("1".equals(resultInfo.getPlClass())) {
						jsonRegVioParam.put("ssn", resultInfo.getPlMZId());									// ??????????????????
					}else {
						jsonRegVioParam.put("ssn", resultInfo.getPlMerchantNo());							// ????????? ????????????
					}
					
					jsonRegVioParam.put("vio_fin_code", String.valueOf(resultInfo.getComCode()));		// ??????????????????
					jsonRegVioParam.put("vio_code", regVio.getViolationCd());							// ??????????????????
					jsonRegVioParam.put("vio_date", regVio.getRegTimestamp());							// ?????????
				
					BatchDomain vioBatchDomain = new BatchDomain();
					vioBatchDomain.setScheduleName("violationReg");
					vioBatchDomain.setParam(jsonRegVioParam.toString());
					batchRepository.insertBatchPlanInfo(vioBatchDomain);

				}
			}
			return new ResponseMsg(HttpStatus.OK, "success", "??????????????? ?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
		}
		
	}
	
	
	
	
	

	// 2021-12-02 ?????? ????????? ?????? ?????????
	@Transactional(readOnly=true)
	public List<NewUserDomain> selectTotList(NewUserDomain newUserDomain){

		UtilMask mask = new UtilMask();
		
		//?????? ??????
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		newUserDomain.setCreGrp(loginInfo.getCreGrp());
		
		//????????? ?????????
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
		
		//?????????
		List<NewUserDomain> userRegList = userRepo.selectTotList(newUserDomain);
		
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
	
	
	
	
	
	
}
