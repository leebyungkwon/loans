package com.loanscrefia.admin.users.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("corpUsers")
public class CorpUsersExcelDomain extends BaseDomain {
	
	@ExcelColumn(headerName="성명", vCell="A")
	private String userName;
	
	@ExcelColumn(headerName="연락처", vCell="B")
	private String mobileNo;
	
	@ExcelColumn(headerName="법인명", vCell="C", vLenMin=1, vLenMax=20)
	private String plMerchantName;
	
	@ExcelColumn(headerName="법인번호", vCell="D", vLenMin=14, vLenMax=14, vEncrypt="Y")
	private String plMerchantNo;
	
	@ExcelColumn(headerName = "범죄이력", vCell="E", vLenMin=1, vLenMax=1, vEnum="Y,N")	
	private String dis9;
	
	@ExcelColumn(headerName = "부실금융기관", vCell="F", vLenMin=1, vLenMax=1, vEnum="Y,N")	
	private String dis10;
	
	@ExcelColumn(headerName = "영업취소", vCell="G", vLenMin=1, vLenMax=1, vEnum="Y,N")	
	private String dis11;
	
	@ExcelColumn(headerName = "대부업자", vCell="H", vLenMin=1, vLenMax=1, vEnum="Y,N")	
	private String dis12;
	
	@ExcelColumn(headerName = "다단계판매업자", vCell="I", vLenMin=1, vLenMax=1, vEnum="Y,N")	
	private String dis13;
	
}
