package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("tranInfo")
public class TransactionDomain extends BaseDomain {
	private int	userSeq;
	private String url;
	private String param;
	private String ip;
	private String ip2;
}