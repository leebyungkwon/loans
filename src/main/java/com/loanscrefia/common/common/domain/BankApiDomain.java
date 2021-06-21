package com.loanscrefia.common.common.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("bankapi")
public class BankApiDomain extends BaseDomain{

	private String test;
}
