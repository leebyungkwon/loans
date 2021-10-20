package com.loanscrefia.common.common.sms.repository;


import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.sms.domain.SmsDomain;

@Mapper
public interface SmsRepository {
	
	// 이메일 전송
	int sendSms(SmsDomain smsDomain);
	
}
