package com.loanscrefia.admin.recruit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.repository.RecruitRepository;

@Service
public class RecruitService {

	@Autowired private RecruitRepository recruitRepo;
	

	// 모집인 리스트 조회
	@Transactional(readOnly=true)
	public List<RecruitDomain> selectRecruitList(RecruitDomain recruitDomain){
		return recruitRepo.selectRecruitList(recruitDomain);
	}
	
	
	// 모집인 단건 조회
	@Transactional(readOnly=true)
	public RecruitDomain getRecruit(RecruitDomain recruitDomain){
		return recruitRepo.getRecruit(recruitDomain);
	}
	
}
