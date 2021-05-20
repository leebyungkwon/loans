package com.loanscrefia.admin.recruit.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;

@Mapper
public interface RecruitRepository {
	
	//모집인 조회 및 변경 > 리스트
	List<RecruitDomain> selectRecruitList(RecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 상세
	RecruitDomain getRecruitDetail(RecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 법인 : 대표자 및 임원 상세
	List<RecruitImwonDomain> selectRecruitCorpImwonList(RecruitImwonDomain recruitImwonDomain);
	
	//모집인 조회 및 변경 > 법인 : 전문인력 상세
	List<RecruitExpertDomain> selecRecruitCorpExpertList(RecruitExpertDomain recruitExpertDomain);
	
	//모집인 조회 및 변경 > 법인 : 전산인력 상세
	List<RecruitItDomain> selectRecruitCorpItList(RecruitItDomain recruitItDomain);
	
	//모집인 조회 및 변경 > 승인처리
	int updateRecruitPlStat(RecruitDomain recruitDomain);
	
	//모집인 조회 및 변경 > 모집인단계이력
	void insertMasterStep(RecruitDomain recruitDomain);
	
}
