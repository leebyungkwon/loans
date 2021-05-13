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
	
	@NotBlank(message = "비밀번호를 입력하세요.")
	private String password;				// 비밀번호 
	
	private int 	comCode;				// 회원사코드
	
	@NotBlank(message = "담당자이름 을 입력하세요.")
	@Pattern(regexp = "^[a-zA-Z가-힣]*${1,20}", message = "담당자이름 은 한글 1~20자리로 입력해 주세요.")
	private String 	memberName;				// 담당자이름
	
	@NotBlank(message = "이메일 을 입력해주세요.")
	@Email
	private String 	email;					// 이메일
	
	@Pattern(regexp = "^[0-9]*${0,20}", message = "휴대폰번호를 다시 입력해 주세요.")
	private String 	mobileNo;				// 휴대폰번호
	
	@NotBlank(message = "회사전화번호 를 입력해주세요.")
	@Pattern(regexp = "^[0-9]*${1,20}", message = "회사전화번호를 다시 입력해 주세요.")
	private String 	extensionNo;			// 회사 전화번호
	
	@NotBlank(message = "부서명 을 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]*${1,30}", message = "부서명 은 한글 1~30자리로 입력해 주세요.")
	private String 	deptNm;					// 부서명
	
	@NotBlank(message = "직위명 을 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z가-힣]*${1,10}", message = "직위명 은 한글 1~20자리로 입력해 주세요.")
	private String 	positionNm;				// 직위명
	
	private String 	roleName;				// 권한명
	private int 	fileSeq;				// 첨부파일ID
	
	private String optionTermsYn;			// 선택적약관동의
	
}