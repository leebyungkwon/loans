package com.loanscrefia.admin.edu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.edu.domain.EduDomain;
import com.loanscrefia.admin.edu.repository.EduRepository;

import sinsiway.CryptoUtil;

@Service
public class EduService {

	@Autowired private EduRepository eduRepository;
	
	// 교육이수번호 리스트
	@Transactional(readOnly=true)
	public List<EduDomain> selectEduList(EduDomain eduDomain) {
		return eduRepository.selectEduList(eduDomain);
	}
		
}
