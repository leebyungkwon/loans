package com.loanscrefia.common.common.email.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.SendEmailDomain;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Mapper
public interface EmailRepository {
	
	// 이메일 전송
	int sendEmail(SendEmailDomain sendEmailDomain);
	
}
