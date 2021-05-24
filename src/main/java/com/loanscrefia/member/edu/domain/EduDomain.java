package com.loanscrefia.member.edu.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("edu")
public class EduDomain extends BaseDomain {

	//교육이수정보(tb_lc_edu01)
	private String plMName;		//모집인이름
	private String plMZId;		//모집인주민등록번호
	private String careerTyp;	//구분				-> [CAR001]신규,경력
	private String plProduct;	//취급상품				-> [PRD001]대출,시설대여 및 연불판매,할부,어음할인,매출채권매입,지급보증,기타 대출성 상품
	private String plEduNo;		//교육이수번호			-> 신규일 때
	private String plCrfNo;		//인증서번호			-> 경력일 때
	private String plEduDate;	//수료일자
}
