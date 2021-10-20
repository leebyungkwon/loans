package com.loanscrefia.common.common.sms.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("sms")
public class SmsDomain extends BaseDomain{

	private String tranPhone;						// 수신번호(-제거)
	private String tranCallback;					// 발신번호(-제거)
	private String tranStatus;						// "1로" 고정
	private String tranDate;						// GETDATE();
	private String tranMsg;							// 문자내용 : 최대 80Byte
	private String tranEtc1;						// 인스턴스ID
	private String tranEtc3;						// 공백으로 보내기
	
}
