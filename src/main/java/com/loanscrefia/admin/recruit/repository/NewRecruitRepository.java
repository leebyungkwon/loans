package com.loanscrefia.admin.recruit.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.recruit.domain.NewRecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;

@Mapper
public interface NewRecruitRepository {
	
	//모집인 조회 및 변경 > 리스트
	List<NewRecruitDomain> selectNewRecruitList(NewRecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 상세
	NewRecruitDomain getNewRecruitDetail(NewRecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 법인 : 대표자 및 임원 상세
	List<RecruitImwonDomain> selectNewRecruitCorpImwonList(RecruitImwonDomain recruitImwonDomain);
	
	//모집인 조회 및 변경 > 법인 : 전문인력 상세
	List<RecruitExpertDomain> selectNewRecruitCorpExpertList(RecruitExpertDomain recruitExpertDomain);
	
	//모집인 조회 및 변경 > 법인 : 전산인력 상세
	List<RecruitItDomain> selectNewRecruitCorpItList(RecruitItDomain recruitItDomain);
	
	//모집인 조회 및 변경 > 승인처리
	int updateNewRecruitPlStat(NewRecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 모집인단계이력
	void insertNewMasterStep(NewRecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 이력상세
	NewRecruitDomain getNewRecruitHistDetail(NewRecruitDomain recruitDomain);
	
}
