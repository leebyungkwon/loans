package com.loanscrefia.admin.recruit.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.admin.recruit.repository.RecruitRepository;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
import com.loanscrefia.member.user.domain.excel.UserCorpExcelDomain;
import com.loanscrefia.member.user.domain.excel.UserIndvExcelDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;

import sinsiway.CryptoUtil;

@Service
public class RecruitService {

	@Autowired private RecruitRepository recruitRepository;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private UserRepository userRepo;
	@Autowired
	private EmailRepository emailRepository;

	//모집인 조회 및 변경 > 리스트
	@Transactional(readOnly=true)
	public List<RecruitDomain> selectRecruitList(RecruitDomain recruitDomain){
		MemberDomain memberDomain = new MemberDomain();
		MemberDomain result = commonService.getMemberDetail(memberDomain);
		recruitDomain.setCreGrp(result.getCreGrp());
		
		// 주민번호 및 법인번호 암호화 후 비교
		recruitDomain.setPlMerchantNo(CryptoUtil.encrypt(recruitDomain.getPlMerchantNo()));
		recruitDomain.setPlMZId(CryptoUtil.encrypt(recruitDomain.getPlMZId()));
		
		List<RecruitDomain> recruitResultList = recruitRepository.selectRecruitList(recruitDomain);
		for(RecruitDomain list : recruitResultList) {
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
		return recruitResultList;
	}
	
	//모집인 조회 및 변경 > 상세 : 개인
	@Transactional(readOnly=true)
	public Map<String,Object> getRecruitIndvDetail(RecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		RecruitDomain recruitInfo = recruitRepository.getRecruitDetail(recruitDomain);
		
		//이력조회 시 변경된 데이터 찾기(현재 데이터와 다른 데이터중 최신)
		recruitDomain.setSearchPlMName(recruitInfo.getPlMName());
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone("");
		//이력상세 - 이름
		RecruitDomain recruitHistInfoName = recruitRepository.getRecruitHistDetail(recruitDomain);
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
		RecruitDomain recruitHistInfoZid = recruitRepository.getRecruitHistDetail(recruitDomain);
		if(recruitHistInfoZid != null) {
			if(!recruitHistInfoZid.getPlMZId().equals(recruitInfo.getPlMZId())) {
				
				// 주민번호 암호화 해제		
				StringBuilder histZid = new StringBuilder();
				if(StringUtils.isNotEmpty(recruitHistInfoZid.getPlMZId())) {
					histZid.append(CryptoUtil.decrypt(recruitHistInfoZid.getPlMZId()));
					histZid.insert(6, "-");
					recruitInfo.setHistPlMZId(histZid.toString());
					recruitInfo.setHistZidSeq(recruitHistInfoName.getMasterHistSeq());
				}
			}
		}
		
		//이력조회 시 변경된 데이터 찾기(현재 데이터와 다른 데이터중 최신)
		recruitDomain.setSearchPlMName("");
		recruitDomain.setSearchPlMZId("");
		recruitDomain.setSearchPlCellphone(recruitInfo.getPlCellphone().replaceAll("-", ""));
		//이력상세 - 연락처
		RecruitDomain recruitHistInfoPhone = recruitRepository.getRecruitHistDetail(recruitDomain);
		if(recruitHistInfoPhone != null) {
			if(!recruitHistInfoPhone.getPlCellphone().equals(recruitInfo.getPlCellphone())) {
				recruitInfo.setHistPlCellphone(recruitHistInfoPhone.getPlCellphone());
				recruitInfo.setHistPhoneSeq(recruitHistInfoName.getMasterHistSeq());
			}
		}
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
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
        			}
        		}
        	}
    	}
    	
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	//위반이력
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("recruitInfo", recruitInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
    	
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(등록정보 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getRecruitCorpDetail(RecruitDomain recruitDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		RecruitDomain recruitInfo 	= recruitRepository.getRecruitDetail(recruitDomain);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
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
        			}
        		}
        	}
        	
		}
		
    	UserDomain userDomain = new UserDomain();
    	userDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	//위반이력
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain = new PayResultDomain();
    	payResultDomain.setMasterSeq(recruitDomain.getMasterSeq());
    	PayResultDomain payResult = commonService.getPayResultDetail(payResultDomain);
		
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("recruitInfo", recruitInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		
		return result;
	}
	
	//모집인 조회 및 변경 > 상세 : 법인(대표자 및 임원관련사항 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getRecruitCorpImwonDetail(RecruitImwonDomain recruitImwonDomain){
		
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
		RecruitDomain dtlParam			= new RecruitDomain();
		dtlParam.setMasterSeq(recruitImwonDomain.getMasterSeq());
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//대표자 및 임원 리스트
		List<RecruitImwonDomain> imwonList = recruitRepository.selectRecruitCorpImwonList(recruitImwonDomain);
		
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
	public Map<String,Object> getRecruitCorpExpertDetail(RecruitExpertDomain recruitExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		RecruitDomain dtlParam			= new RecruitDomain();
		dtlParam.setMasterSeq(recruitExpertDomain.getMasterSeq());
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//전문인력 리스트
		List<RecruitExpertDomain> expertList = recruitRepository.selecRecruitCorpExpertList(recruitExpertDomain);
		
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
	public Map<String,Object> getRecruitCorpItDetail(RecruitItDomain recruitItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		RecruitDomain dtlParam			= new RecruitDomain();
		dtlParam.setMasterSeq(recruitItDomain.getMasterSeq());
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(dtlParam);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
			zid.insert(6, "-");
			recruitInfo.setPlMZId(zid.toString());
		}
		
		//전산인력 리스트
		List<RecruitItDomain> itList 	= recruitRepository.selectRecruitCorpItList(recruitItDomain);
		
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
	public Map<String,Object> getRecruitCorpEtcDetail(RecruitDomain recruitDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		RecruitDomain recruitInfo 		= recruitRepository.getRecruitDetail(recruitDomain);
		
		// ORIGIN 법인번호 암호화 해제
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getOriginPlMerchantNo())) {
			orgMerchantNo.append(CryptoUtil.decrypt(recruitInfo.getOriginPlMerchantNo()));
			orgMerchantNo.insert(6, "-");
			recruitInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(recruitInfo.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			recruitInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(recruitInfo.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(recruitInfo.getPlMZId()));
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
	public ResponseMsg updateRecruitPlStat(RecruitDomain recruitDomain){
		RecruitDomain statCheck = recruitRepository.getRecruitDetail(recruitDomain);
		
		// 현재 승인상태와 화면에 있는 승인상태 비교
		if(!recruitDomain.getOldPlStat().equals(statCheck.getPlStat())){
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
		
		if("3".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			// 변경요청에 대한 승인
			emailDomain.setInstId("145");
			//emailDomain.setSubsValue(statCheck.getMasterToId()+"|"+recruitDomain.getPlHistTxt());
			emailDomain.setSubsValue(statCheck.getMasterToId());
		}else if("6".equals(recruitDomain.getPlStat())) {
			// 변경요청에 대한 보완요청
			emailDomain.setInstId("146");
			emailDomain.setSubsValue(statCheck.getMasterToId()+"|"+recruitDomain.getPlHistTxt());
		}else if("4".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			// 해지요청에 대한 승인
			emailDomain.setInstId("147");
			emailDomain.setSubsValue(statCheck.getMasterToId());	
		}else if("2".equals(recruitDomain.getPlRegStat()) && "9".equals(recruitDomain.getPlStat())) {
			// 승인요청에 대한 승인
			emailDomain.setInstId("142");
			emailDomain.setSubsValue(statCheck.getMasterToId());
		}else{
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		
		
		int result = recruitRepository.updateRecruitPlStat(recruitDomain);
		// 임시처리
		emailResult = emailRepository.sendEmail(emailDomain);
		//emailResult = 1;
		if(emailResult > 0 && result > 0) {
			// 모집인단계이력
			recruitRepository.insertMasterStep(recruitDomain);
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else if(emailResult == 0){
			return new ResponseMsg(HttpStatus.OK, "fail", "메일발송에 실패하였습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
		
		
	}
	
	//변경사항
	@Transactional(readOnly=true)
	public RecruitDomain getRecruitHistDetail(RecruitDomain recruitDomain) {
		
		RecruitDomain histDetail = recruitRepository.getRecruitHistDetail(recruitDomain);
		
		// 법인번호 암호화 해제		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(histDetail.getPlMerchantNo())) {
			merchantNo.append(CryptoUtil.decrypt(histDetail.getPlMerchantNo()));
			merchantNo.insert(6, "-");
			histDetail.setPlMerchantNo(merchantNo.toString());
		}
		
		// 주민번호 암호화 해제		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(histDetail.getPlMZId())) {
			zid.append(CryptoUtil.decrypt(histDetail.getPlMZId()));
			zid.insert(6, "-");
			histDetail.setPlMZId(zid.toString());
		}
		
		return histDetail; 
	}
	
	
	
	//첨부파일 이력 조회
	@Transactional(readOnly=true)
	public Map<String,Object> getFileHistDetail(RecruitDomain recruitDomain){
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
