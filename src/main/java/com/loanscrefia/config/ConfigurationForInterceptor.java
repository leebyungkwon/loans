package com.loanscrefia.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.string.CosntPage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConfigurationForInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String sid = request.getHeader("site");
		String secretKey = request.getHeader("SecretKey");
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		

		
		String ips = request.getHeader("X-Forwarded-For");
		 

		//if("/".equals(url) || "/error".equals(url)) return false;

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