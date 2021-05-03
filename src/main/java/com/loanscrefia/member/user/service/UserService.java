package com.loanscrefia.member.user.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.excel.UserCorpExcelDomain;
import com.loanscrefia.member.user.domain.excel.UserIndvExcelDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;

@Service
public class UserService {

	@Autowired private UserRepository userRepo;
	@Autowired private UtilFile utilFile;
	
	//모집인 등록 > 리스트
	@Transactional(readOnly=true)
	public List<UserDomain> selectUserRegList(UserDomain userDomain){
		return userRepo.selectUserRegList(userDomain);
	}
	
	//모집인 등록 > 등록
	/*
	@Transactional
	public int insertUserRegInfoByExcel(MultipartFile[] files, UserDomain userDomain){
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				String filePath = Paths.get(file.get(0).getFilePath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelResult 	= new UtilExcel().upload(filePath,UserIndvExcelDomain.class);
				
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
				}else {
					
				}
				
				//System.out.println("userService >>>>> excelResult.size() >>>>> "+excelResult.size());
				System.out.println("userService >>>>> excelResult.get(0) >>>>> "+excelResult.get(0).get("errorMsgMap"));
				System.out.println("userService >>>>> excelResult.get(1) >>>>> "+excelResult.get(1));
				
				//userDomain.setExcelParam(this.excelParamValid(userDomain.getPlClass(),excelParam));
				//userDomain.setExcelParam(excelParam);
			}
		}
		//등록
		int result= 0;
		
		if(userDomain.getPlClass() != null && !userDomain.getPlClass().equals("")) {
			if(userDomain.getPlClass().equals("1")) {
				//개인
				userDomain.setComCode(1);
				
				result = userRepo.insertUserRegIndvInfoByExcel(userDomain);
			}else {
				//법인
				result = userRepo.insertUserRegCorpInfoByExcel(userDomain);
			}
		}
		return result;
	}
	*/
	
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
					System.out.println("전 userDomain.getRegSeq() >>>>>>>>>>>>>>>>>>>>>> "+userDomain.getRegSeq());
					userDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegIndvInfoByExcel(userDomain);
					System.out.println("후 userDomain.getRegSeq() >>>>>>>>>>>>>>>>>>>>>> "+userDomain.getRegSeq());
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
	
	//모집인 등록 > 법인 : 대표자 및 임원
	
	//모집인 등록 > 법인 : 업무 수행이 필요한 전문성을 갖춘 인력에 관한 사항
	
	//모집인 등록 > 법인 : 전산 설비 운영,유지 및 관리를 전문적으로 수행할 수 있는 인력에 관한 사항
	
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
	
	//모집인 등록 > 상세
	@Transactional(readOnly=true)
	public UserDomain getUserRegDetail(UserDomain userDomain){
		return userRepo.getUserRegDetail(userDomain);
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
