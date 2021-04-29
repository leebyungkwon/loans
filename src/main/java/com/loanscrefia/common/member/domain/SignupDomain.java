package com.loanscrefia.common.member.domain;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("signup")
public class SignupDomain extends BaseDomain{
	
	private Long memberNo;
	
	private Long memberSeq;
	
	@NotBlank(message = "ID를 입력하세요.")
    private String memberId;				// 담당자ID
	
	@NotBlank(message = "비밀번호를 입력하세요.")
	private String password;				// 비밀번호 
	
	@NotBlank(message = "회원사 를 선택해주세요.")
	private int 		comCode;				// 회원사코드
	
	@NotBlank(message = "담당자이름 을 입력하세요.")
	private String memberName;			// 담당자이름
	
	@NotBlank(message = "이메일 을 입력해주세요.")
	private String email;						// 이메일
	
	@NotBlank(message = "전화번호 를 입력해주세요.")
	private String mobileNo;				// 전화번호
	
	@NotBlank(message = "부서명 을 입력해주세요.")
	private String deptNm;					// 부서명
	
	@NotBlank(message = "직위명 을 입력해주세요.")
	private String positionNm;			// 직위명
	private int 	fileSeq;					// 첨부파일ID
	
    List<MemberRoleDomain> roles;
	                                     
}