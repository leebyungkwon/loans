package com.loanscrefia.admin.company.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.company.domain.CompanyDomain;

@Mapper
public interface CompanyRepository {
	
	//협회 - 회원사 담당자 리스트 페이지
	List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain);
	
	//회원사 당담자 상세 리스트 조회
	CompanyDomain getCompanyDetail(CompanyDomain companyDomain);
	
	//회원사 당담자 승인요청
	int updateCompanyStat(CompanyDomain companyDomain);
	
	//회원사 당담자 삭제
	int deleteCompany(CompanyDomain companyDomain);

	
	// 비밀번호 초기화
	int cleanPassword(CompanyDomain companyDomain);
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 회원사 관리
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트
	List<CompanyDomain> selectCompanyCodeList(CompanyDomain companyDomain);
	
	//회원사코드 중복체크
	int comCodeDupChk(CompanyDomain companyDomain);
	
	//등록
	int saveCompanyCode(CompanyDomain companyDomain);
	
	
	// 2022-01-04 보안취약점에 따른 로그인 잠김 해제 기능 추가
	int updLoginFail(CompanyDomain companyDomain);
	
}