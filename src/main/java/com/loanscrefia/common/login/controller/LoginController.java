package com.loanscrefia.common.login.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilFile;

@RestController
public class LoginController {

	@Autowired private LoginService loginService;
	@Autowired UtilFile utilFile;

	// 로그인 페이지
	@GetMapping("/login")
	public ModelAndView dispLogin() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/login");
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
	public int idCheck(@Valid SignupDomain signupDomain) {
		int count = 0;
		try {
			count = loginService.idCheck(signupDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// 회원가입
	@PostMapping(value="/signup")
	public ResponseEntity<ResponseMsg> signup(@RequestParam("files") MultipartFile[] files, @Valid SignupDomain signupDomain){
		Map<String, Object> ret = utilFile.setPath("signup") 
				.setFiles(files)
				.setExt("all") 
				.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				signupDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}
		ResponseMsg responseMsg = loginService.insertSignup(signupDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
