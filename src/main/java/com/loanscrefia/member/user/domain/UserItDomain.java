package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("userIt")
public class UserItDomain extends BaseDomain {

	//전산인력(tb_lc_mas01_it)
	private int operSeq;			//전산인력시퀀스
	private int masterSeq;			//접수번호시퀀스
	
	@ExcelColumn(headerName="성명", vCell="A", vLenMin=2, vLenMax=20)
	private String operName;		//이름
	
	@ExcelColumn(headerName="주민등록번호", vCell="B", vLenMin=14, vLenMax=14, vEncrypt="Y", chkFormat="pId")
	private String plMZId;			//주민등록번호
	
	private Integer fileSeq;		//첨부파일 그룹 시퀀스
	private String useYn;			//사용여부
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
	//전산인력 첨부파일 가공
	private FileDomain fileType19;
	private FileDomain fileType20;
	private FileDomain fileType37;
	private FileDomain fileType38;
}
