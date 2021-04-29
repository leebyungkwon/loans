package com.loanscrefia.member.user.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.member.user.domain.UserDomain;
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
	@Transactional
	public int insertUserRegInfoByExcel(MultipartFile[] files, UserDomain userDomain){
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("excelTest")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelParam = new ArrayList<Map<String, Object>>();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				String filePath = Paths.get(file.get(0).getFilePath(), file.get(0).getFileSaveNm()+"."+file.get(0).getFileExt()).toString();
				excelParam 		= new UtilExcel().upload(filePath);
				System.out.println("userService >>>>> result.size() >>>>> "+excelParam.size());
				userDomain.setExcelParam(this.excelParamValid(userDomain.getPlClass(),excelParam));
				//userDomain.setExcelParam(excelParam);
			}
		}
		//등록
		int result= 0;
		
		if(userDomain.getPlClass() != null && !userDomain.getPlClass().equals("")) {
			if(userDomain.getPlClass().equals("1")) {
				//개인
				userDomain.setComCode(1);
				userDomain.setMemberSeq(1);
				
				result = userRepo.insertUserRegIndvInfoByExcel(userDomain);
			}else {
				//법인
				result = userRepo.insertUserRegCorpInfoByExcel(userDomain);
			}
		}
		return result;
	}
	
	//나중에 하자.....
	public List<Map<String, Object>> excelParamValid(String plClass, List<Map<String, Object>> excelParam){
		
		if(plClass != null && !plClass.equals("")) {
			if(plClass.equals("1")) {
				//개인
				for(int i = 0;i < excelParam.size();i++) {
					Map<String, Object> map = excelParam.get(i);
					
					map.replace("C", map.get("C").toString().replaceAll("-", ""));
					map.replace("D", map.get("D").toString().replaceAll("-", ""));
					map.replace("H", map.get("H").toString().replaceAll("-", ""));
					map.replace("I", Integer.parseInt(map.get("I").toString()));
					map.replace("J", map.get("J").toString().replaceAll("-", ""));
					map.replace("K", map.get("K").toString().replaceAll("-", ""));
					map.replace("L", map.get("L").toString().replaceAll("-", ""));
					map.replace("M", map.get("M").toString().replaceAll("-", ""));
				}
			}else {
				//법인
				for(int i = 0;i < excelParam.size();i++) {
					Map<String, Object> map = excelParam.get(i);
					
					map.replace("C", map.get("C").toString().replaceAll("-", ""));
					map.replace("D", map.get("D").toString().replaceAll("-", ""));
					map.replace("F", Integer.parseInt(map.get("F").toString()));
					map.replace("H", map.get("H").toString().replaceAll("-", ""));
					map.replace("I", map.get("I").toString().replaceAll("-", ""));
				}
			}
		}
		return excelParam;
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
