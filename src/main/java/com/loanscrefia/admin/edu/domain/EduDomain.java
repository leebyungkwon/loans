package com.loanscrefia.admin.edu.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("edu")
public class EduDomain extends BaseDomain {
	
	//교육이수정보(tb_lc_edu01)
	private String plMName;			// 모집인이름
	private String plMBirth;			// 모집인생년월일
	private String plMGender;			// 모집인성별
	private String careerTyp;			// 구분					
	private String plProduct;			// 취급상품				
	private String plEduNo;			// 교육이수번호		
	private String plCrfNo;				// 인증서번호			
	private String plEduDate;			// 수료일자
	private String regTimestamp;	// 등록일시
	
}
