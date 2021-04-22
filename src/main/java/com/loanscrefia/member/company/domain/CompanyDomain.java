package com.loanscrefia.member.company.domain;

import org.apache.ibatis.type.Alias;
import com.loanscrefia.common.common.domain.BaseDomain;
import lombok.Data;

@Data
@Alias("company")
public class CompanyDomain extends BaseDomain{
	
	private String comNo;
	private String comNm;
	private String comId;
	private String comDeptNo;
	private String comDeptNm;
	private String managerNm;
	private String test1;
	
	private String approveCd;
	private String approveCdNm;
	
	
}
