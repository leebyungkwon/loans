package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("userRegExcelDomain")
public class UserRegExcelDomain extends BaseDomain {

	@ExcelColumn(headerName = "접수번호", order = 0)
	private String masterToId;		
	@ExcelColumn(headerName = "모집인분류", order = 1)
	private String plClassNm;		
	@ExcelColumn(headerName = "법인사용인여부", order = 2)
	private String corpUserYn;		
	@ExcelColumn(headerName = "금융상품유형", order = 3)
	private String plProductNm;		
	@ExcelColumn(headerName = "이름", order = 4)
	private String plMName;			
	@ExcelColumn(headerName = "주민번호", order = 5)
	private String plMZId;			
	@ExcelColumn(headerName = "휴대폰번호", order = 6)
	private String plCellphone;		
	@ExcelColumn(headerName = "법인명", order = 7)
	private String plMerchantName;	
	@ExcelColumn(headerName = "법인번호", order = 8)
	private String plMerchantNo;	
	@ExcelColumn(headerName = "계약기간", order = 9)
	private String comContDate;		
	@ExcelColumn(headerName = "승인요청일", order = 10)
	private String comRegDate;	
	@ExcelColumn(headerName = "승인상태", order = 11)
	private String plStatNm;
	@ExcelColumn(headerName = "ci", order = 12)
	private String ci;	
	
}
