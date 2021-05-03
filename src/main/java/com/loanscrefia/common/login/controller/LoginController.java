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
import com.loanscrefia.common.member.domain.MemberRoleDomain;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.code.domain.CodeMstDomain;
import com.loanscrefia.util.UtilFile;

import lombok.extern.java.Log;

@Log
@RestController
public class LoginController {

	@Autowired private LoginService loginService;
	@Autowired UtilFile utilFile;

	@GetMapping("/login")
	public ModelAndView dispLogin() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/login");
		return mv;
	}

	@PostMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/login");
		return mv;
	}
	
	@GetMapping("/prevLogin")
	public ModelAndView prevLogin() {
		
		// memberSeq 읽어와서 조회 후 던지기
		
		
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/prevLogin");
		return mv;
	}
	
	
	@GetMapping("/terms")
	public ModelAndView terms() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/terms");
		return mv;
	}

	@GetMapping("/signup")
	public ModelAndView dispSignup() {
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/signup");
		return mv;
	}

	/*
	@PostMapping("/signup")
	public ModelAndView execSignup(@RequestParam("files") MultipartFile[] files, SignupDomain signupDomain, MemberRoleDomain memberRoleDomain) {
		// 파일 업로드 추가
		Map<String, Object> ret = utilFile.setPath("signup") 
				.setFiles(files)
				.setExt("excel") 
				.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				signupDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}
		// 회원가입 데이터 저장
		signupDomain.setMemberSeq(loginService.saveUser(signupDomain).getMemberSeq());
		// Role 권한
		memberRoleDomain.setMemberSeq(signupDomain.getMemberSeq());
		loginService.saveRole(memberRoleDomain);
		ModelAndView mv = new ModelAndView(CosntPage.Common + "/login");
		return mv;
	}
	*/

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
    public ModelAndView dispDenied() {
    	ModelAndView mv = new ModelAndView(CosntPage.Error+"/denied");
        return mv;
    }
    
    @PostMapping("/dormantCheck")
    public boolean dormantCheck(SignupDomain signupDomain) {
    	return true;
    }

	// 아이디 중복체크
	@PostMapping("/idcheck")
	public int idCheck(SignupDomain signupDomain) {
		int count = 0;
		
		try {
			count = loginService.idCheck(signupDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	@PostMapping(value="/signupTest")
	public ResponseEntity<ResponseMsg> signupTest(@RequestParam("files") MultipartFile[] files, @Valid SignupDomain signupDomain){
		
		// 파일 업로드 추가
		Map<String, Object> ret = utilFile.setPath("signup") 
				.setFiles(files)
				.setExt("excel") 
				.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				signupDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}
		ResponseMsg responseMsg = loginService.saveUserTest(signupDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
