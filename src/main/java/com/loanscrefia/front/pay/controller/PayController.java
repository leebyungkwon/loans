package com.loanscrefia.front.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Controller
@RequestMapping(value="/front")
public class PayController {
	
	@Autowired private SearchService searchService;
	
	//모집인 결제 > 모집인 조회 페이지
	@GetMapping(value="/pay/payUserSearchPage")
	public String payUserSearchPage() {
		return CosntPage.FoPayPage+"/payUserSearch";
	}
	
	//모집인 결제 > 모집인 조회 결과
	@GetMapping(value="/pay/payUserSearchResult")
	public ModelAndView selectIndvUserInfo(SearchDomain searchDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.FoPayPage+"/payUserSearchResult");
		return mv;
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
	public String payResult(SearchDomain searchDomain) {
		System.out.println("PayController > payResult() :::::::: "+searchDomain.getMasterSeq());
		return CosntPage.FoPayPage+"/payResult";
	}
}