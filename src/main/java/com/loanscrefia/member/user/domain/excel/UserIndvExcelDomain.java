package com.loanscrefia.member.user.domain.excel;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("userIndv")
public class UserIndvExcelDomain extends BaseDomain {

	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록(개인) 엑셀 업로드용 VO
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	@ExcelColumn(headerName="구분", vCell="A", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String careerTyp;		//구분(신규/경력)
	
	@ExcelColumn(headerName="성명", vCell="B", vLenMin=2, vLenMax=20)
	private String plMName;			//모집인이름
	
	@ExcelColumn(headerName="주민등록번호", vCell="C", vLenMin=14, vLenMax=14)
	private String plMZId;			//모집인주민등록번호
	
	@ExcelColumn(headerName="휴대폰번호", vCell="D", vLenMin=13, vLenMax=13)
	private String plCellphone;		//모집인휴대전화번호
	
	@ExcelColumn(headerName="주소", vCell="E", vLenMin=1, vLenMax=2, vEnum="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17")
	private String addr;			//본점소재지
	
	@ExcelColumn(headerName="금융상품유형", vCell="F", vLenMin=1, vLenMax=1, vEnum="1,2,3,4,5,6,7")
	private String plProduct;		//금융상품유형(취급상품)
	
	@ExcelColumn(headerName="법인명", vCell="G", vLenMin=0, vLenMax=30)
	private String plMerchantName;	//법인명
	
	@ExcelColumn(headerName="법인등록번호", vCell="H", vLenMin=0, vLenMax=14)
	private String plMerchantNo;	//법인등록번호
	
	@ExcelColumn(headerName="교육이수번호", vCell="I", vLenMin=10, vLenMax=10, chkDb="edu")
	private int plEduNo;			//교육이수번호
	
	@ExcelColumn(headerName="경력시작일", vCell="J", vLenMin=10, vLenMax=10)
	private String careerStartDate;	//경력시작일
	
	@ExcelColumn(headerName="경력종료일", vCell="K", vLenMin=10, vLenMax=10)
	private String careerEndDate;	//경력종료일
	
	@ExcelColumn(headerName="계약일자", vCell="L", vLenMin=10, vLenMax=10)
	private String comContDate;		//계약일자
	
	@ExcelColumn(headerName="위탁예정기간", vCell="M", vLenMin=10, vLenMax=10)
	private String entrustDate;		//위탁예정일
}
