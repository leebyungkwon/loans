package com.loanscrefia.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.login.domain.SecurityMember;

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
				
				if(p[0].getClass().isInstance(BaseDomain.class)) {
					domain = (BaseDomain)p[0];
					domain.setRegSeq(seq);
					domain.setUpdSeq(seq);
				}
			}
		}
		return domain;
    }
}
