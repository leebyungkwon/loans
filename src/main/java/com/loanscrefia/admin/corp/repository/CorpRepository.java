package com.loanscrefia.admin.corp.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.member.user.domain.UserDomain;

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
	
	//법인 금융감독원 승인여부 체크
	int corpPassCheck(UserDomain userDomain);
	
	
	
	
	//법인등록 임시 리스트
	List<CorpDomain> selectCheckCorpList(CorpDomain corpDomain);
	
}
