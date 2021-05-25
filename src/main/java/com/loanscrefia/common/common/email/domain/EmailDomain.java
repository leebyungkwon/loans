package com.loanscrefia.common.common.email.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("sendEmail")
public class EmailDomain extends BaseDomain{
	
	private String instId;					// 인스턴스ID
	private String name;					// 보내는 사람명
	private String email;					// 받는사람 이메일
	private String subsValue;				// 파라미터(파라미터 | 로 구분)
	
}
