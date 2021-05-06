package com.loanscrefia.member.user.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
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

@Service
public class UserService {

	@Autowired private UserRepository userRepo;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private UtilFile utilFile;
	
	//모집인 등록 > 리스트
	@Transactional(readOnly=true)
	public List<UserDomain> selectUserRegList(UserDomain userDomain){
		return userRepo.selectUserRegList(userDomain);
	}
	
	//모집인 등록 > 개인
	@Transactional
	public ResponseMsg indvExcelUpload(MultipartFile[] files, UserDomain userDomain){
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath = Paths.get(file.get(0).getFileFullPath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelResult 	= new UtilExcel().upload(filePath, UserIndvExcelDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegIndvInfoByExcel(userDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "", "success", "");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "", "fail", "");
	}
	
	//모집인 등록 > 법인
	@Transactional
	public ResponseMsg corpExcelUpload(MultipartFile[] files, UserDomain userDomain){
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath = Paths.get(file.get(0).getFileFullPath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelResult 	= new UtilExcel().upload(filePath, UserCorpExcelDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpInfoByExcel(userDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "", "success", "");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "", "fail", "");
	}
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 등록
	@Transactional
	public ResponseMsg corpImwonExcelUpload(MultipartFile[] files, UserImwonDomain userImwonDomain){
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath = Paths.get(file.get(0).getFileFullPath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelResult 	= new UtilExcel().upload(filePath, UserImwonDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userImwonDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpImwonInfoByExcel(userImwonDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "", "success", "");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "", "fail", "");
	}
	
	//모집인 등록 > 법인 : 전문인력 정보 등록
	@Transactional
	public ResponseMsg corpExpertExcelUpload(MultipartFile[] files, UserExpertDomain userExpertDomain){
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath = Paths.get(file.get(0).getFileFullPath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelResult 	= new UtilExcel().upload(filePath, UserExpertDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userExpertDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpExpertInfoByExcel(userExpertDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "", "success", "");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "", "fail", "");
	}
	
	//모집인 등록 > 법인 : 전산인력 정보 등록
	@Transactional
	public ResponseMsg corpItExcelUpload(MultipartFile[] files, UserItDomain userItDomain){
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath = Paths.get(file.get(0).getFileFullPath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelResult 	= new UtilExcel().upload(filePath, UserItDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userItDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpItInfoByExcel(userItDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "", "success", "");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "", "fail", "");
	}
	
	//모집인 등록 > 승인요청
	@Transactional
	public int updatePlRegStat(UserDomain userDomain){
		
		int result 			= 0;
		int[] masterSeqArr 	= userDomain.getMasterSeqArr();
		
		for(int i = 0;i < masterSeqArr.length;i++) {
			userDomain.setMasterSeq(masterSeqArr[i]);
			result += userRepo.updatePlRegStat(userDomain);
		}
		
		return result;
	}
	
	//모집인 등록 > 상세 : 개인
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegIndvDetail(UserDomain userDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		UserDomain userRegInfo = userRepo.getUserRegDetail(userDomain);
		
		//첨부파일
		FileDomain fileType1 = null;
		FileDomain fileType2 = null;
		FileDomain fileType3 = null;
		FileDomain fileType4 = null;
		FileDomain fileType5 = null;
		FileDomain fileType6 = null;
		FileDomain fileType7 = null;
		
    	if(userRegInfo.getFileSeq() != null) {
    		FileDomain fileParam = new FileDomain();
    		
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList = commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				fileType1 = fileList.get(i);
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				fileType2 = fileList.get(i);
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				fileType3 = fileList.get(i);
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				fileType4 = fileList.get(i);
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				fileType5 = fileList.get(i);
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				fileType6 = fileList.get(i);
        			}else if(fileList.get(i).getFileType().equals("7")) {
        				fileType7 = fileList.get(i);
        			}
        		}
        	}
    	}
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("userRegInfo", userRegInfo);
    	result.put("fileType1", fileType1);
    	result.put("fileType2", fileType2);
    	result.put("fileType3", fileType3);
    	result.put("fileType4", fileType4);
    	result.put("fileType5", fileType5);
    	result.put("fileType6", fileType6);
    	result.put("fileType7", fileType7);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegCorpDetail(UserDomain userDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(userDomain);
		int masterSeq			= userRegInfo.getMasterSeq();
		
		//대표자 및 임원 리스트
		UserImwonDomain imwonParam 		= new UserImwonDomain();
		imwonParam.setMasterSeq(masterSeq);
		List<UserImwonDomain> imwonList = userRepo.selectUserRegCorpImwonList(imwonParam);
		
		//전문인력 리스트
		UserExpertDomain expertParam 		= new UserExpertDomain();
		expertParam.setMasterSeq(masterSeq);
		List<UserExpertDomain> expertList 	= userRepo.selectUserRegCorpExpertList(expertParam);
		
		//전산인력 리스트
		UserItDomain itParam			= new UserItDomain();
		itParam.setMasterSeq(masterSeq);
		List<UserItDomain> itList 		= userRepo.selectUserRegCorpItList(itParam);
		
		//첨부파일
		
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("userRegInfo", userRegInfo);
		result.put("imwonList", imwonList);
		result.put("expertList", expertList);
		result.put("itList", itList);
		
		return result;
	}
		
	//모집인 등록 > 수정
	public int updateUserRegInfo(MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("doc")
				.setEntity(fileDomain)
				.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				System.out.println("userService > fileGrpSeq :::: "+file.get(0).getFileGrpSeq());
				userDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}
		}
		//수정
		int result = userRepo.updateUserRegInfo(userDomain);
		
		return result;
	}
}
