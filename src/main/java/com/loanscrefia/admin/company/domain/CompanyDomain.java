package com.loanscrefia.admin.company.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("company")
public class CompanyDomain extends BaseDomain {

	private int memberSeq;
	@ExcelColumn(headerName = "담당자 아이디", order = 1)
	private String memberId;
	private String password;
	private int comCode;
	private int extensionNo;
	@ExcelColumn(headerName = "회원사", order = 0)
	private String comCodeNm;
	@ExcelColumn(headerName = "담당자 이름", order = 3)
	private String memberName;
	@ExcelColumn(headerName = "이메일", order = 7)
	private String email;
	@ExcelColumn(headerName = "휴대폰번호", order = 8)
	private String mobileNo;
	@ExcelColumn(headerName = "부서명", order = 2)
	private String deptNm;
	@ExcelColumn(headerName = "직위명", order = 4)
	private String positionNm;
	@ExcelColumn(headerName = "가입일", order = 5)
	private String joinDt;
	@ExcelColumn(headerName = "승인여부", order = 6)
	private String apprYn;
	private int fileSeq;
	private int failCnt;
	private String creYn;
	private String dropYn;
	private int updSeq;
	private String updTimestamp;
	private int[] memberSeqArr;
	
}
