package com.loanscrefia.member.admin.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.admin.repository.AdminRepository;
import com.loanscrefia.util.UtilFile;

@Service
public class AdminService {

	@Autowired private AdminRepository AdminRepository;
	@Autowired private UtilFile utilFile;
	
	// 회원사 회원 조회
	@Transactional(readOnly=true)
	public List<AdminDomain> selectAdminList(AdminDomain adminDomain){
		return AdminRepository.selectAdminList(adminDomain);
	}
	
	// 회원사 조회 -> 상세
	@Transactional(readOnly=true)
	public AdminDomain getAdminDetail(AdminDomain adminDomain){
		return AdminRepository.getAdminDetail(adminDomain);
	}
	
	// 회원사 상세 -> 수정
	@Transactional(readOnly=true)
	public AdminDomain getAdminDetailUpd(AdminDomain adminDomain){
		return AdminRepository.getAdminDetailUpd(adminDomain);
	}
	
	// 회원사 수정 -> Insert
	@Transactional
	public ResponseMsg saveAdminUpdate(MultipartFile[] files, AdminDomain adminDomain){
		
		Map<String, Object> ret = utilFile.setPath("signup") 
										.setFiles(files)
										.setExt("all") 
										.upload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				adminDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		if(StringUtils.isNotEmpty(adminDomain.getPassword())) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			adminDomain.setPassword(passwordEncoder.encode(adminDomain.getPassword()));
		}
    	String memCheck = adminDomain.getTempMemberCheck();
    	adminDomain = AdminRepository.saveAdminUpdate(adminDomain);
    	if("Y".equals(memCheck)) {
    		return new ResponseMsg(HttpStatus.OK, "COM0001", "정보변경이 완료되었습니다. \n재승인을 요청해 주세요. \n승인 후에 로그인 가능합니다.");
    	}else {
    		return new ResponseMsg(HttpStatus.OK, "COM0001", "관리자 수정에 성공하였습니다.");
    	}
		
	}

	// 회원사 조회 (체크박스 선택시) - > 삭제 
	@Transactional
	public int adminCheckDelete(AdminDomain adminDomain){
		int result 			= 0;
		
		try {
			result = AdminRepository.adminCheckDelete(adminDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// 재승인 요청
	@Transactional
	public int reAppr(MultipartFile[] files, AdminDomain adminDomain) {
		
		Map<String, Object> ret = utilFile.setPath("signup") 
										.setFiles(files)
										.setExt("all") 
										.upload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				adminDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}else {
			return -1;
		}
		
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	adminDomain.setPassword(passwordEncoder.encode(adminDomain.getPassword()));
    	int result = AdminRepository.reAppr(adminDomain);
    	
		return result;
	}
	
	
}
