package com.loanscrefia.admin.company.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.member.user.domain.UserDomain;

@Mapper
public interface CompanyRepository {
	
	// 협회 - 회원사 담당자 리스트 페이지
	List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain);
	
	//회원사 당담자 상세 리스트 조회
	CompanyDomain getCompanyDetail(CompanyDomain companyDomain);
}
