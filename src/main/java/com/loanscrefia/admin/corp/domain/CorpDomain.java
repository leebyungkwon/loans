package com.loanscrefia.admin.corp.domain;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("corp")
public class CorpDomain extends BaseDomain {

	//법인정보(tb_lc_corp)
	private Integer corpSeq;		//법인시퀀스
	
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]*${1,20}", message = "법인명을 다시 입력해 주세요.")
	private String plMerchantName;	//법인명
	
	@Pattern(regexp = "^(\\d{6})-(\\d{7})*${14,14}", message = "법인등록번호 14자리 ( -포함)으로 다시 입력해 주세요.")
	private String plMerchantNo;	//법인등록번호
	
	private String pathTyp;			//등록경로					-> [CPT001]모집인 등록 엑셀 등록,수동 등록
	
	//가공
	private String pathTypNm;		//등록경로명
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
}
