package com.loanscrefia.member.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.member.company.domain.CompanyDomain;
import com.loanscrefia.member.company.repository.CompanyRepository;

@Service
public class CompanyService {

	@Autowired private CompanyRepository companyRepository;
	

	// 회원사 회원 조회
	@Transactional(readOnly=true)
	public List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain){
		return companyRepository.selectCompanyList(companyDomain);
	}
	
	
	// 회원사 회원 상세 조회
	@Transactional(readOnly=true)
	public CompanyDomain getCompany(CompanyDomain companyDomain){
		return companyRepository.getCompany(companyDomain);
	}
	
}
