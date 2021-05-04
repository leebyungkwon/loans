package com.loanscrefia.admin.company.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("company")
public class CompanyDomain extends BaseDomain {

	private int memberSeq;										//담당자 시퀀스
	@ExcelColumn(headerName = "담당자 아이디", order = 1)
	private String memberId;									//담당자 ID
	private String password;									//비밀번호
	private int comCode;										//회원사 코드
	@ExcelColumn(headerName = "회원사", order = 0)
	private String comCodeNm;									//코드 이름
	@ExcelColumn(headerName = "담당자 이름", order = 3)
	private String memberName;									//담당자 이름
	@ExcelColumn(headerName = "이메일", order = 6)
	private String email;										//이메일
	private String extensionNo;									//내선번호
	@ExcelColumn(headerName = "휴대폰번호", order = 7)
	private String mobileNo;									//휴대폰번호
	@ExcelColumn(headerName = "부서명", order = 2)
	private String deptNm;										//부서명
	@ExcelColumn(headerName = "직위명", order = 4)
	private String positionNm;									//직위명
	@ExcelColumn(headerName = "가입일", order = 5)
	private String joinDt;										//가입일
	private String roleName;									//권한명
	private String termsYn;										//약관동의
	private String termsDate;									//약관동의일
	private String apprStat;									//승인상태(1.미승인, 2. 가승인, 3. 승인)
	/* private String apprStatNm; */ 							//승인상태이름(1.미승인, 2. 가승인, 3. 승인)
	private int fileSeq;										//첨부파일시퀀스
	private int failCnt;										//로그인실패횟수
	private String creYn;										//협회여부
	private String dropYn;										//탈퇴여부
	private int updSeq;											//수정자시퀀스
	private String updTimestamp;								//수정일시
	private int[] memberSeqArr;									//멤버시퀀스어레이
	
}
