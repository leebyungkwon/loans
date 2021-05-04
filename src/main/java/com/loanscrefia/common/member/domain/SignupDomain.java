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
	
	private Long memberNo;
	
	private Long memberSeq;
	
	@NotBlank(message = "ID를 입력하세요.")
	@Pattern(regexp = "[a-zA-Z]{1,11}", message = "ID 는 영문 5~10 자리로 입력해 주세요.")
    private String memberId;				// 담당자ID
	
	@NotBlank(message = "비밀번호를 입력하세요.")
	private String password;				// 비밀번호 
	
	private int 		comCode;				// 회원사코드
	
	@NotBlank(message = "담당자이름 을 입력하세요.")
	@Pattern(regexp = "^[가-힣]*${1,20}", message = "담당자이름 은 한글 1~20자리로 입력해 주세요.")
	private String memberName;			// 담당자이름
	
	@NotBlank(message = "이메일 을 입력해주세요.")
	@Email
	private String email;						// 이메일
	
	private String mobileNo;				// 휴대폰번호
	
	@NotBlank(message = "회사전화번호 를 입력해주세요.")
	private String extensionNo;				// 회사 전화번호
	
	@NotBlank(message = "부서명 을 입력해주세요.")
	@Pattern(regexp = "^[가-힣]*${1,30}", message = "부서명 은 한글 1~30자리로 입력해 주세요.")
	private String deptNm;					// 부서명
	
	@NotBlank(message = "직위명 을 입력해주세요.")
	@Pattern(regexp = "^[가-힣]*${1,10}", message = "직위명 은 한글 1~20자리로 입력해 주세요.")
	private String positionNm;			// 직위명
	
	private String roleName;			// 직위명
	
	private int 	fileSeq;					// 첨부파일ID
	
}