package com.loanscrefia.admin.apply.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.repository.ApplyRepository;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;

import sinsiway.CryptoUtil;

@Service
public class ApplyService {

	@Autowired
	private ApplyRepository applyRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EmailRepository emailRepository;
	
	//은행연합회
	@Autowired 
	private KfbApiService kfbApiService;
	
	@Autowired
	private KfbApiRepository kfbApiRepository;

	//모집인 조회 및 변경 > 리스트
	@Transactional(readOnly=true)
	public List<ApplyDomain> selectApplyList(ApplyDomain applyDomain){
		MemberDomain memberDomain = new MemberDomain();
		MemberDomain result = commonService.getMemberDetail(memberDomain);
		applyDomain.setCreGrp(result.getCreGrp());
		
		// 주민번호 및 법인번호 암호화 후 비교
		applyDomain.setPlMerchantNo(CryptoUtil.encrypt(applyDomain.getPlMerchantNo()));
		applyDomain.setPlMZId(CryptoUtil.encrypt(applyDomain.getPlMZId()));
		
		List<ApplyDomain> applyResultList = applyRepository.selectApplyList(applyDomain);
		for(ApplyDomain list : applyResultList) {
			StringBuilder jumin = new StringBuilder();
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				jumin.append(CryptoUtil.decrypt(list.getPlMZId()));
				jumin.insert(6, "-");
				list.setPlMZId(jumin.toString());
			}
			
			StringBuilder merchantNo = new StringBuilder();
			if(StringUtils.isNotEmpty(list.getPlMerchantNo())) {
				merchantNo.append(CryptoUtil.decrypt(list.getPlMerchantNo()));
				merchantNo.insert(6, "-");
				list.setPlMerchantNo(merchantNo.toString());
			}
		}
		
		
		
		return applyResultList;
	}
	
	//모집인 조회 및 변경 > 상세 : 개인
	@Transactional(readOnly=true)
	public Map<String,Object> getApplyIndvDetail(ApplyDomain applyDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
		
		
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		
		
		//첨부파일
    	if(applyInfo.getFileSeq() != null) {
    		FileDomain fileParam = new FileDomain();
    		
    		fileParam.setFileGrpSeq(applyInfo.getFileSeq());
        	List<FileDomain> fileList = commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				applyInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				applyInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				applyInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				applyInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				applyInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				applyInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("7")) {
        				applyInfo.setFileType7(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("8")) {
        				applyInfo.setFileType8(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("9")) {
        				applyInfo.setFileType9(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("10")) { //변경요청 시 증빙서류
        				applyInfo.setFileType10(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("11")) { //변경요청 시 증빙서류
        				applyInfo.setFileType11(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("12")) {
        				applyInfo.setFileType12(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("13")) {
        				applyInfo.setFileType13(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("14")) {
        				applyInfo.setFileType14(fileList.get(i));
        			}
        			
        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
        			checkDomain.setFileSeq(fileList.get(i).getFileSeq());
        			List<ApplyCheckDomain> checkList = applyRepository.selectApplyCheckList(checkDomain);
        			for(ApplyCheckDomain tmp : checkList) {
        				if("1".equals(tmp.getCheckCd())){
        					applyInfo.setCheckCd1(tmp.getCheckCd());
        				}else if("2".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd2(tmp.getCheckCd());
        				}else if("3".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd3(tmp.getCheckCd());
        				}else if("4".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd4(tmp.getCheckCd());
        				}else if("5".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd5(tmp.getCheckCd());
        				}else if("6".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd6(tmp.getCheckCd());
        				}else if("7".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd7(tmp.getCheckCd());
        				}else if("8".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd8(tmp.getCheckCd());
        				}else if("9".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd9(tmp.getCheckCd());
        				}else if("10".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd10(tmp.getCheckCd());
        				}else if("11".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd11(tmp.getCheckCd());
        				}else if("12".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd12(tmp.getCheckCd());
        				}else if("13".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd13(tmp.getCheckCd());
        				}else if("14".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd14(tmp.getCheckCd());
        				}else if("15".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd15(tmp.getCheckCd());
        				}else if("16".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd16(tmp.getCheckCd());
        				}
        			}
        		}
        	}
    	}
    	
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMemberSeq(applyDomain.getMemberSeq());
    	//위반이력
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
    	
		
    	MemberDomain memberDomain = new MemberDomain();
    	memberDomain.setMemberSeq(Long.valueOf(applyInfo.getMemberSeq()));
    	MemberDomain memberResult = commonService.getCompanyMemberDetail(memberDomain);
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("applyInfo", applyInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("memberResult", memberResult);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(등록정보 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getApplyCorpDetail(ApplyDomain applyDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		ApplyDomain applyInfo 	= applyRepository.getApplyDetail(applyDomain);
		
		
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		
		
		//첨부파일
		if(applyInfo.getFileSeq() != null) {
			FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(applyInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				applyInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				applyInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				applyInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				applyInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				applyInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				applyInfo.setFileType6(fileList.get(i));
        			}
        			
        			
        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
        			checkDomain.setFileSeq(fileList.get(i).getFileSeq());
        			List<ApplyCheckDomain> checkList = applyRepository.selectApplyCheckList(checkDomain);
        			for(ApplyCheckDomain tmp : checkList) {
        				if("1".equals(tmp.getCheckCd())){
        					applyInfo.setCheckCd1(tmp.getCheckCd());
        				}else if("2".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd2(tmp.getCheckCd());
        				}else if("3".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd3(tmp.getCheckCd());
        				}else if("4".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd4(tmp.getCheckCd());
        				}else if("5".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd5(tmp.getCheckCd());
        				}else if("6".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd6(tmp.getCheckCd());
        				}else if("7".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd7(tmp.getCheckCd());
        				}else if("8".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd8(tmp.getCheckCd());
        				}else if("9".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd9(tmp.getCheckCd());
        				}else if("10".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd10(tmp.getCheckCd());
        				}else if("11".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd11(tmp.getCheckCd());
        				}else if("12".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd12(tmp.getCheckCd());
        				}else if("13".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd13(tmp.getCheckCd());
        				}
        			}
        			
        		}
        	}
		}
		
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMemberSeq(applyDomain.getMemberSeq());
    	//위반이력
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
		
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("applyInfo", applyInfo);
		result.put("violationInfoList", violationInfoList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(대표자 및 임원관련사항 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getApplyCorpImwonDetail(ApplyImwonDomain applyImwonDomain){
		
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
		ApplyDomain dtlParam			= new ApplyDomain();
		dtlParam.setMasterSeq(applyImwonDomain.getMasterSeq());
		ApplyDomain applyInfo 		= applyRepository.getApplyDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		
		//대표자 및 임원 리스트
		List<ApplyImwonDomain> imwonList = applyRepository.selectApplyCorpImwonList(applyImwonDomain);
		
		//첨부파일
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
				// 주민번호 암호화 해제		
				StringBuilder imwonZid = new StringBuilder();
				if(StringUtils.isNotEmpty(imwonList.get(i).getPlMZId())) {
					imwonZid.append(CryptoUtil.decrypt(imwonList.get(i).getPlMZId()));
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
						}else if(fileList.get(j).getFileType().equals("15")) {
							imwonList.get(i).setFileType15(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("27")) {
							imwonList.get(i).setFileType27(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("28")) {
							imwonList.get(i).setFileType28(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("30")) {
							imwonList.get(i).setFileType30(fileList.get(j));
						}
						
						
	        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
	        			checkDomain.setFileSeq(fileList.get(j).getFileSeq());
	        			List<ApplyCheckDomain> checkList = applyRepository.selectApplyCheckList(checkDomain);
	        			for(ApplyCheckDomain tmp : checkList) {
	        				if("100".equals(tmp.getCheckCd())){
	        					imwonList.get(i).setCheckCd100(tmp.getCheckCd());
	        				}else if("101".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd101(tmp.getCheckCd());
	        				}else if("102".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd102(tmp.getCheckCd());
	        				}else if("103".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd103(tmp.getCheckCd());
	        				}else if("104".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd104(tmp.getCheckCd());
	        				}else if("105".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd105(tmp.getCheckCd());
	        				}else if("106".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd106(tmp.getCheckCd());
	        				}else if("107".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd107(tmp.getCheckCd());
	        				}else if("108".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd108(tmp.getCheckCd());
	        				}else if("109".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd109(tmp.getCheckCd());
	        				}else if("110".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd110(tmp.getCheckCd());
	        				}else if("111".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd111(tmp.getCheckCd());
	        				}else if("112".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd112(tmp.getCheckCd());
	        				}else if("113".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd113(tmp.getCheckCd());
	        				}else if("114".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd114(tmp.getCheckCd());
	        				}else if("115".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd115(tmp.getCheckCd());
	        				}else if("116".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd116(tmp.getCheckCd());
	        				}
	        			}
						
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("applyInfo", applyInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(전문인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getApplyCorpExpertDetail(ApplyExpertDomain applyExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		ApplyDomain dtlParam			= new ApplyDomain();
		dtlParam.setMasterSeq(applyExpertDomain.getMasterSeq());
		ApplyDomain applyInfo 		= applyRepository.getApplyDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//전문인력 리스트
		List<ApplyExpertDomain> expertList = applyRepository.selecApplyCorpExpertList(applyExpertDomain);
		
		//첨부파일
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
				
				// 주민번호 암호화 해제		
				StringBuilder expertZid = new StringBuilder();
				if(StringUtils.isNotEmpty(expertList.get(i).getPlMZId())) {
					expertZid.append(CryptoUtil.decrypt(expertList.get(i).getPlMZId()));
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
						}
						
	        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
	        			checkDomain.setFileSeq(fileList.get(j).getFileSeq());
	        			List<ApplyCheckDomain> checkList = applyRepository.selectApplyCheckList(checkDomain);
	        			for(ApplyCheckDomain tmp : checkList) {
	        				if("200".equals(tmp.getCheckCd())){
	        					expertList.get(i).setCheckCd200(tmp.getCheckCd());
	        				}else if("201".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd201(tmp.getCheckCd());
	        				}else if("202".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd202(tmp.getCheckCd());
	        				}else if("203".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd203(tmp.getCheckCd());
	        				}else if("204".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd204(tmp.getCheckCd());
	        				}else if("205".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd205(tmp.getCheckCd());
	        				}else if("206".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd206(tmp.getCheckCd());
	        				}else if("207".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd207(tmp.getCheckCd());
	        				}
	        			}
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("applyInfo", applyInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(전산인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getApplyCorpItDetail(ApplyItDomain applyItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		ApplyDomain dtlParam			= new ApplyDomain();
		dtlParam.setMasterSeq(applyItDomain.getMasterSeq());
		ApplyDomain applyInfo 		= applyRepository.getApplyDetail(dtlParam);
		
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//전산인력 리스트
		List<ApplyItDomain> itList 	= applyRepository.selectApplyCorpItList(applyItDomain);
		
		//첨부파일
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
				
				// 주민번호 암호화 해제		
				StringBuilder itZid = new StringBuilder();
				if(StringUtils.isNotEmpty(itList.get(i).getPlMZId())) {
					itZid.append(CryptoUtil.decrypt(itList.get(i).getPlMZId()));
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
						}
						
	        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
	        			checkDomain.setFileSeq(fileList.get(j).getFileSeq());
	        			List<ApplyCheckDomain> checkList = applyRepository.selectApplyCheckList(checkDomain);
	        			for(ApplyCheckDomain tmp : checkList) {
	        				if("300".equals(tmp.getCheckCd())){
	        					itList.get(i).setCheckCd300(tmp.getCheckCd());
	        				}else if("301".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd301(tmp.getCheckCd());
	        				}else if("302".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd302(tmp.getCheckCd());
	        				}else if("303".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd303(tmp.getCheckCd());
	        				}else if("304".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd304(tmp.getCheckCd());
	        				}
	        			}
						
					}
				}
			}
		}
		
		//전달
		result.put("applyInfo", applyInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(기타 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getApplyCorpEtcDetail(ApplyDomain applyDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		ApplyDomain applyInfo 		= applyRepository.getApplyDetail(applyDomain);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//첨부파일
    	if(applyInfo.getFileSeq() != null) {
    		FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(applyInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("21")) {
        				applyInfo.setFileType21(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("22")) {
        				applyInfo.setFileType22(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("23")) {
        				applyInfo.setFileType23(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("24")) {
        				applyInfo.setFileType24(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("25")) {
        				applyInfo.setFileType25(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("26")) {
        				applyInfo.setFileType26(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("29")) {
        				applyInfo.setFileType29(fileList.get(i));
        			}
        			
        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
        			checkDomain.setFileSeq(fileList.get(i).getFileSeq());
        			List<ApplyCheckDomain> checkList = applyRepository.selectApplyCheckList(checkDomain);
        			for(ApplyCheckDomain tmp : checkList) {
        				if("400".equals(tmp.getCheckCd())){
        					applyInfo.setCheckCd400(tmp.getCheckCd());
        				}else if("401".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd401(tmp.getCheckCd());
        				}else if("402".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd402(tmp.getCheckCd());
        				}else if("403".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd403(tmp.getCheckCd());
        				}else if("404".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd404(tmp.getCheckCd());
        				}else if("405".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd405(tmp.getCheckCd());
        				}else if("406".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd406(tmp.getCheckCd());
        				}else if("407".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd407(tmp.getCheckCd());
        				}else if("408".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd408(tmp.getCheckCd());
        				}
        			}
        			
        		}
        	}
    	}
    	
		//전달
		result.put("applyInfo", applyInfo);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상태변경처리
	@Transactional
	public ResponseMsg updateApplyPlStat(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		ApplyDomain statCheck = applyRepository.getApplyDetail(applyDomain);
		
		// 현재 승인상태와 화면에 있는 승인상태 비교
		if(!applyDomain.getOldPlStat().equals(statCheck.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		//승인처리시 이메일 발송
		if(StringUtils.isEmpty(statCheck.getEmail())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이메일을 확인해 주세요.");
		}
		
		int emailResult = 0;
		EmailDomain emailDomain = new EmailDomain();
		emailDomain.setName("여신금융협회");
		emailDomain.setEmail(statCheck.getEmail());
		
		// API성공여부
		boolean apiCheck = false;
		
		if("5".equals(applyDomain.getPlStat())) {
			// 승인요청에 대한 보완
			emailDomain.setInstId("141");
			emailDomain.setSubsValue(statCheck.getMasterToId()+"|"+applyDomain.getPlHistTxt());		
			apiCheck = true;
		}else if("9".equals(applyDomain.getPlStat()) && "2".equals(applyDomain.getPlRegStat()) && "N".equals(applyDomain.getPreRegYn())) {
			// 승인요청에 대한 승인
			emailDomain.setInstId("142");
			emailDomain.setSubsValue(statCheck.getMasterToId());
			apiCheck = true;
		}else if("9".equals(applyDomain.getPlStat()) && "3".equals(applyDomain.getPlRegStat()) && "Y".equals(applyDomain.getPreRegYn())) {
			// 승인요청에 대한 승인이면서 기등록자인 경우(자격취득 / 완료)
			emailDomain.setInstId("143");
			emailDomain.setSubsValue(statCheck.getMasterToId());
			
			/*
			
			// 2021-06-25 은행연합회 API 통신 - 개인 등록
			String apiKey = kfbApiRepository.selectKfbApiKey();
			JsonObject jsonParam = new JsonObject();
			if("1".equals(statCheck.getPlClass())) {
				jsonParam.addProperty("pre_lc_num", applyDomain.getPreLcNum());
				responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.CheckLoanUrl, "POST");				
			}else {
				jsonParam.addProperty("pre_lc_num", applyDomain.getPreLcNum());
				responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.PreLoanCorpUrl, "POST");
			}
			
			if(responseMsg != null) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				if("success".equals(responseMsg.getCode())) {
					// 가등록에서 본등록시 등록번호 발급
					String lcNum = "";
					UserDomain userDomain = new UserDomain();
					if("1".equals(statCheck.getPlClass())) {
						lcNum = responseJson.getString("lc_num");
					}else {
						lcNum = responseJson.getString("lc_corp_num");
					}
					userDomain.setMasterSeq(applyDomain.getMasterSeq());
					userDomain.setPlRegistNo(lcNum);
					int updateCnt = kfbApiRepository.updateKfbApiByUserInfo(userDomain);
					if(updateCnt > 0) {
						apiCheck = true;
					}else {
						return new ResponseMsg(HttpStatus.OK, "fail", "API연동 후 내부데이터 오류 발생\n관리자에 문의해 주세요.");
					}
				}else {
					return new ResponseMsg(HttpStatus.OK, "fail", responseJson.getString("res_msg"));
				}
			}
			*/
			
			apiCheck = true;
			
		}else if("10".equals(applyDomain.getPlStat())) {
			// 승인요청에 대한 부적격
			emailDomain.setInstId("144");
			emailDomain.setSubsValue(statCheck.getMasterToId()+"|"+applyDomain.getPlHistTxt());

			/*
			
			// 2021-06-25 은행연합회 API 통신 - 개인 삭제
			String apiKey = kfbApiRepository.selectKfbApiKey();
			JsonObject jsonParam = new JsonObject();
			if("1".equals(statCheck.getPlClass())) {
				jsonParam.addProperty("pre_lc_num", applyDomain.getPreLcNum());
				responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.PreLoanUrl, "DELETE");
			}else {
				jsonParam.addProperty("pre_corp_lc_num", applyDomain.getPreLcNum());
				responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.PreLoanCorpUrl, "DELETE");
			}

			if(responseMsg != null) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				if("success".equals(responseMsg.getCode())) {
					apiCheck = true;
				}else {
					return new ResponseMsg(HttpStatus.OK, "fail", responseJson.getString("res_msg"));
				}
			}
			
			*/
			apiCheck = true;
			
		}else{
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		
		if(apiCheck) {
			int result = applyRepository.updateApplyPlStat(applyDomain);
			emailResult = emailRepository.sendEmail(emailDomain);
			// 임시 성공
			//emailResult = 1;
			if(emailResult > 0 && result > 0) {
				// 모집인단계이력
				applyRepository.insertMasterStep(applyDomain);
				return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
			}else if(emailResult == 0){
				return new ResponseMsg(HttpStatus.OK, "fail", "메일발송에 실패하였습니다.");
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "API오류가 발생하였습니다.");
		}
	}

	
	//모집인 조회 및 변경 > 첨부서류체크 등록
	@Transactional
	public ResponseMsg insertApplyCheck(ApplyCheckDomain applyCheckDomain){
		int result = applyRepository.insertApplyCheck(applyCheckDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	//모집인 조회 및 변경 > 첨부서류체크 삭제
	@Transactional
	public ResponseMsg deleteApplyCheck(ApplyCheckDomain applyCheckDomain){
		int result = applyRepository.deleteApplyCheck(applyCheckDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	//모집인 조회 및 변경 > 실무자확인
	@Transactional
	public ResponseMsg applyCheck(ApplyDomain applyDomain){
		
		int result = applyRepository.applycheck(applyDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	//기등록검증여부확인
	@Transactional
	public ResponseMsg prevRegCheck(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		ApplyDomain statCheck = applyRepository.getApplyDetail(applyDomain);
		return new ResponseMsg(HttpStatus.OK, "fail", "API오류가 발생하였습니다.");
	}
	
}
