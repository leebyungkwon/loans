package com.loanscrefia.front.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/front")
public class PayController {
	
	//결제
	@GetMapping(value="/pay/payPage")
	public String payPage() {
		return CosntPage.FoPayPage+"/payTest";
	}
	
	//결제 인증정보 수신 페이지
	@PostMapping(value="/pay/allatReceive")
	public String allatReceive() {
		return CosntPage.FoPayPage+"/allat_receive";
	}
	
	//결제 승인요청 및 결과수신 페이지
	@PostMapping(value="/pay/allatApproval")
	public String allatApproval() {
		return CosntPage.FoPayPage+"/allat_approval";
	}
	
	//결제완료 페이지
	@GetMapping(value="/pay/payResult")
	public String payResult() {
		return CosntPage.FoPayPage+"/payResult";
	}
}