package com.loanscrefia.admin.recruit.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("recruitExpert")
public class RecruitExpertDomain extends BaseDomain {

	//전문인력(tb_lc_mas01_expert)
	private int expSeq;				//전문인력시퀀스
	private int masterSeq;			//접수번호시퀀스
	
	@ExcelColumn(headerName="구분", vCell="A", vLenMin=1, vLenMax=1, vEnum="1,2")
	private String careerTyp;		//구분					-> [CAR001]신규,경력
	
	@ExcelColumn(headerName="성명", vCell="B", vLenMin=2, vLenMax=20)
	private String expName;			//이름
	
	@ExcelColumn(headerName="주민등록번호", vCell="C", vLenMin=14, vLenMax=14)
	private String plMZId;			//주민등록번호
	
	@ExcelColumn(headerName="교육이수번호", vCell="D", vLenMin=10, vLenMax=10, chkDb="edu")
	private String plEduNo;			//교육이수번호
	
	@ExcelColumn(headerName="경력시작일", vCell="E", vLenMin=10, vLenMax=10)
	private String careerStartDate;	//경력시작일
	
	@ExcelColumn(headerName="경력종료일", vCell="F", vLenMin=10, vLenMax=10)
	private String careerEndDate;	//경력종료일
	
	private Integer fileSeq;		//첨부파일 그룹 시퀀스
	
	//가공
	private String careerTypNm;		//구분명
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
	//전문인력 첨부파일 가공
	private FileDomain fileType16;
	private FileDomain fileType17;
	private FileDomain fileType18;
	private FileDomain fileType31;
	private FileDomain fileType35;
	private FileDomain fileType36;
	private FileDomain fileType39;
	private FileDomain fileType40;
}
