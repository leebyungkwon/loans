package com.loanscrefia.system.code.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.system.code.domain.CodeDomain;

@Mapper 
public interface CodeRepository {
	
	//코드 마스터 리스트
	List<CodeDomain> selectCodeMstList(CodeDomain codeDomain);
	
	//코드 마스터 아이디 중복체크
	int codeMstCdDupCheck(CodeDomain codeDomain);
	
	//코드 마스터 등록
	int insertCodeMst(CodeDomain codeDomain);
	
	//코드 마스터 상세
	
	//코드 마스터 수정
	int updateCodeMst(CodeDomain codeDomain);
	
	//코드 상세 리스트
	List<CodeDomain> selectCodeDtlList(CodeDomain codeDomain);
	
	//코드 상세 아이디 중복체크
	int codeDtlCdDupCheck(CodeDomain codeDomain);
		
	//코드 상세 등록
	int insertCodeDtl(CodeDomain codeDomain);
	
	//코드 상세
	
	//코드 상세 수정
	int updateCodeDtl(CodeDomain codeDomain);
	
}