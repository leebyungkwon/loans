package com.loanscrefia.member.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.admin.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired private AdminRepository AdminRepository;
	

	// 회원사 회원 조회
	@Transactional(readOnly=true)
	public List<AdminDomain> selectAdminList(AdminDomain AdminDomain){
		return AdminRepository.selectAdminList(AdminDomain);
	}
	
	
	// 회원사 회원 상세 조회
	@Transactional(readOnly=true)
	public AdminDomain getAdmin(AdminDomain AdminDomain){
		return AdminRepository.getAdmin(AdminDomain);
	}
	
}
