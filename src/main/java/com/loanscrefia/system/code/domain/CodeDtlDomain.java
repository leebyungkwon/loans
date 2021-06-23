package com.loanscrefia.system.code.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("codeDtl")
public class CodeDtlDomain extends BaseDomain {

	//코드상세(tb_lc_cod01_detail)
	private String codeMstCd;
	private String codeDtlCd;
	private String codeDtlNm;
	private String codeDtlDesc;
	private String property01;
	private String property02;
	private String property03;
	private String property04;
	private String property05;
	
	private String saveType;
	
	private String creYn;
	private String creGrp;
}
