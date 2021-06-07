package com.loanscrefia.member.user.domain.excel;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("userCorp")
public class UserCorpExcelDomain extends BaseDomain {

	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록(개인) 엑셀 업로드용 VO
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	@ExcelColumn(headerName="상호", vCell="A", vLenMin=2, vLenMax=30)
	private String plMerchantName;	//법인명
	
	@ExcelColumn(headerName="대표이사", vCell="B", vLenMin=2, vLenMax=10)
	private String plCeoName;		//대표이사명
	
	@ExcelColumn(headerName="법인등록번호", vCell="C", vLenMin=14, vLenMax=14, vEncrypt="N")
	private String plMerchantNo;	//법인등록번호
	
	@ExcelColumn(headerName="설립년월일", vCell="D", vLenMin=10, vLenMax=10)
	private String corpFoundDate;	//법인설립일
	
	@ExcelColumn(headerName="본점소재지", vCell="E", vLenMin=1, vLenMax=2, vEnum="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17")
	private String addr;			//본점소재지
	
	@ExcelColumn(headerName="상세주소", vCell="F", vLenMin=1, vLenMax=200)
	private String addrDetail;		//상세주소
	
	@ExcelColumn(headerName="자본금", vCell="G", vLenMin=1, vLenMax=1000000)
	private int capital;			//자본금
	
	@ExcelColumn(headerName="금융상품유형", vCell="H", vLenMin=1, vLenMax=1, vEnum="1,2,3", chkPrd="prd1")
	private String plProduct;		//금융상품유형(취급상품)
	
	@ExcelColumn(headerName="계약일자", vCell="I", vLenMin=10, vLenMax=10, chkCal="Y")
	private String comContDate;		//계약일자
	
	@ExcelColumn(headerName="위탁예정기간", vCell="J", vLenMin=10, vLenMax=10, chkCal="Y")
	private String entrustDate;		//위탁예정일
	
	@ExcelColumn(headerName="CI", vCell="K", vLenMin=1, vLenMax=88, chkCi="Y", chkPrd="prd2")
	private String ci;				//CI
}
