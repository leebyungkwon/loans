package com.loanscrefia.admin.edu.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("edu")
public class EduDomain extends BaseDomain {

	//교육이수정보(tb_lc_edu01)
	private String plMName;			//모집인이름
	private String plMBirth;		//모집인생년월일
	private String plMGender;		//모집인성별
	private String careerTyp;		//구분							-> [CAR001]신규,경력
	private String plProduct;		//금융상품유형(취급상품)				-> [PRD001]대출,시설대여/할부/유사상품,기타 대출성 상품
	private String plEduNo;			//교육이수번호
	private String plCrfNo;			//인증서번호
	private String plEduDate;		//수료일자
}
