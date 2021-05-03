package com.loanscrefia.config.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.common.member.domain.MemberDomain;

public class LoginFailurHandler implements AuthenticationFailureHandler {    
	
	
	@Autowired 
	private LoginService loginService;
	
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
            throws IOException, ServletException {
		
		if (exception instanceof AuthenticationServiceException) {
			req.setAttribute("loginFailMsg", "존재하지 않는 사용자입니다.");
			
		} else if(exception instanceof BadCredentialsException) {
			String memberId = req.getParameter("memberId").toString();
			MemberDomain memberDomain = new MemberDomain();
			memberDomain.setMemberId(memberId);
			// 로그인 실패횟수 증가
			loginService.loginFailCnt(memberDomain);
			req.setAttribute("loginFailMsg", "아이디 또는 비밀번호가 틀립니다.");
		} else if(exception instanceof LockedException) {
			req.setAttribute("loginFailMsg", "잠긴 계정입니다..");
			
		} else if(exception instanceof DisabledException) {
			req.setAttribute("loginFailMsg", "승인이 필요한 계정입니다.");
			
		}  else if(exception instanceof CredentialsExpiredException) {
			req.setAttribute("loginFailMsg", "비밀번호가 만료되었습니다.");
			
		}  else if(exception instanceof AuthenticationException) {
			req.setAttribute("loginFailMsg", "휴면(만료) 계정입니다.");
		}  
		
        //req.setAttribute("email", req.getParameter("email"));
        //req.getRequestDispatcher("/login_view?error=true").forward(req, res);
		RequestDispatcher dispatcher = req.getRequestDispatcher("/login");
		dispatcher.forward(req, res);
		
		//res.sendRedirect("/login");

	}
 
}
