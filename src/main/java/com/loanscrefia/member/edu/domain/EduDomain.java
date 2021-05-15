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
	private String plClass;		//구분				-> [CAR001]신규,경력
	private String plProduct;	//취급상품 교육분류		-> []대출,리스
	private String plEduNo;		//수료번호
	private String plEduDate;	//수료일자
}
