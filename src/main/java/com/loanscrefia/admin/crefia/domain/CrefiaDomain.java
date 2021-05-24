package com.loanscrefia.admin.crefia.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("crefia")
public class CrefiaDomain extends BaseDomain {

	private int memberSeq;				// 담당자시퀀스
	
	@NotBlank(message = "ID를 입력하세요.")
	@Pattern(regexp = "^[a-z][a-z0-9]{4,10}$", message = "ID는 영문(소문자),숫자 5~11 자리로 입력해 주세요.")
    private String memberId;				// 담당자ID(로그인ID)
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 최소 8~20 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 입력하세요.")
	private String password;				// 비밀번호
	
	@Pattern(regexp = "^[a-zA-Z가-힣]*${1,20}", message = "담당자이름을 1~20자리로 입력해 주세요.")
	private String memberName;			// 담당자이름
	
	@Email
	private String email;						// 이메일
	private String extensionNo;			// 내선번호
	private String mobileNo;				// 휴대폰번호
	private String deptNm;					// 부서명
	private String creYn;					// 협회여부
	private String creGrp;					// 협회그룹
	private String creGrpNm;				// 협회그룹명
	private String joinDt;					// 가입일
	private int[] memberSeqArr;			// 삭제 배열
	private int[] comCodeArr;			// 회원사 코드 배열
	
	/* ========================================================= */
	
	private String dropYn;					// 담당자 탈퇴 여부
	private Long comCode;					// 회원사 코드
	private String comName;				// 회원사 이름
	private String delYn;					// 회원사 탈퇴 여부
	

}
