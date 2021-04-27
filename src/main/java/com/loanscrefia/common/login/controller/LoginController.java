package com.loanscrefia.common.login.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilFile;

import lombok.extern.java.Log;

@Log
@RestController
public class LoginController {
	
	@Autowired private LoginService loginService;
	@Autowired UtilFile utilFile;

    @GetMapping("/login")
    public ModelAndView dispLogin() {
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/login");
        return mv;
    }
    
    @PostMapping("/login")
    public ModelAndView testLogin() {
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/login");
        return mv;
    }

    @GetMapping("/signup")
    public ModelAndView dispSignup() {
    	log.info("여기에는 타니?");
        ModelAndView mv = new ModelAndView(CosntPage.Common+"/signup");
        return mv;
    }

    @PostMapping("/signup")
    public ModelAndView execSignup(@RequestParam("files") MultipartFile[] files, MemberDomain memberDomain) {
    	// 1. 첨부파일을 저장한다
    	// 2. 저장한 첨부파일에 ID(SEQ)를 빼온다
    	// 3. 빼온 ID로 화면에서 전달받은 도메인에 set해준다
    	// 4. 화면에서 전달받은 도메인 + 첨부파일ID를 활용해 insert한다 
    	
		// 파일 업로드 추가중입니다.
		Map<String, Object> ret = utilFile.setPath("test") //추후 수정 예정
				.setFiles(files)
				.setExt("image") //추후 수정 예정
				.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				memberDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}
		loginService.saveUser(memberDomain);
		// 화면에서 전달받은 도메인 + 첨부파일ID를 활용해 insert한다
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
    public ModelAndView dispDenied() {
    	ModelAndView mv = new ModelAndView(CosntPage.Error+"/denied");
        return mv;
    }
    
    @PostMapping("/dormantCheck")
    public boolean dormantCheck(MemberDomain memberDomain) {
    	return true;
    }

}
