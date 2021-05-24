package com.loanscrefia.front.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.repository.PayRepository;

@Service
public class PayService {

	@Autowired private PayRepository payRepo;
	
	//결제정보 저장
	@Transactional
	public int insertPayResult(PayDomain payDoamin) {
		return payRepo.insertPayResult(payDoamin);
	}
}
