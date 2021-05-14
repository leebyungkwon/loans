package com.loanscrefia.admin.corp.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.corp.domain.CorpDomain;

@Mapper
public interface CorpRepository {

	//법인 리스트
	List<CorpDomain> selectCorpList(CorpDomain corpDomain);
	
	//법인 등록
	int insertCorpInfo(CorpDomain corpDomain);
	
	//법인 상세
	CorpDomain getCorpInfo(CorpDomain corpDomain);
	
	//법인 수정
	int updateCorpInfo(CorpDomain corpDomain);
	
}
