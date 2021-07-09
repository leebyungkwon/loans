package com.loanscrefia.member.admin.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;
import com.loanscrefia.common.common.domain.BaseDomain;
import lombok.Data;

@Data
@Alias("admin")
public class AdminDomain extends BaseDomain{
	
	private long groupNo;						// 회원사그룹코드(key) (?)
	
	private int comCode;						// 회원사 코드
	private String 	comCodeNm;				// 회원사 코드명
	private String 	memberId;					// ID
	
	@NotBlank(message = "비밀번호를 입력하세요.")
	private String 	password;					// 패스워드
	
	@NotBlank(message = "부서명 을 입력해주세요.")
	@Pattern(regexp = "^\\S[a-zA-Z0-9가-힣]*${1,30}", message = "부서명을 공백없이 2~30자리로 입력해 주세요.")
	private String 	deptNm;					// 부서명
	
	@NotBlank(message = "담당자이름 을 입력하세요.")
	@Pattern(regexp = "^\\S[a-zA-Z가-힣]*${1,20}", message = "담당자이름을 공백없이 1~20자리로 입력해 주세요.")
	private String 	memberName;			// 담당자이름
	
	@NotBlank(message = "직위명 을 입력해주세요.")
	@Pattern(regexp = "^\\S[a-zA-Z가-힣]*${1,20}", message = "직위명을 공백없이 2~20자리로 입력해 주세요.")
	private String 	positionNm;				// 직위명
	
	@NotBlank(message = "이메일 을 입력해주세요.")
	@Email
	private String 	email;							// 이메일
	
	@NotBlank(message = "회사전화번호 를 입력해주세요.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}${1,20}", message = "회사전화번호를 다시 입력해 주세요.")
	private String 	extensionNo;				// 회사전화번호
	
	private String 	mobileNo;					// 휴대폰번호
	private String 	joinDt;						// 가입일
	private String 	apprStat;					// 승인여부
	private Integer	fileSeq;					// 첨부파일시퀀스
	private int 	failCnt;					// 로그인 실패횟수
	private String	creYn;						// 협회여부
	private String 	dropYn;						// 탈퇴여부
	private int 	updSeq;						// 수정자 시퀀스
	private String 	updTimestamp;				// 수정일시
	private Long 	memberSeq;					// 담당자 시퀀스
	private String	msg;						// 가승인 사유
	private String 	tempMemberCheck;			// 가승인회원여부
	
}
