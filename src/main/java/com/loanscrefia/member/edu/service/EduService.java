package com.loanscrefia.member.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.member.edu.domain.EduDomain;
import com.loanscrefia.member.edu.repository.EduRepository;

@Service
public class EduService {

	@Autowired private EduRepository eduRepo;
	
	//회원사 시스템 > 모집인 등록 : 엑셀 업로드 시 교육이수번호 체크
	@Transactional(readOnly=true)
	public int plEduNoCheck(EduDomain eduDomain) {
		return eduRepo.plEduNoCheck(eduDomain);
	}
}
