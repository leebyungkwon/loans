package com.loanscrefia.system.code.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.domain.CodeMstDomain;

@Mapper 
public interface CodeRepository {
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드마스터
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//코드마스터 리스트
	List<CodeMstDomain> selectCodeMstList(CodeMstDomain codeMstDomain);
	
	//코드마스터 아이디 중복체크
	int codeMstCdDupCheck(CodeMstDomain codeMstDomain);
	
	//코드마스터 등록
	int insertCodeMst(CodeMstDomain codeMstDomain);
	
	//코드마스터 수정
	int updateCodeMst(CodeMstDomain codeMstDomain);
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드상세
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//코드상세 리스트
	List<CodeDtlDomain> selectCodeDtlList(CodeDtlDomain codeDtlDomain);
	
	//코드상세 아이디 중복체크
	int codeDtlCdDupCheck(CodeDtlDomain codeDtlDomain);
		
	//코드상세 등록
	int insertCodeDtl(CodeDtlDomain codeDtlDomain);
	
	//코드상세 수정
	int updateCodeDtl(CodeDtlDomain codeDtlDomain);
	
}