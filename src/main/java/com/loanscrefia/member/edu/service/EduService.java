package com.loanscrefia.member.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loanscrefia.member.edu.repository.EduRepository;

@Service
public class EduService {

	@Autowired private EduRepository eduRepo;
	
}
