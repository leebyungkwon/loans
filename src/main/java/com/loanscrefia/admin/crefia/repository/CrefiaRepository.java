package com.loanscrefia.admin.crefia.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;

@Mapper
public interface CrefiaRepository {
	
	// 협회 관리자 관리 조회
	List<CrefiaDomain> selectCrefiaList(CrefiaDomain crefiaDomain);

	// 협회 관리자 관리 상세 조회
	CrefiaDomain crefiaDetail(CrefiaDomain crefiaDomain);
	
}