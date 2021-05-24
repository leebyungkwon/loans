package com.loanscrefia.front.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/front")
public class MainController {

	//메인 페이지
	@GetMapping(value="/main")
	public String main() {
		return CosntPage.FoMainPage+"main";
	}
}
