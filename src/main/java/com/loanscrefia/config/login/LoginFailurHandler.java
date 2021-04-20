package com.loanscrefia.config.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailurHandler implements AuthenticationFailureHandler {    
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException arg2)
            throws IOException, ServletException {
		
		req.setAttribute("failMsg", "로그인에 실패했다.");
        //req.setAttribute("email", req.getParameter("email"));
        //req.getRequestDispatcher("/login_view?error=true").forward(req, res);
		//RequestDispatcher dispatcher = req.getRequestDispatcher("/login");
		//dispatcher.forward(req, res);
		
		//res.sendRedirect("/login");

	}
 
}
