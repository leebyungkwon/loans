package com.loanscrefia.common.login.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.common.service.EnterService;
import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LoginController {

	@Autowired private LoginService loginService;
	@Autowired private EnterService enter;
	
	// 로그인 페이지
	@GetMapping("/login")
	public ModelAndView dispLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		MemberDomain memberDomain = (MemberDomain) session.getAttribute("member");
		ModelAndView mv = new ModelAndView();
		if(memberDomain != null) {
			mv.setViewName("redirect:/main");
		}else {
			if(!enter.isVaildIp(request)) 		{
				mv.setViewName(CosntPage.Error + "/noIp");
			}
			else mv.setViewName(CosntPage.Common + "/login");
		}
		return mv;
	}

	// 로그인 - security 확인
	@PostMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/login");
		return mv;
	}
	
	// 로그인 성공
    @GetMapping("/login/result")
    public ModelAndView dispLoginResult() {
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/loginSuccess");
        return mv;
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public String logout() {
    	return "redirect:/login";
    }
    
    // 로그아웃 성공
    @GetMapping("/logout/result")
    public String dispLogout() {
        return "/logout";
    }

    // 권한 미충분 페이지
    @GetMapping("/denied")
    public ModelAndView dispDenied() {
    	ModelAndView mv = new ModelAndView(CosntPage.Error+"/denied");
        return mv;
    }
    @GetMapping("/noIp")
    public ModelAndView noIp() {
    	ModelAndView mv = new ModelAndView(CosntPage.Error+"/noIp");
        return mv;
    }
	
	// 약관동의 페이지
	@GetMapping("/terms")
	public ModelAndView terms() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/terms");
		return mv;
	}
	
	// 회원가입 페이지
	@GetMapping("/signup")
	public ModelAndView dispSignup(SignupDomain signupDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/signup");
		mv.addObject("termsData", signupDomain);
		return mv;
	}

	// 아이디 중복체크
	@PostMapping("/idcheck")
	public ResponseEntity<ResponseMsg> idCheck(@Valid SignupDomain signupDomain, BindingResult bindingResult) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		
		int count = 0;
		try {
			count = loginService.idCheck(signupDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseMsg = new ResponseMsg(HttpStatus.OK, null, count, "success");
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 회원가입
	@PostMapping(value="/signup")
	public ResponseEntity<ResponseMsg> signup(@RequestParam("files") MultipartFile[] files, @Valid SignupDomain signupDomain, BindingResult bindingResult){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null, 0, "");		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		responseMsg = loginService.insertSignup(files, signupDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
