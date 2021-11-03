package com.loanscrefia.admin.users.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("indvUsers")
public class IndvUsersExcelDomain extends BaseDomain {
	
	@ExcelColumn(headerName="성명", vCell="A", vLenMin=1, vLenMax=20)
	private String userName;

	@ExcelColumn(headerName="주민등록번호", vCell="B", vLenMin=14, vLenMax=14, vEncrypt="Y")
	private String plMZId;
	
	@ExcelColumn(headerName="연락처", vCell="C")
	private String mobileNo;
	
	@ExcelColumn(headerName="이메일", vCell="D")
	private String email;
	
	@ExcelColumn(headerName="결격사유", vCell="E", vLenMin=1, vLenMax=1, vEnum="Y,N")
	private String dis1;
	
	@ExcelColumn(headerName="범죄경력", vCell="F", vLenMin=1, vLenMax=1, vEnum="Y,N")
	private String dis2;
}
