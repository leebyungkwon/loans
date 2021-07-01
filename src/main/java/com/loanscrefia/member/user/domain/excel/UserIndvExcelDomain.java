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
	
	@ExcelColumn(headerName="구분", vCell="A", vLenMin=1, vLenMax=1, vEnum="1,2", chkDb="edu1")
	private String careerTyp;		//구분(신규/경력)
	
	@ExcelColumn(headerName="성명", vCell="B", vLenMin=2, vLenMax=20, chkDb="edu2")
	private String plMName;			//모집인이름
	
	@ExcelColumn(headerName="주민등록번호", vCell="C", vLenMin=14, vLenMax=14, chkDb="edu3", vEncrypt="Y", chkFormat="pId")
	private String plMZId;			//모집인주민등록번호
	
	@ExcelColumn(headerName="휴대폰번호", vCell="D", vLenMin=13, vLenMax=13)
	private String plCellphone;		//모집인휴대전화번호
	
	@ExcelColumn(headerName="주소", vCell="E", vLenMin=1, vLenMax=2, vEnum="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17")
	private String addr;			//주소
	
	@ExcelColumn(headerName="상세주소", vCell="F", vLenMin=1, vLenMax=200)
	private String addrDetail;		//상세주소
	
	@ExcelColumn(headerName="금융상품유형", vCell="G", vLenMin=2, vLenMax=2, vEnum="01,05", chkDb="edu4", chkPrd="prd1")
	private String plProduct;		//금융상품유형(취급상품)
	
	@ExcelColumn(headerName="법인명", vCell="H", vLenMin=0, vLenMax=30)
	private String plMerchantName;	//법인명
	
	@ExcelColumn(headerName="법인등록번호", vCell="I", vLenMin=0, vLenMax=14, chkDb="corp", vEncrypt="Y", chkFormat="mNo")
	private String plMerchantNo;	//법인등록번호
	
	@ExcelColumn(headerName="교육이수번호", vCell="J", vLenMin=10, vLenMax=30, chkDb="edu5")
	private String plEduNo;			//교육이수번호
	
	@ExcelColumn(headerName="경력시작일", vCell="K", vLenMin=0, vLenMax=10) //, chkFormat="cal"
	private String careerStartDate;	//경력시작일
	
	@ExcelColumn(headerName="경력종료일", vCell="L", vLenMin=0, vLenMax=10) //, chkFormat="cal"
	private String careerEndDate;	//경력종료일
	
	@ExcelColumn(headerName="계약일자", vCell="M", vLenMin=10, vLenMax=10, chkFormat="cal")
	private String comContDate;		//계약일자
	
	@ExcelColumn(headerName="위탁예정기간", vCell="N", vLenMin=10, vLenMax=10, chkFormat="cal")
	private String entrustDate;		//위탁예정일
	
	@ExcelColumn(headerName="CI", vCell="O", vLenMin=1, vLenMax=88, chkFormat="ci", chkPrd="prd2")
	private String ci;				//CI
}
