package com.loanscrefia.admin.apply.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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
	
}
