package com.loanscrefia.admin.recruit.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.admin.recruit.repository.RecruitRepository;
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
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;
import com.loanscrefia.util.UtilMask;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class RecruitService {

	@Autowired private RecruitRepository recruitRepository;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private UserRepository userRepo;
	@Autowired private EmailRepository emailRepository;
	@Autowired private KfbApiRepository kfbApiRepository;
	@Autowired private KfbApiService kfbApiService;

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
	public List<RecruitDomain> selectRecruitList(RecruitDomain recruitDomain){
		
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
		
		List<RecruitDomain> recruitResultList = recruitRepository.selectRecruitList(recruitDomain);
		String plMZId = "";
		for(RecruitDomain list : recruitResultList) {
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
	public Map<String,Object> getRecruitIndvDetail(RecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		RecruitDomain recruitInfo = recruitRepository.getRecruitDetail(recruitDomain);
		
		//???????????? ??? ????????? ????????? ??????(?????? ???????????? ?????? ???????????? ??????)
		recruitDomain.setSearchPlMName(recruitInfo.getPlMName());
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone("");
		
		//???????????? - ??????
		RecruitDomain recruitHistInfoName = recruitRepository.getRecruitHistDetail(recruitDomain);
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
		RecruitDomain recruitHistInfoZid = recruitRepository.getRecruitHistDetail(recruitDomain);
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
		RecruitDomain recruitHistInfoPhone = recruitRepository.getRecruitHistDetail(recruitDomain);
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
        			}
        		}
        	}
    	}
    	
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	//????????????
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
    	
    	//????????????
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
    	
    	//??????
    	result.put("addrCodeList", addrCodeList);
    	result.put("recruitInfo", recruitInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
    	
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getRecruitCorpDetail(RecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		RecruitDomain recruitInfo 	= recruitRepository.getRecruitDetail(recruitDomain);
		
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
		
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	//????????????
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
    	
    	//????????????
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
		
		//??????
		result.put("addrCodeList", addrCodeList);
		result.put("recruitInfo", recruitInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(????????? ??? ?????????????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getRecruitCorpImwonDetail(RecruitImwonDomain recruitImwonDomain){
		
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
		RecruitDomain dtlParam			= new RecruitDomain();
		dtlParam.setMasterSeq(recruitImwonDomain.getMasterSeq());
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(dtlParam);
		
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
		List<RecruitImwonDomain> imwonList = recruitRepository.selectRecruitCorpImwonList(recruitImwonDomain);
		
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
	public Map<String,Object> getRecruitCorpExpertDetail(RecruitExpertDomain recruitExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????(??????,??????) ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		RecruitDomain dtlParam			= new RecruitDomain();
		dtlParam.setMasterSeq(recruitExpertDomain.getMasterSeq());
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(dtlParam);
		
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
		List<RecruitExpertDomain> expertList = recruitRepository.selecRecruitCorpExpertList(recruitExpertDomain);
		
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
	public Map<String,Object> getRecruitCorpItDetail(RecruitItDomain recruitItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
		RecruitDomain dtlParam			= new RecruitDomain();
		dtlParam.setMasterSeq(recruitItDomain.getMasterSeq());
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(dtlParam);
		
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
		List<RecruitItDomain> itList 	= recruitRepository.selectRecruitCorpItList(recruitItDomain);
		
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
	public Map<String,Object> getRecruitCorpEtcDetail(RecruitDomain recruitDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(recruitDomain);
		
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
	public ResponseMsg updateRecruitPlStat(RecruitDomain recruitDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", null,  "????????? ?????????????????????.");
		RecruitDomain statCheck = recruitRepository.getRecruitDetail(recruitDomain);
		
		//????????????
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	userDomain.setVioNum("empty");
    	// ??????????????? ????????????
    	List<UserDomain> violationRegList = userRepo.selectUserViolationInfoList(userDomain);
    	
    	// ??????????????? ????????????
    	userDomain.setVioNum("notEmpty");
    	List<UserDomain> violationDelList = userRepo.selectUserViolationInfoList(userDomain);    	
    	
		
		// ?????? ??????????????? ????????? ?????? ???????????? ??????
		if(!recruitDomain.getOldPlStat().equals(statCheck.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}
		
		//??????????????? ????????? ??????
		if(StringUtils.isEmpty(statCheck.getEmail())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "???????????? ????????? ?????????.");
		}
		
		int emailResult = 0;
		EmailDomain emailDomain = new EmailDomain();
		emailDomain.setName("??????????????????");
		emailDomain.setEmail(statCheck.getEmail());
		
		
		// API????????????
		boolean apiCheck = false;
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		
		if("3".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			// ??????????????? ?????? ??????
			emailDomain.setInstId("145");
			//emailDomain.setSubsValue(statCheck.getMasterToId()+"|"+recruitDomain.getPlHistTxt());
			emailDomain.setSubsValue(statCheck.getMasterToId());
			
			if(kfbApiApply) {
				String prdCheck = statCheck.getPlProduct();
				// ???????????? TM??????(3), TM??????(6) ??????
				if("01".equals(prdCheck) || "05".equals(prdCheck)) {
					String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
					JSONObject jsonParam = new JSONObject();
					JSONObject jsonArrayParam = new JSONObject();
					JSONArray jsonArray = new JSONArray();

					// 2021-07-04 ??????????????? API ?????? - ??????
					// ??????????????? ???????????? + ???????????? = ????????????, ??????????????? ????????? ???????????????
					// ???????????? ????????? ??????????????? ?????? API ??????
					// ??????????????? ??????????????? API?????? ????????? ?????? ?????? ??? ???????????? ??????
					String plClass = statCheck.getPlClass();
					if("1".equals(plClass)) {
						jsonParam.put("lc_num", statCheck.getPlRegistNo());
						jsonParam.put("name", statCheck.getPlMName());
						
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("con_mobile", statCheck.getPlCellphone());
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						jsonArrayParam.put("cancel_date", "");
						jsonArrayParam.put("cancel_code", "");
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						log.info("########################");
						log.info("JSONObject In JSONArray :: " + jsonParam);
						log.info(":::::::::::::API?????? ?????? :::::::::");
						log.info("########################");
						
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "PUT", plClass, "N");
						if("success".equals(responseMsg.getCode())) {
							// ???????????? ?????? API ?????? - ??????????????? ??????
							boolean zIdCheck = false;
							JSONObject zIdParam = new JSONObject();
							if(zIdCheck) {
								zIdParam.put("bef_ssn", "");											// ?????? ??? ???????????? - API ????????? ?????????
								zIdParam.put("aft_ssn", CryptoUtil.decrypt(statCheck.getPlMZId()));	// ?????? ??? ???????????? - DB?????????
								log.info("########################");
								log.info("jsonParam :: " + zIdParam);
								log.info("########################");
								responseMsg = kfbApiService.commonKfbApi(apiKey, zIdParam, KfbApiService.ApiDomain+KfbApiService.modUrl, "PUT", plClass, "N");
								if(!"success".equals(responseMsg.getCode())) {
									return responseMsg;
								}
							}
							
							apiCheck = true;
							
						}else {
							return responseMsg;
						}
						
					}else {
						jsonParam.put("corp_lc_num", statCheck.getPlRegistNo());					// ????????????
						jsonParam.put("corp_name", statCheck.getPlMerchantName());					// ?????????
						jsonParam.put("corp_rep_name", statCheck.getPlCeoName());					// ???????????????
						jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(statCheck.getPlMZId()));	// ????????????????????????
						//jsonParam.put("corp_rep_ci", statCheck.getCi());							// ????????????CI
						
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						jsonArrayParam.put("cancel_date", "");
						jsonArrayParam.put("cancel_code", "");
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						log.info("########################");
						log.info("JSONObject In JSONArray :: " + jsonParam);
						log.info(":::::::::::::API?????? ?????? :::::::::");
						log.info("########################");
						
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "PUT", plClass, "N");
						if(!"success".equals(responseMsg.getCode())) {
							return responseMsg;
						}
					}
					
					log.error("#######???????????? ?????? ??? ?????? ?????? ##########");
					log.error("#######???????????? ?????? ??? ?????? ?????? ##########");
					log.error("#######"+violationRegList.size()+"##########");
					log.error("#######???????????? ?????? ??? ?????? ?????? ##########");
					log.error("#######???????????? ?????? ??? ?????? ?????? ##########");
					
					if(violationRegList.size() > 0) {
						// ???????????? ??????
						for(UserDomain regVio : violationRegList) {
							JSONObject jsonRegVioParam = new JSONObject();
							jsonRegVioParam.put("lc_num", statCheck.getPlRegistNo());					// ????????????
							jsonRegVioParam.put("ssn", CryptoUtil.decrypt(statCheck.getPlMZId()));		// ??????????????????
							jsonRegVioParam.put("vio_fin_code", statCheck.getComCode());				// ??????????????????
							jsonRegVioParam.put("vio_code", regVio.getViolationCd());					// ??????????????????
							jsonRegVioParam.put("vio_date", regVio.getRegTimestamp());					// ?????????
							
							responseMsg = kfbApiService.violation(apiKey, jsonRegVioParam, "POST");
							if("success".equals(responseMsg.getCode())) {
								JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
								String apiVioNum = responseJson.getString("vio_num");
								UserDomain vioRegDomain = new UserDomain();
								vioRegDomain.setVioNum(apiVioNum);
								vioRegDomain.setViolationSeq(regVio.getViolationSeq());
								userRepo.updateUserViolationInfo(vioRegDomain);
							}else {
								return responseMsg;
							}
						}
					}
					
					if(violationDelList.size() > 0) {
						// ???????????? ??????
						for(UserDomain delVio : violationDelList) {
							JSONObject jsonDelVioParam = new JSONObject();
							jsonDelVioParam.put("vio_num", delVio.getVioNum());								// ??????????????????
							responseMsg = kfbApiService.violation(apiKey, jsonDelVioParam, "DELETE");
							if("success".equals(responseMsg.getCode())) {
								UserDomain vioDelDomain = new UserDomain();
								vioDelDomain.setViolationSeq(delVio.getViolationSeq());
								userRepo.deleteUserViolationInfo(vioDelDomain);
							}else {
								return responseMsg;
							}
						}
					}
					
					if("success".equals(responseMsg.getCode())) {
						apiCheck = true;
					}else {
						return responseMsg;
					}
					
				}else {
					apiCheck = true;
				}
			}else {
				apiCheck = true;
			}
			
		}else if("6".equals(recruitDomain.getPlStat())) {
			// ??????????????? ?????? ????????????
			emailDomain.setInstId("146");
			emailDomain.setSubsValue(statCheck.getMasterToId()+"|"+recruitDomain.getPlHistTxt());
			apiCheck = true;
		}else if("4".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			// ??????????????? ?????? ??????
			emailDomain.setInstId("147");
			emailDomain.setSubsValue(statCheck.getMasterToId());
			
			if(kfbApiApply) {
				// 2021-07-04 ??????????????? API ?????? - ??????
				String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
				JSONObject jsonParam = new JSONObject();
				JSONObject jsonArrayParam = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				
				String plClass = statCheck.getPlClass();
				String prdCheck = statCheck.getPlProduct();
				if("01".equals(prdCheck) || "05".equals(prdCheck)) {
					SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					Date currentDt = new Date();
					String cancelDt = dateFormatParser.format(currentDt);
					
					if("1".equals(plClass)) {
						jsonParam.put("lc_num", statCheck.getPlRegistNo());
						jsonParam.put("name", statCheck.getPlMName());
						
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("con_mobile", statCheck.getPlCellphone());
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						//jsonArrayParam.put("cancel_date", statCheck.getCreHaejiDate().replaceAll("-", ""));
						jsonArrayParam.put("cancel_date", cancelDt);
						jsonArrayParam.put("cancel_code", statCheck.getPlHistCd());
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						log.info("########################");
						log.info("JSONObject In JSONArray :: " + jsonParam);
						log.info(":::::::::::::API?????? ?????? :::::::::");
						log.info("########################");
						
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "PUT", plClass, "N");
						
					}else {
						jsonParam.put("corp_lc_num", statCheck.getPlRegistNo());					// ????????????
						jsonParam.put("corp_name", statCheck.getPlMerchantName());					// ?????????
						jsonParam.put("corp_rep_name", statCheck.getPlCeoName());					// ???????????????
						jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(statCheck.getPlMZId()));	// ????????????????????????
						//jsonParam.put("corp_rep_ci", statCheck.getCi());							// ????????????CI
						
						jsonArrayParam.put("con_num", statCheck.getConNum());
						jsonArrayParam.put("con_date", statCheck.getComContDate().replaceAll("-", ""));
						jsonArrayParam.put("fin_phone", "");
						jsonArrayParam.put("loan_type", statCheck.getPlProduct());
						//jsonArrayParam.put("cancel_date", statCheck.getCreHaejiDate().replaceAll("-", ""));
						jsonArrayParam.put("cancel_date", cancelDt);
						jsonArrayParam.put("cancel_code", statCheck.getPlHistCd());
						
						jsonArray.put(jsonArrayParam);
						jsonParam.put("con_arr", jsonArray);
						
						log.info("########################");
						log.info("JSONObject In JSONArray :: " + jsonParam);
						log.info(":::::::::::::API?????? ?????? :::::::::");
						log.info("########################");
						
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "PUT", plClass, "N");
					}
					
					if("success".equals(responseMsg.getCode())) {
						apiCheck = true;
					}else {
						return responseMsg;
					}
				}else {
					// TM??????, TM?????? ??????
					apiCheck = true;				
				}
			}else {
				apiCheck = true;
			}
			
		}else if("2".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			// ??????????????? ?????? ?????? - ????????????
			emailDomain.setInstId("142");
			emailDomain.setSubsValue(statCheck.getMasterToId());
			apiCheck = true;
		}else{
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}
		
		if(apiCheck) {
			//????????? ?????? ??????
			UserDomain param = new UserDomain();
			param.setMasterSeq(recruitDomain.getMasterSeq());
			userRepo.insertUserHistory(param);
			
			//????????? ?????? ??????
			if(StringUtils.isNotEmpty(recruitDomain.getPlRegStat())) {
				if(recruitDomain.getPlRegStat().equals("4") && statCheck.getUserSeq() != null) { //2021.12.20 ??????
					recruitDomain.setRegPath("F");
				}
			}
			int result = recruitRepository.updateRecruitPlStat(recruitDomain);
			
			//????????? ??????
			if(emailApply) {
				emailResult = emailRepository.sendEmail(emailDomain);
			}else {
				emailResult = 1;
			}
			
			if(emailResult > 0 && result > 0) {
				// ?????????????????????
				recruitRepository.insertMasterStep(recruitDomain);
				return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "?????????????????????.");
			}else if(emailResult == 0){
				return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ?????????????????????.");
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", responseMsg,  "API????????? ?????????????????????.");
		}
	}
	
	//????????????
	@Transactional(readOnly=true)
	public RecruitDomain getRecruitHistDetail(RecruitDomain recruitDomain) {
		
		RecruitDomain histDetail = recruitRepository.getRecruitHistDetail(recruitDomain);
		
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
	public Map<String,Object> getFileHistDetail(RecruitDomain recruitDomain){
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
	
	// 2021-11-02 ???????????? ?????? ??????
	@Transactional
	public ResponseMsg updatePlRegistNo(RecruitDomain recruitDomain) throws IOException{
		int result = recruitRepository.updatePlRegistNo(recruitDomain);
		if(result > 0) {
			UserDomain param = new UserDomain();
			param.setMasterSeq(recruitDomain.getMasterSeq());
			userRepo.insertUserHistory(param);
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");			
		}
	}
	
	// 2021-11-02 ???????????? ?????? ??????
	@Transactional
	public ResponseMsg updateConNum(RecruitDomain recruitDomain) throws IOException{
		int result = recruitRepository.updateConNum(recruitDomain);
		if(result > 0) {
			UserDomain param = new UserDomain();
			param.setMasterSeq(recruitDomain.getMasterSeq());
			userRepo.insertUserHistory(param);
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");			
		}
	}
	
	
	
}
