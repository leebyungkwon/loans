package com.loanscrefia.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loanscrefia.common.common.service.TransactionService;
import com.loanscrefia.common.member.domain.MemberDomain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConfigurationForInterceptor extends HandlerInterceptorAdapter {

	@Autowired private TransactionService tran;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//서버에 접속되는 정보를 저장한다
		tran.logSave(request);
		
		String url 			= request.getRequestURI();
		//세션 체크
		boolean isContinue	= false;
		
		if(url.equals("/") || url.indexOf("/front/") >= 0 ||
				url.indexOf("/login") >= 0 ||
				url.indexOf("/terms") >= 0 ||
				url.indexOf("/signup") >= 0 ||
				url.indexOf("/denied") >= 0 ||
				url.indexOf("/common/") >= 0 ||
				url.indexOf("/idcheck") >= 0) {
			//System.out.println(":::::: isContinue = true ::::::");
			isContinue = true;
		}

		if(!isContinue) {
			HttpSession session 	= request.getSession();
			MemberDomain loginInfo 	= (MemberDomain)session.getAttribute("member");
			
			if(loginInfo == null) {
				response.sendRedirect("/logout");
				return false;
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object obj, ModelAndView mav)	throws Exception {
	}
	
}