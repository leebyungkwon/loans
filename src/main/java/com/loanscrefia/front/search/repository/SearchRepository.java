package com.loanscrefia.front.search.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.front.search.domain.SearchDomain;

@Mapper
public interface SearchRepository {

	//모집인 조회 : 개인(결제)
	SearchDomain selectPayIndvUserInfo(SearchDomain searchDoamin);
	
	//모집인 조회 : 개인(기등록여부 - 결제여부 체크 : 승인전)
	List<SearchDomain> selectPayResultIndvUserList(SearchDomain searchDoamin);
	
	//모집인 조회 : 개인(기등록여부 - 결제여부 체크 : 승인완료)
	SearchDomain selectPayResultIndvUserInfo(SearchDomain searchDoamin);
		
	//모집인 조회 : 법인(결제)
	SearchDomain selectPayCorpUserInfo(SearchDomain searchDoamin);
	
	//모집인 조회 : 법인(기등록여부 - 결제여부 체크 : 승인전)
	List<SearchDomain> selectPayCorpUserList(SearchDomain searchDoamin);
	
	//모집인 조회 : 법인(기등록여부 - 결제여부 체크 : 승인완료)
	SearchDomain selectPayResultCorpUserInfo(SearchDomain searchDoamin);	
	
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
	
	//모집인 정보 이력 저장
	int insertSearchUserHistory(SearchDomain searchDoamin);
	
	//위반이력 리스트
	List<SearchDomain> selectSearchUserViolationInfoList(SearchDomain searchDoamin);
	
	//기등록여부에 따른 상태값 변경
	int updatePayResultStat(SearchDomain searchDoamin);
	
	//모집인 상태 변경 : 승인완료 이전건에 대한 기등록여부 상태 변경
	int updatePreRegYn(SearchDomain searchDoamin);
	
	//2021-09-03 모집인 결제 조회 : 개인
	SearchDomain getPayResultIndvSearch(SearchDomain searchDomain);
	
	//2021-09-03 모집인 결제 조회 : 법인
	SearchDomain getPayResultCorpSearch(SearchDomain searchDomain);
	
	//2021-09-03 모집인 결제 조회 : 결과
	SearchDomain getPayResultSearchResult(SearchDomain searchDomain);
	
	
	//결제완료 후 승인상태가 승인완료 이전 리스트 - 개인
	List<SearchDomain> selectPrevIndvPaySearchResult(SearchDomain searchDoamin);
	
	//결제완료 후 승인상태가 승인완료 이전 리스트 - 법인
	List<SearchDomain> selectPrevCorpPaySearchResult(SearchDomain searchDoamin);
	
	//결제완료 후 승인상태가 승인완료인 리스트 - 개인
	List<SearchDomain> selectIndvPaySearchResult(SearchDomain searchDoamin);
	
	//결제완료 후 승인상태가 승인완료인 리스트 - 법인
	List<SearchDomain> selectCorpPaySearchResult(SearchDomain searchDoamin);
	
	
}
