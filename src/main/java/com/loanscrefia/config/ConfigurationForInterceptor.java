package com.loanscrefia.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loanscrefia.common.member.domain.MemberDomain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConfigurationForInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String sid 			= request.getHeader("site");
		String secretKey 	= request.getHeader("SecretKey");
		String ip 			= request.getRemoteAddr();
		String url 			= request.getRequestURI();
		String ips 			= request.getHeader("X-Forwarded-For");
		
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
		
		//파라미터
		Enumeration<String> paramNames = request.getParameterNames();
		String p = "?";

		log.info("============================== START ==============================");
		log.info(" Class       \t:  "	+ handler.getClass());
		log.info(" Request URI \t:  "	+ url);
		log.info(" ip \t:  " 			+ ip);
		
		while (paramNames.hasMoreElements()) {
			String key = (String) paramNames.nextElement();  
			String value = request.getParameter(key);

			value = value.trim();
	    	
    		if(!"?".equals(p)) p = p + "&";
    		
    		p = p + key + "="+value;

		}
		
		return super.preHandle(request, response, handler);
	}
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object obj, ModelAndView mav)	throws Exception {
	}
	
}