package com.loanscrefia.admin.apply.domain;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("applyIt")
public class ApplyItDomain extends BaseDomain {

	//전산인력(tb_lc_mas01_it)
	private int operSeq;			//전산인력시퀀스
	private int masterSeq;			//접수번호시퀀스
	
	@ExcelColumn(headerName="성명", vCell="A", vLenMin=2, vLenMax=20)
	//@Pattern(regexp = "[가-힣]{1,10}", message = "이름은 한글 1~10자리로 입력해 주세요.")
	private String operName;		//이름
	
	@ExcelColumn(headerName="주민등록번호", vCell="B", vLenMin=14, vLenMax=14)
	//@Pattern(regexp = "^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}${14,14}", message = "주민등록번호는 숫자 14자리(- 포함)로 입력해 주세요.")
	private String plMZId;			//주민등록번호
	
	private Integer fileSeq;		//첨부파일 그룹 시퀀스
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
	//전산인력 첨부파일 가공
	private FileDomain fileType19;
	private FileDomain fileType20;
	
	// 전산설비 첨부파일 체크
	private String checkCd300;
	private String checkCd301;
	private String checkCd302;
	private String checkCd303;
	private String checkCd304;
	private String checkCd305;
	private String checkCd306;
}


