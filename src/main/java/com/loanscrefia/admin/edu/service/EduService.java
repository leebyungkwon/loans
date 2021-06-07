package com.loanscrefia.admin.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.edu.domain.EduDomain;
import com.loanscrefia.admin.edu.repository.EduRepository;

@Service
public class EduService {

	@Autowired private EduRepository eduRepo;
	
}
