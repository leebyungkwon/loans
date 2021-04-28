package com.loanscrefia.common.main.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.string.CosntPage;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping(value="/main")
public class MainController {
	
	@Autowired private CommonService commonService;
	
	@GetMapping(value="")
	public String main(HttpSession session) {
		MemberDomain mem = (MemberDomain) session.getAttribute("member");
		
		System.out.println("####" + mem.getMemberSeq());
		
		return CosntPage.BoMainPage+"main";
	}
	/*
	 * @GetMapping(value="/getVer") public String getVer(Long verId) { VersionEntity
	 * versionDomain = new VersionEntity(); versionDomain.setVerId(verId);
	 * 
	 * commonService.getVer(versionDomain); return CosntPage.BoMainPage+"main"; }
	 */
}
