package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("userExcelDomain")
public class UserExcelDomain extends BaseDomain {

	@ExcelColumn(headerName = "접수번호", order = 0)
	private String masterToId;		//접수번호ID(조회)
	@ExcelColumn(headerName = "가등록번호", order = 15)
	private String preLcNum;		//가등록번호
	@ExcelColumn(headerName = "법인사용인여부", order = 5)
	private String corpUserYn;		//법인사용인여부
	@ExcelColumn(headerName = "등록번호", order = 1)
	private String plRegistNo;		//모집인등록번호						-> 은행연합회에서 던져주는 정보(1:n)
	@ExcelColumn(headerName = "CI", order = 16)
	private String ci;				//CI값
	@ExcelColumn(headerName = "이름", order = 7)
	private String plMName;			//모집인이름
	@ExcelColumn(headerName = "주민번호", order = 8)
	private String plMZId;			//모집인주민등록번호
	@ExcelColumn(headerName = "휴대폰번호", order = 9)
	private String plCellphone;		//모집인휴대전화번호
	@ExcelColumn(headerName = "법인명", order = 10)
	private String plMerchantName;	//법인명
	@ExcelColumn(headerName = "법인번호", order = 11)
	private String plMerchantNo;	//법인등록번호
	@ExcelColumn(headerName = "승인완료일", order = 12)
	private String creAppDate;		//승인일
	@ExcelColumn(headerName = "자격취득일", order = 14)
	private String creLicenseDate;	//자격취득일
	@ExcelColumn(headerName = "담당자명", order = 17)
	private String memberNm;		//담당자명
	@ExcelColumn(headerName = "모집인분류", order = 4)
	private String plClassNm;		//분류명
	@ExcelColumn(headerName = "금융상품유형", order = 6)
	private String plProductNm;		//취급상품명
	@ExcelColumn(headerName = "모집인상태", order = 2)
	private String plRegStatNm;		//모집인상태명
	@ExcelColumn(headerName = "처리상태", order = 3)
	private String plStatNm;		//처리상태명
	@ExcelColumn(headerName = "결제완료일", order = 13)
	private String payRegDate;
}
