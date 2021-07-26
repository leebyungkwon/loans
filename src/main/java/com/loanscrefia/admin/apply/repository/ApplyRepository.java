package com.loanscrefia.admin.apply.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;

@Mapper
public interface ApplyRepository {
	
	//모집인 조회 및 변경 > 리스트
	List<ApplyDomain> selectApplyList(ApplyDomain applyDomain);
	
	//모집인 조회 및 변경 > 상세
	ApplyDomain getApplyDetail(ApplyDomain applyDomain);
	
	//모집인 조회 및 변경 > 법인 : 대표자 및 임원 상세
	List<ApplyImwonDomain> selectApplyCorpImwonList(ApplyImwonDomain applyImwonDomain);
	
	//모집인 조회 및 변경 > 법인 : 전문인력 상세
	List<ApplyExpertDomain> selecApplyCorpExpertList(ApplyExpertDomain applyExpertDomain);
	
	//모집인 조회 및 변경 > 법인 : 전산인력 상세
	List<ApplyItDomain> selectApplyCorpItList(ApplyItDomain applyItDomain);
	
	//모집인 조회 및 변경 > 승인처리
	int updateApplyPlStat(ApplyDomain applyDomain);
	
	//모집인 조회 및 변경 > 모집인단계이력
	void insertMasterStep(ApplyDomain applyDomain);

	//모집인 조회 및 변경 > 첨부서류체크리스트
	List<ApplyCheckDomain> selectApplyCheckList(ApplyCheckDomain applyCheckDomain);
	
	//모집인 조회 및 변경 > 첨부서류체크 등록
	int insertApplyCheck(ApplyCheckDomain applyCheckDomain);
	
	//모집인 조회 및 변경 > 첨부서류체크 삭제	
	int deleteApplyCheck(ApplyCheckDomain applyCheckDomain);
	
	//모집인 조회 및 변경 > 실무자확인
	int applycheck(ApplyDomain applyDomain);
	
	//모집인 조회 및 변경 > 관리자확인
	int applyAdmincheck(ApplyDomain applyDomain);
	
	//모집인 조회 > 법인 : 대표 및 임원 상세(OCR)
	ApplyImwonDomain getApplyImwonDetail(ApplyImwonDomain applyImwonDomain);
	
	//기등록여부체크리스트
	List<ApplyDomain> selectPrevRegCheckList(ApplyDomain applyDomain);
	
	
}
