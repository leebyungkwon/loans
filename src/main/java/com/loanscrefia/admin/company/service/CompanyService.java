package com.loanscrefia.admin.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.repository.CompanyRepository;

@Service
public class CompanyService {

	@Autowired private CompanyRepository companyRepository;
	
	//회원사 담당자 리스트 조회
	@Transactional(readOnly = true)
	public List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain){
		return companyRepository.selectCompanyList(companyDomain);
	}
	
	//회원사 당담자 상세 리스트 조회
	@Transactional(readOnly=true)
	public CompanyDomain getCompanyDetail(CompanyDomain companyDomain){
		return companyRepository.getCompanyDetail(companyDomain);
	}
	
	
}