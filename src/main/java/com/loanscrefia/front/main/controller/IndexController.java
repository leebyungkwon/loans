package com.loanscrefia.front.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/front")
public class IndexController {

	//메인페이지
	@GetMapping(value="/index")
	public String main() {
		return CosntPage.FoMainPage+"main";
	}
	
	//대출모집인 제도
	@GetMapping(value="/info")
	public String info() {
		return CosntPage.FoInfoPage+"/info";
	}
	
	//관련규정
	@GetMapping(value="/rules")
	public String rules() {
		return CosntPage.FoInfoPage+"/rules";
	}
	
	//FAQ
	@GetMapping(value="/faq")
	public String faq() {
		return CosntPage.FoInfoPage+"/faq";
	}
	
	
	//이용약관
	@GetMapping(value="/terms")
	public String terms() {
		return CosntPage.FoInfoPage+"/terms";
	}
	
	
	//개인정보처리방침
	@GetMapping(value="/privacy")
	public String privacy() {
		return CosntPage.FoInfoPage+"/privacy";
	}
	
	
}
