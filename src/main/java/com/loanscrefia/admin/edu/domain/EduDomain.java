package com.loanscrefia.admin.edu.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("edu")
public class EduDomain extends BaseDomain {
	
	//교육이수정보(tb_lc_edu01)
	private String userName;		//성명
	private String userBirth;		//싱년월일
	private String userSex;			//성별
	private String processCd;		//교육과정명(대출/리스할부)
	private String careerTyp;		//구분							-> [CAR001]신규,경력
	private String companyGubun;	//교육기관
	private String deplomaNo;		//교육이수번호						-> 경력일 때
	private String passDate;		//교육수료일자
	private String examCertNo;		//인증서번호						-> 신규일 때
	private String examDate;		//인증일자
	private String createDate;		//등록일시
	
	private String careerTypNm;
	private String compDate;
	
	//검색
	private String srchInput;
}