package com.loanscrefia.common.common.email.repository;


import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.common.common.email.domain.EmailDomain;

@Mapper
public interface EmailRepository {
	
	// 이메일 전송
	int sendEmail(EmailDomain emailDomain);
	
}
