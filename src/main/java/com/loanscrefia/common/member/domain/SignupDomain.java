package com.loanscrefia.common.member.domain;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("signup")
public class SignupDomain extends BaseDomain{
	
	private Long memberNo;
	
	@NotBlank(message = "ID를 입력하세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{2,20}", message = "내용은 영문/숫자 2~20자리로 입력해 주세요.")
    private String memberId;				// 담당자ID
	@NotBlank(message = "비밀번호를 입력하세요.")
	private String password;				// 비밀번호 
	private int 		comCode;				// 회원사코드
	private String memberName;			// 담당자이름
	private String email;						// 이메일
	private String mobileNo;				// 전화번호
	private String deptNm;					// 부서명
	private String positionNm;			// 직위명
	private Long 	fileSeq;					// 첨부파일ID
	
    List<MemberRoleDomain> roles;
	                                     
}