package com.loanscrefia.common.login.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;
import com.loanscrefia.config.string.CosntPage;

import lombok.extern.java.Log;

@Log
@RestController
public class LoginController {
	
	@Autowired private LoginService loginService;

    @GetMapping("/login")
    public ModelAndView dispLogin() {
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/login");
        return mv;
    }

    @GetMapping("/signup")
    public ModelAndView dispSignup() {
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/signup");
        return mv;
    }

    @PostMapping("/signup")
    public ModelAndView execSignup(MemberDomain mem) {
    	System.out.println("####### mem :: " + mem);
        long memberNo = loginService.saveUser(mem);
        MemberRoleDomain role = new MemberRoleDomain();
        role.setMemberNo(memberNo);
        loginService.saveRole(role);
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/login");
        return mv;
    }

    @GetMapping("/login/result")
    public ModelAndView dispLoginResult() {
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/loginSuccess");
        return mv;
    }

    @PostMapping("/logout")
    public String logout() {
    	return "redirect:/login";
    }
    
    @GetMapping("/logout/result")
    public String dispLogout() {
        return "/logout";
    }

    @GetMapping("/denied")
    public ModelAndView dispDenied( ) {
    	ModelAndView mv = new ModelAndView(CosntPage.Error+"/denied");
        return mv;
    }

}
