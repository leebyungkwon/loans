package com.loanscrefia.system.code.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("code")
public class CodeDomain extends BaseDomain{
	
	//코드 마스터
	private String codeMstCd;
	private String codeMstNm;
	private String codeMstDesc;
	
	//코드 상세
	private String codeDtlCd;
	private String codeDtlNm;
	private String codeDtlDesc;
	
	//공통
	private String property01;
	private String property02;
	private String property03;
	private String property04;
	private String property05;
	
	//그외
	private String saveType;
	
}
