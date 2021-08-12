package com.loanscrefia.config.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.repository.MemberRepository;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	
	@Autowired private CommonRepository commonRepository;
	
	public LoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
    	Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String redirectUrl = "";
        SecurityMember mem = (SecurityMember) authentication.getPrincipal();
        
        MemberDomain memberDomain = new MemberDomain();
        memberDomain.setMemberSeq(Long.parseLong(mem.getUsername()));
        MemberDomain result = commonRepository.getMemberDetail(memberDomain);
        session.setAttribute("member", result);
        
        if (session != null) {
            Object[] bb = mem.getAuthorities().toArray();
            String resultRole = bb[0].toString();
            if("ROLE_MEMBER".equals(resultRole)) {
            	redirectUrl = "/common/board/noticePage";
            }else {
            	redirectUrl = "/main";
            }
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