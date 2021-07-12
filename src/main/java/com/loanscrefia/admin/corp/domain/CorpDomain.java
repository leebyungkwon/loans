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
	
	private String plMerchantName;	//법인명
	private String plMerchantNo;	//법인등록번호
	private String pathTyp;			//등록경로					-> [CPT001]엑셀 등록,수동 등록
	private String passYn;			//금융감독원 승인여부
	
	//가공
	private String pathTypNm;		//등록경로명
	private int[] corpSeqArr;
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
}
