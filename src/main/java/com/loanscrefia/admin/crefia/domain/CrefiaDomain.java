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

	private int memberSeq;					// 담당자시퀀스
	
	@NotBlank(message = "ID를 입력하세요.")
	@Pattern(regexp = "^[A-za-z0-9]*${5,11}", message = "ID는 영문,숫자 5~11 자리로 입력해 주세요.")
    private String memberId;				// 담당자ID(로그인ID)
	
	private String password;				// 비밀번호
	
	@Pattern(regexp = "^[a-zA-Z가-힣]*${1,20}", message = "담당자이름 1~20자리로 입력해 주세요.")
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

}
