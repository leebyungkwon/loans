package com.loanscrefia.front.pay.repository;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.front.pay.domain.PayDomain;

@Mapper
public interface PayRepository {

	//결제정보 저장
	int insertPayResult(PayDomain payDomain);
}
