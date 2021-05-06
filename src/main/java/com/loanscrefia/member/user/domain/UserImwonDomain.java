package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("userImwon")
public class UserImwonDomain extends BaseDomain {

	//대표자 및 임원(tb_lc_mas01_imwon)
	private int excSeq;				//임원시퀀스
	private int masterSeq;			//접수번호시퀀스
	
	@ExcelColumn(headerName="구분", vCell="A", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String careerTyp;		//구분					-> [CAR001]신규,경력
	
	@ExcelColumn(headerName="직위", vCell="B", vLenMin=1, vLenMax=20)
	private String positionNm;		//직위명
	
	@ExcelColumn(headerName="성명", vCell="C", vLenMin=2, vLenMax=20)
	private String excName;			//이름
	
	@ExcelColumn(headerName="주민등록번호", vCell="D", vLenMin=14, vLenMax=14)
	private String plMZId;			//주민등록번호
	
	@ExcelColumn(headerName="교육이수번호", vCell="E", vLenMin=10, vLenMax=10, chkDb="edu")
	private int plEduNo;			//교육이수번호
	
	@ExcelColumn(headerName="경력시작일", vCell="F", vLenMin=10, vLenMax=10)
	private String careerStartDt;	//경력시작일
	
	@ExcelColumn(headerName="경력종료일", vCell="G", vLenMin=10, vLenMax=10)
	private String careerEndDt;		//경력종료일
	
	@ExcelColumn(headerName="상근여부", vCell="H", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String fullTmYn;		//상근여부					-> []상근,비상근
	
	@ExcelColumn(headerName="전문인력여부", vCell="I", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String expertYn;		//전문인력여부				-> []전문인력,비전문인력
	
	private int fileSeq;			//첨부파일 그룹 시퀀스
	
	//가공
	private String careerTypNm;		//구분명
	private String plProductNm;		//취급상품명 				-> tb_lc_mas01테이블의 pl_product 컬럼 참조
	private String fullTmYnNm;		//상근여부명
	private String expertYnNm;		//전문인력여부명
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
}
