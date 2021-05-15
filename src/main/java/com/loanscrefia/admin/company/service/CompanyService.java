package com.loanscrefia.admin.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.repository.CompanyRepository;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class CompanyService {

	@Autowired private CompanyRepository companyRepository;
	
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

	//회원사 관리 리스트 조회
	@Transactional(readOnly = true)
	public List<CompanyDomain> selectCompanyCodeList(CompanyDomain companyDomain){
		return companyRepository.selectCompanyCodeList(companyDomain);
	}
	
	//회원사 관리 상세 조회
	@Transactional(readOnly=true)
	public CompanyDomain getCompanyCodeDetail(CompanyDomain companyDomain){
		return companyRepository.getCompanyCodeDetail(companyDomain);
	}

	// 법인등록번호 중복체크
	public int plMerchantNoCheck(CompanyDomain companyDomain) {
		return companyRepository.plMerchantNoCheck(companyDomain);
	}

	// 회원사 관리 -> Insert (등록)
	public ResponseMsg saveCompanyCodeDetail(CompanyDomain companyDomain) {
		companyRepository.saveCompanyCodeDetail(companyDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "정보가 등록 되었습니다.");
	}
	
	// 회원사 관리 -> Update (수정)
	public ResponseMsg updCompanyCodeDetail(CompanyDomain companyDomain) {
		companyRepository.updCompanyCodeDetail(companyDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "정보가 수정 되었습니다.");
	}
	
	// 회원사 관리 -> Delete (글 삭제)
	public ResponseMsg delCompanyCodeDetail(CompanyDomain companyDomain) {
		companyRepository.delCompanyCodeDetail(companyDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "정보가 삭제 되었습니다.");
	}
	
	
}