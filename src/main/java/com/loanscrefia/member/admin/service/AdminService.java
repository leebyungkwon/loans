package com.loanscrefia.member.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.admin.repository.AdminRepository;
import com.loanscrefia.member.user.domain.UserDomain;

@Service
public class AdminService {

	@Autowired private AdminRepository AdminRepository;
	
	// 회원사 회원 조회
	@Transactional(readOnly=true)
	public List<AdminDomain> selectAdminList(AdminDomain AdminDomain){
		return AdminRepository.selectAdminList(AdminDomain);
	}
	
	//회원사 등록 -> 상세, 수정
	@Transactional(readOnly=true)
	public AdminDomain getAdminDetail(AdminDomain AdminDomain){
		return AdminRepository.getAdminDetail(AdminDomain);
	}
	
	// 회원사 수정 -> Insert
	@Transactional
	public ResponseMsg saveAdminUpdate(AdminDomain AdminDomain){
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	AdminDomain.setPassword(passwordEncoder.encode(AdminDomain.getPassword()));
    	AdminDomain = AdminRepository.saveAdminUpdate(AdminDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "관리자 수정에 성공하였습니다.");
	}
}
