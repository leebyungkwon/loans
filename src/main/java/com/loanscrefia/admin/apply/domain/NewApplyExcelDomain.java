package com.loanscrefia.admin.apply.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("newApplyExcelDomain")
public class NewApplyExcelDomain extends BaseDomain {

	@ExcelColumn(headerName = "접수번호", order = 0)
	private String masterToId;		//접수번호ID(조회)
	
	@ExcelColumn(headerName = "회원사", order = 1)
	private String comCodeNm;		//담당회원사명
	
	@ExcelColumn(headerName = "가등록번호", order = 2)
	private String preLcNum;		//가등록번호
	
	@ExcelColumn(headerName = "모집인분류", order = 3)
	private String plClassNm;		//분류명

	@ExcelColumn(headerName = "법인사용인여부", order = 4)
	private String corpUserYn;		//법인사용인여부	

	@ExcelColumn(headerName = "금융상품유형", order = 5)
	private String plProductNm;		//취급상품명	

	@ExcelColumn(headerName = "이름", order = 6)
	private String plMName;			//모집인이름
	
	@ExcelColumn(headerName = "주민번호", order = 7)
	private String plMZId;			//모집인주민등록번호
	
	@ExcelColumn(headerName = "법인명", order = 8)
	private String plMerchantName;	//법인명
	
	@ExcelColumn(headerName = "법인번호", order = 9)
	private String plMerchantNo;	//법인등록번호
	
	@ExcelColumn(headerName = "요청일", order = 10)
	private String comRegDate;		//신청일(심사요청일)
	
	@ExcelColumn(headerName = "최초승인요청일", order = 11)
	private String firstAppDate;	//최초승인요청일
	
	@ExcelColumn(headerName = "승인남은일수", order = 12)
	private String creAppFiDateNm;	// 승인남은일
	
	@ExcelColumn(headerName = "실무자확인", order = 13)
	private String chkYnTxt;
	
	@ExcelColumn(headerName = "관리자확인", order = 14)
	private String adminChkYnTxt;
	
	@ExcelColumn(headerName = "승인상태", order = 15)
	private String plStatNm;		//처리상태명
	
	@ExcelColumn(headerName = "CI", order = 16)
	private String ci;				//CI값
	
	@ExcelColumn(headerName = "모집인휴대전화번호", order = 17)
	private String plCellphone;		//모집인휴대전화번호
	
	@ExcelColumn(headerName = "관할검찰청또는지청", order = 18)
	private String withinGovrNm;	//관할검찰청또는지청
	
	@ExcelColumn(headerName = "관할경찰청", order = 19)
	private String withinPoliceNm;	//관할경찰청
	
	@ExcelColumn(headerName = "관할행정관청", order = 20)
	private String withinAdmNm;		//관할행정관청
	
	@ExcelColumn(headerName = "관할행정관청(직접입력)", order = 21)
	private String withinAdmName;	//관할행정관청(직접입력)	
	
	
}
