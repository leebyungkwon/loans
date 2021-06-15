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

	//법인등록번호 중복체크
	int plMerchantNoCheck(CorpDomain corpDomain);
	
	//법인 정보 존재여부 체크
	int selectCorpInfoCnt(CorpDomain corpDomain);
	
	//법인등록번호 사용여부 체크
	int plMerchantNoSearchCheck(CorpDomain corpDomain);
	
	//법인 삭제
	int deleteCorpInfo(CorpDomain corpDomain);
}
