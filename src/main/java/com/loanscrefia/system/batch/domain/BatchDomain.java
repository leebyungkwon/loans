package com.loanscrefia.system.batch.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("batch")
public class BatchDomain extends BaseDomain{

	private String scheduleName;//스케줄명
	private int reqCnt;			//요청건수
	private int successCnt;		//성공건수
	private String param;		//json 형식

	private String startTime;	//실행시간
	private String endTime;		//종료시간
	
	private String status;
	private String error;
	
	private String property01;
	private String property02;
	private String property03;
	private String property04;
	private String property05;
	
}
