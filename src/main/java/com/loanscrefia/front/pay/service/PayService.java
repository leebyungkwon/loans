package com.loanscrefia.front.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loanscrefia.front.pay.repository.PayRepository;

@Service
public class PayService {

	@Autowired private PayRepository payRepo;
	
	
}
