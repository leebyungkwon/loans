package com.loanscrefia.common.member.domain;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("signup")
public class SignupDomain extends BaseDomain{
	
	private Long 	memberNo;				// (?)
	private Long 	memberSeq;				// 담당자 시퀀스
	
	@NotBlank(message = "ID를 입력하세요.")
	@Pattern(regexp = "[a-zA-Z0-9]{5,11}", message = "ID는 영문,숫자 5~11 자리로 입력해 주세요.")
    private String 	memberId;				// 담당자ID
	
	private String password;				// 비밀번호 
	
	private int 	comCode;				// 회원사코드
	
	@Pattern(regexp = "^[a-zA-Z가-힣]*${1,20}", message = "담당자이름 은 한글 1~20자리로 입력해 주세요.")
	private String 	memberName;				// 담당자이름
	
	@Email
	private String 	email;					// 이메일
	
	@Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}${1,20}", message = "휴대폰번호를 다시 입력해 주세요.")
	private String 	mobileNo;				// 휴대폰번호
	
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}${1,20}", message = "회사전화번호를 다시 입력해 주세요.")
	private String 	extensionNo;			// 회사 전화번호
	
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]*${1,30}", message = "부서명 은 한글 1~30자리로 입력해 주세요.")
	private String 	deptNm;					// 부서명
	
	@Pattern(regexp = "^[a-zA-Z가-힣]*${1,10}", message = "직위명 은 한글 1~20자리로 입력해 주세요.")
	private String 	positionNm;				// 직위명
	
	private String 	roleName;				// 권한명
	private int 	fileSeq;				// 첨부파일ID
	
	private String optionTermsYn;			// 선택적약관동의
	
}