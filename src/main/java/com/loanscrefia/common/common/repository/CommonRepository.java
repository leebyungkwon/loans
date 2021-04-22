package com.loanscrefia.common.common.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Mapper
public interface CommonRepository {
	void insertFile(FileDomain attach);
	
	// 공통코드 조회 selectBox
	List<CodeDtlDomain> selectCommonCodeList(CodeDtlDomain codeDtlDomain);
}
