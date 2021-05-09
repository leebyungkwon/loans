package com.loanscrefia.member.admin.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.member.admin.domain.AdminDomain;

@Mapper 
public interface AdminRepository {
	
	// 회원사 시스템 - 관리자 조회
	List<AdminDomain> selectAdminList(AdminDomain adminDomain);
	
	// 회원사 시스템 - 관리자 상세
	AdminDomain getAdminDetail(AdminDomain adminDomain);
	
	// 회원사 시스템 - 수정 페이지
	AdminDomain getAdminDetailUpd(AdminDomain adminDomain);
	
	// 관리자 수정 저장
	AdminDomain saveAdminUpdate(AdminDomain adminDomain);

	// 회원사 조회 (체크박스 선택시) - > 삭제 
	int adminCheckDelete(AdminDomain adminDomain);
	
}