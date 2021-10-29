package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("productDtl")
public class ProductDtlDomain extends BaseDomain {

	private int masterSeq;
	private String plProductDtlCd;
	private String plProductDtlCdNm;
	private String useYn;
}
