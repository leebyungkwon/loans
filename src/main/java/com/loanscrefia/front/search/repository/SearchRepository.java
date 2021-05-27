package com.loanscrefia.front.search.repository;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.front.search.domain.SearchDomain;

@Mapper
public interface SearchRepository {

	//모집인 조회 : 개인
	SearchDomain selectIndvUserInfo(SearchDomain searchDoamin);
		
	//모집인 조회 : 법인
	SearchDomain selectCorpUserInfo(SearchDomain searchDoamin);
	
	//모집인 조회
	SearchDomain selectSearchUserInfo(SearchDomain searchDoamin);
	
	//모집인 상태 변경 : 자격취득
	int updatePlRegStat(SearchDomain searchDoamin);
	
	//모집인 정보 단계별(STATUS) 이력 저장
	int insertSearchUserStepHistory(SearchDomain searchDoamin);
}
