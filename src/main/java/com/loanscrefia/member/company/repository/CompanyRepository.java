package com.loanscrefia.member.company.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.company.domain.CompanyDomain;

@Mapper 
public interface CompanyRepository {
	
	// 회원사 회원 조회
	List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain);
	
	// 회원사 회원 상세조회
	CompanyDomain getCompany(CompanyDomain companyDomain);
	
}