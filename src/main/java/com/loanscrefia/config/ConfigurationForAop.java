package com.loanscrefia.config;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.member.domain.MemberDomain;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Aspect
public class ConfigurationForAop {

	@Pointcut("execution(* com.loanscrefia.*.*.repository.*Repository.*(..))")
	public void setBaseData(){ }

    /**
     * @param joinPoint
     * @throws Throwable
     */
    @Before("setBaseData()")
    public BaseDomain before(JoinPoint pjp) throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		BaseDomain domain = new BaseDomain();
		if(authentication != null) {
			if(!"anonymousUser".equals(authentication.getPrincipal())) {
				SecurityMember mem = (SecurityMember) authentication.getPrincipal();
				int seq = Integer.parseInt(mem.getUsername());
				Object [] p = pjp.getArgs();
				if(p[0]==null || p[0]=="") return null;
				domain = (BaseDomain)p[0];
				domain.setRegSeq(seq);
				domain.setUpdSeq(seq);	
			}
		}
		return domain;
    }
}
