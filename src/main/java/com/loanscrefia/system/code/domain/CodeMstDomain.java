package com.loanscrefia.system.code.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("codeMst")
public class CodeMstDomain extends BaseDomain {

	//코드마스터(tb_lc_cod01)
	private String codeMstCd;
	private String codeMstNm;
	private String codeMstDesc;
	private String property01;
	private String property02;
	private String property03;
	private String property04;
	private String property05;
	
	private String saveType;
}
