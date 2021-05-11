package com.loanscrefia.admin.company.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("company")
public class CompanyDomain extends BaseDomain {

	private int memberSeq;								//담당자 시퀀스
	@ExcelColumn(headerName = "담당자 아이디", order = 1)
	private String memberId;								//담당자 ID
	private String password;								//비밀번호
	private Long comCode;									//회원사 코드
	@ExcelColumn(headerName = "회원사", order = 0)
	private String comCodeNm;							//코드 이름
	@ExcelColumn(headerName = "담당자 이름", order = 3)
	private String memberName;							//담당자 이름
	@ExcelColumn(headerName = "이메일", order = 8)
	private String email;										//이메일
	private String extensionNo;							//내선번호
	@ExcelColumn(headerName = "휴대폰번호", order = 7)
	private String mobileNo;								//휴대폰번호
	@ExcelColumn(headerName = "부서명", order = 2)
	private String deptNm;									//부서명
	@ExcelColumn(headerName = "직위명", order = 4)
	private String positionNm;							//직위명
	@ExcelColumn(headerName = "회원가입일", order = 5)
	private String joinDt;									//가입일
	private String roleName;								//권한명(1.미승인: NO_MEMBER, 2.가승인: TEMP_MEMEBER 3. 승인: MEMBER)
	private String termsYn;									//약관동의
	private String termsDate;								//약관동의일
	@ExcelColumn(headerName = "승인상태", order = 6)
	private String apprStat;								//승인상태(1.미승인, 2. 가승인, 3. 승인)
	/* private String apprStatNm; */					//승인상태이름(1.미승인, 2. 가승인, 3. 승인)
	private int fileSeq;										//첨부파일시퀀스
	private int failCnt;										//로그인실패횟수
	private String creYn;									//협회여부
	private String dropYn;									//탈퇴여부
	private int updSeq;										//수정자시퀀스
	private String updTimestamp;						//수정일시
	private int[] memberSeqArr;							//멤버시퀀스어레이
	
	/*========================== [회원사 관리] ==============================*/
	
	@NotBlank(message = "회원사(상호명)을 입력해주세요.")
	@Pattern(regexp = "^[가-힣]*${1,20}", message = "회원사(상호명)을 다시 입력해 주세요.")
	private String comName;								// 회원사(상호명)
	
	@NotBlank(message = "법인등록번호를 입력해주세요.")
	@Pattern(regexp = "^[0-9]*${13}", message = "법인등록번호 13자리를 다시 입력해 주세요.")
	private String plMerchantNo;						// 법인등록번호
	
	@NotBlank(message = "사업자등록번호를 입력해주세요.")
	@Pattern(regexp = "^[0-9]*${10}", message = "사업자등록번호 10자리 다시 입력해 주세요.")
	private String plBusinessNo;							// 사업자등록번호
	
	@NotBlank(message = "회사전화번호를 입력해주세요.")
	@Pattern(regexp = "^[0-9]*${1,20}", message = "회사전화번호를 다시 입력해 주세요.")
	private String compPhoneNo;						// 회사전화번호

}
