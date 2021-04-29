package com.loanscrefia.admin.company.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("company")
public class CompanyDomain extends BaseDomain {

	private int memberSeq;
	private String memberId;
	private String password;
	private int comCode;
	private String comCodeNm;
	private String memberName;
	private String email;
	private String mobileNo;
	private String deptNm;
	private String positionNm;
	private String joinDt;
	private String apprYn;
	private int fileSeq;
	private int failCnt;
	private String creYn;
	private String dropYn;
	private int updSeq;
	private String updTimestamp;
}
