package com.loanscrefia.admin.company.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.company.domain.CompanyDomain;

@Mapper
public interface CompanyRepository {
	
	List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain);
	
	List<CompanyDomain> selectCompanyDetail(CompanyDomain companyDomain);
	
	/*
	 * void getDetail(CompanyDomain companyDomain) return companyDomain;
	 */
}
