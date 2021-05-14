package com.loanscrefia.admin.corp.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("corp")
public class CorpDomain extends BaseDomain {

	//법인정보(tb_lc_corp)
	private Integer corpSeq;		//법인시퀀스
	private String plMerchantName;	//법인명
	private String plMerchantNo;	//법인등록번호
	private String pathTyp;			//등록경로					-> [CPT001]모집인 등록 엑셀 등록,수동 등록
	
	//가공
	private String pathTypNm;		//등록경로명
}
