package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("payResult")
public class PayResultDomain extends BaseDomain {

	//결제정보(tb_lc_pay)
	private int paySeq;				//결제시퀀스
	private String orderNo;			//주문번호
	private int masterSeq;			//접수번호시퀀스
	private String payType;			//지불수단
	private String seqNo;			//거래일련번호
	private String approvalNo;		//승인번호
	private String id;				//카드,은행ID
	private String name;			//카드,은행명
	private String sellMm;			//할부개월
	private int amt;				//금액
}
