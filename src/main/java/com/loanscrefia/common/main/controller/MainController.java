package com.loanscrefia.common.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.main.domain.MainDomain;
import com.loanscrefia.common.main.service.MainService;
import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/main")
public class MainController {
	
	@Autowired 
	private MainService mainService;

	@GetMapping(value="")
	public ModelAndView main(MainDomain mainDomain) {
		ModelAndView mv 	= new ModelAndView(CosntPage.BoMainPage+"main");
		MainDomain result 	= mainService.selectDashBoard(mainDomain);
		mv.addObject("result", result);
		return mv;
	}
}
