package com.loanscrefia.admin.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.repository.CompanyRepository;
import com.loanscrefia.util.UtilFile;

@Service
public class CompanyService {

	@Autowired private CompanyRepository companyRepository;
	@Autowired private UtilFile utilFile;
	
	//회원사 담당자 리스트 조회
	@Transactional(readOnly = true)
	public List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain){
		return companyRepository.selectCompanyList(companyDomain);
	}
	
	//회원사 당담자 상세 보기
	@Transactional(readOnly=true)
	public CompanyDomain getCompanyDetail(CompanyDomain companyDomain){
		return companyRepository.getCompanyDetail(companyDomain);
	}
	
	//회원사 담당자 승인 요청 
	@Transactional
	public int updateCompanyStat(CompanyDomain companyDomain){
		return companyRepository.updateCompanyStat(companyDomain);
	}
	
	//회원사 담당자 삭제 
	@Transactional
	public int deleteCompany(CompanyDomain companyDomain){
		
		int result 			= 0;
		int[] memberSeqArr 	= companyDomain.getMemberSeqArr();
		
		for(int i = 0;i < memberSeqArr.length;i++) {
			companyDomain.setMemberSeq(memberSeqArr[i]);
			result += companyRepository.deleteCompany(companyDomain);
		}
		
		return result;
	}
	
}