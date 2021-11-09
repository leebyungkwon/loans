package com.loanscrefia.system.batch.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("batch")
public class BatchDomain extends BaseDomain{

	private String scheduleName;
	private int reqCnt;
	private int successCnt;
	private String param;		//json 형식
	private String property01;
	private String property02;
	private String property03;
	private String property04;
	private String property05;
	private String startTime;
	private String endTime;
	
}
