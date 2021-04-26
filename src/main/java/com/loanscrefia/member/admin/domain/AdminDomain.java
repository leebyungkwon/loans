package com.loanscrefia.member.admin.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;
import com.loanscrefia.common.common.domain.BaseDomain;
import lombok.Data;

@Data
@Alias("admin")
public class AdminDomain extends BaseDomain{
	
	private long groupNo;					// 회원사그룹코드(key)
	@NotBlank(message = "회원사를 선택하세요.")
	private String companyCd;				// 회원사 코드
	private String companyCdNm;				// 회원사 코드명
	
	@NotBlank(message = "ID를 입력하세요.")
	private String userId;					// ID
	
	@NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{2,20}", message = "내용은 영문/숫자 2~20자리로 입력해 주세요.")
	private String password;				
	
	@NotBlank(message = "부서명을 입력하세요.")
	private String deptNm;					// 부서명
	
	@NotBlank(message = "담당자명을 입력하세요.")
	private String managerNm;				// 담당자명
	
	@NotBlank(message = "직위를 입력하세요.")
	private String ranksNm;					// 직위
	
	@NotBlank(message = "이메일을 입력하세요.")
	private String email;					// 이메일
	
	@NotBlank(message = "전화번호를 입력하세요.")
	private String phnNo;					// 전화번호
	
	private long atchNo;					// 첨부파일번호
	private String failCnt;					// 로그인실패횟수
	
	private String confirmYn;				// 승인여부
	private String confirmDate;				// 승인일시
	
	private String dropYn;					// 탈퇴여부
	private String dropDate;				// 탈퇴일시
	
	
}
