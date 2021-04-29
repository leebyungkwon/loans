package com.loanscrefia.member.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.UserRepository;
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
	/*
	public int updateUserRegInfo(Map<String, MultipartFile> files, UserDomain userDomain){
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				//.setFiles(files)
				.setExt("doc")
				.uploadTest(files);
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				System.out.println("userService > fileSeq :::: "+file.get(0).getFileSeq());
			}
		}
		//수정
		
		return 0;
	}
	*/
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
