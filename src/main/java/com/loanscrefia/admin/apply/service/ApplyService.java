package com.loanscrefia.admin.apply.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.repository.ApplyRepository;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;

@Service
public class ApplyService {

	@Autowired private ApplyRepository applyRepository;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private UserRepository userRepo;

	//모집인 조회 및 변경 > 리스트
	@Transactional(readOnly=true)
	public List<ApplyDomain> selectApplyList(ApplyDomain applyDomain){
		return applyRepository.selectApplyList(applyDomain);
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
		
		//대표자 및 임원 리스트
		List<ApplyImwonDomain> imwonList = applyRepository.selectApplyCorpImwonList(applyImwonDomain);
		
		//첨부파일
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
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
		
		//전문인력 리스트
		List<ApplyExpertDomain> expertList = applyRepository.selecApplyCorpExpertList(applyExpertDomain);
		
		//첨부파일
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
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
		
		//전산인력 리스트
		List<ApplyItDomain> itList 	= applyRepository.selectApplyCorpItList(applyItDomain);
		
		//첨부파일
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
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
		ApplyDomain statCheck = applyRepository.getApplyDetail(applyDomain);
		if(!applyDomain.getOldPlStat().equals(statCheck.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		int result = applyRepository.updateApplyPlStat(applyDomain);
		if(result > 0) {
			// 모집인단계이력
			applyRepository.insertMasterStep(applyDomain);
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
		
		/*
		//승인처리시 이메일 발송
		if(StringUtils.isEmpty(statCheck.getEmail())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이메일을 확인해 주세요.");
		}
		int emailResult = 0;
		int result = applyRepository.updateApplyPlStat(applyDomain);
		
		SendEmailDomain emailDomain = new SendEmailDomain();
		emailDomain.setInstId("추후고정값");
		emailDomain.setName("여신금융협회");
		emailDomain.setEmail(applyDomain.getEmail());
		
		if(applyDomain.getPlStat() == "7" && applyDomain.getPlRegStat() == "2") {
			// 승인요청에 대한 승인
			emailDomain.setSubsValue(applyDomain.getMemberNm()+"|"+applyDomain.getEmail());
		}else if(applyDomain.getPlStat() == "7" && applyDomain.getPlRegStat() == "4") {
			// 해지요청에 대한 승인
			emailDomain.setSubsValue(applyDomain.getMemberNm()+"|"+applyDomain.getEmail());
		}else if(applyDomain.getPlStat() == "7" && applyDomain.getPlRegStat() == "3") {
			// 변경요청에 대한 승인
			emailDomain.setSubsValue(applyDomain.getMemberNm()+"|"+applyDomain.getEmail());
		}
		
		emailResult = commonRepository.sendEmail(emailDomain);
		if(emailResult > 0) {
			// 모집인단계이력
			applyRepository.insertMasterStep(applyDomain);
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		*/
		
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
	
}
