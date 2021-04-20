package com.loanscrefia.admin.recruit.domain;

import org.apache.ibatis.type.Alias;
import com.loanscrefia.common.common.domain.BaseDomain;
import lombok.Data;

@Data
@Alias("recruit")
public class RecruitDomain extends BaseDomain{
	

	private long recruitNo;
	private String userNm;
	private String userJumin;
	private String userAddr;
	private String prdNm;
	private String eduStatus;
	private String startWork;
	private String endWork;
	
	private String colTest1;
	private String colTest2;
	private String colTest3;
	
	private long atchNo;
	
	
}
