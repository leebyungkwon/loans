package com.loanscrefia.admin.apply.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.member.user.domain.NewUserDomain;

@Mapper
public interface NewApplyRepository {
	
	
	// 2021-10-13 모집인 등록 승인처리 리스트
	List<NewApplyDomain> selectNewApplyList(NewApplyDomain newApplyDomain);
	
	//모집인 조회 및 변경 > 상세
	NewApplyDomain getNewApplyDetail(NewApplyDomain newApplyDomain);
	
	//모집인 조회 및 변경 > 법인 : 대표자 및 임원 상세
	List<ApplyImwonDomain> selectNewApplyCorpImwonList(ApplyImwonDomain applyImwonDomain);
	
	//모집인 조회 및 변경 > 법인 : 전문인력 상세
	List<ApplyExpertDomain> selectNewApplyCorpExpertList(ApplyExpertDomain applyExpertDomain);
	
	//모집인 조회 및 변경 > 법인 : 전산인력 상세
	List<ApplyItDomain> selectNewApplyCorpItList(ApplyItDomain applyItDomain);
	
	//모집인 조회 및 변경 > 승인처리
	int updateNewApplyPlStat(NewApplyDomain newApplyDomain);
	
	//모집인 조회 및 변경 > 모집인단계이력
	void insertNewMasterStep(NewApplyDomain newApplyDomain);

	//모집인 조회 및 변경 > 첨부서류체크리스트
	List<ApplyCheckDomain> selectNewApplyCheckList(ApplyCheckDomain applyCheckDomain);
	
	//모집인 조회 및 변경 > 첨부서류체크 등록
	int insertNewApplyCheck(ApplyCheckDomain applyCheckDomain);
	
	//모집인 조회 및 변경 > 첨부서류체크 삭제	
	int deleteNewApplyCheck(ApplyCheckDomain applyCheckDomain);
	
	//모집인 조회 및 변경 > 실무자확인
	int applyNewcheck(NewApplyDomain newApplyDomain);
	
	//모집인 조회 및 변경 > 관리자확인
	int applyNewAdmincheck(NewApplyDomain newApplyDomain);
	
	//모집인 조회 및 변경 > 승인일홀딩
	int newAppDateHold(NewApplyDomain newApplyDomain);	
	
	//모집인 조회 > 법인 : 대표 및 임원 상세(OCR)
	ApplyImwonDomain getNewApplyImwonDetail(ApplyImwonDomain applyImwonDomain);
	
	//기등록여부체크리스트
	List<NewApplyDomain> selectNewPrevRegCheckList(NewApplyDomain newApplyDomain);
	
	//법인 승인여부 체크
	int applyNewCorpStatCheck(NewUserDomain userDomain);
	
	//모집인 승인처리 > 일괄 보완요청
	int updateNewApplyImprovePlStat(NewApplyDomain newApplyDomain);
	
	//모집인 정보 단계별(STATUS) 이력 저장
	int insertUserStepHistory(SearchDomain searchDomain);
}
