package com.loanscrefia.config.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.common.member.domain.MemberDomain;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired 
	private LoginService loginService;
	
	public LoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
    	Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
         
		/*
		 * SecurityMember mem = (SecurityMember) authentication.getPrincipal();
		 * MemberDomain member = new MemberDomain();
		 * member.setMemberNo(Long.parseLong(mem.getUsername())); MemberDomain result =
		 * loginService.getMember(member); if("Y".equals(result.getDormant())) {
		 * 
		 * }
		 */
        
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");
            if (redirectUrl != null) {
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}