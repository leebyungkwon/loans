package com.loanscrefia.member.admin.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.admin.domain.AdminDomain;

@Mapper 
public interface AdminRepository {
	
	// 회원사 시스템 - 관리자 조회
	List<AdminDomain> selectAdminList(AdminDomain AdminDomain);
	
	// 회원사 시스템 - 관리자 상세 조회
	AdminDomain getAdmin(AdminDomain AdminDomain);
	
}