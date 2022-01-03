package com.loanscrefia.admin.apply.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("applyImwon")
public class ApplyImwonDomain extends BaseDomain {

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
	private String plEduNo;			//교육이수번호
	
	@ExcelColumn(headerName="경력시작일", vCell="F", vLenMin=10, vLenMax=10)
	private String careerStartDate;	//경력시작일
	
	@ExcelColumn(headerName="경력종료일", vCell="G", vLenMin=10, vLenMax=10)
	private String careerEndDate;	//경력종료일
	
	@ExcelColumn(headerName="상근여부", vCell="H", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String fullTmStat;		//상근여부					-> [FTM001]상근,비상근
	
	@ExcelColumn(headerName="전문인력여부", vCell="I", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String expertStat;		//전문인력여부				-> [EXP001]전문인력,비전문인력
	
	private Integer fileSeq;		//첨부파일 그룹 시퀀스
	
	//가공
	private String careerTypNm;		//구분명
	private String fullTmStatNm;	//상근여부명
	private String expertStatNm;	//전문인력여부명
	
	private String positionCd;		//직위코드
	private String positionCdNm;	//직위코드명
	
	private String properCd;		//임원자격적합여부코드
	private String properCdNm;		//임원자격적합여부코드명
	
	private String regAddr;			//등록기준지
	private String regAddrDetail;	//등록기준지 상세주소
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
	//임원 첨부파일 가공
	private FileDomain fileType7;
	private FileDomain fileType8;
	private FileDomain fileType9;
	private FileDomain fileType10;
	private FileDomain fileType11;
	private FileDomain fileType12;
	private FileDomain fileType13;
	private FileDomain fileType14;
	private FileDomain fileType15;
	private FileDomain fileType27;
	private FileDomain fileType28;
	private FileDomain fileType30;
	private FileDomain fileType33;
	private FileDomain fileType34;
	private FileDomain fileType46;
	private FileDomain fileType47;
	
	
	// 임원 첨부파일 체크
	private String checkCd100;
	private String checkCd101;
	private String checkCd102;
	private String checkCd103;
	private String checkCd104;
	private String checkCd105;
	private String checkCd106;
	private String checkCd107;
	private String checkCd108;
	private String checkCd109;
	private String checkCd110;
	private String checkCd111;
	private String checkCd113;
	private String checkCd114;
	private String checkCd115;
	private String checkCd116;
	private String checkCd117;
	private String checkCd118;
	private String checkCd119;
	private String checkCd120;

	
}
