package com.loanscrefia.admin.recruit.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;

@Mapper 
public interface RecruitRepository {
	
	// 모집인 조회
	List<RecruitDomain> selectRecruitList(RecruitDomain recruitDomain);
	
}