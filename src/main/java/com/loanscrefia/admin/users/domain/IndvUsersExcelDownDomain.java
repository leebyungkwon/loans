package com.loanscrefia.admin.users.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("indvUsersExcelDomain")
public class IndvUsersExcelDownDomain extends BaseDomain {

	@ExcelColumn(headerName = "이름", order = 0)
	private String userName;				// 회원명
	@ExcelColumn(headerName = "연락처", order = 2)
	private String mobileNo;				// 전화번호
	@ExcelColumn(headerName = "주민등록번호", order = 1)
	private String plMZId;					// 주민등록번호
	@ExcelColumn(headerName = "이메일", order = 3)
	private String email;					// 이메일
	@ExcelColumn(headerName = "결격사유조회결과", order = 4)
	private String dis1;					// 결격사유 조회 필드
	@ExcelColumn(headerName = "범죄경력조회결과", order = 5)
	private String dis2;					// 범죄경력 조회 필드
}
