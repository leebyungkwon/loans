package com.loanscrefia.admin.users.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("corpUsersExcelDomain")
public class CorpUsersExcelDownDomain extends BaseDomain {

	@ExcelColumn(headerName = "이름", order = 0)
	private String userName;				
	@ExcelColumn(headerName = "연락처", order = 1)
	private String mobileNo;				
	@ExcelColumn(headerName = "법인명", order = 2)
	private String plMerchantName;
	@ExcelColumn(headerName = "법인번호", order = 3)	
	private String plMerchantNo;
	@ExcelColumn(headerName = "범죄이력", order = 4)	
	private String dis9;
	@ExcelColumn(headerName = "부실금융기관", order = 5)	
	private String dis10;
	@ExcelColumn(headerName = "영업취소", order = 6)	
	private String dis11;
	@ExcelColumn(headerName = "대부업자", order = 7)	
	private String dis12;
	@ExcelColumn(headerName = "다단계판매업자", order = 8)	
	private String dis13;
}
