package com.loanscrefia.member.admin.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.user.domain.UserDomain;

@Mapper 
public interface AdminRepository {
	
	// 회원사 시스템 - 관리자 조회
	List<AdminDomain> selectAdminList(AdminDomain AdminDomain);
	
	// 회원사 시스템 - 관리자 상세, 수정 페이지
	AdminDomain getAdminDetail(AdminDomain AdminDomain);
	
	// 관리자 수정 저장
	AdminDomain saveAdminUpdate(AdminDomain AdminDomain);
}