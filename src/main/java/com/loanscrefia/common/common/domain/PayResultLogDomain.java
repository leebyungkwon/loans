package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("paylog")
public class PayResultLogDomain extends BaseDomain {

	private String url;				
	private String resData;			
	private String fullUrl;			
}
