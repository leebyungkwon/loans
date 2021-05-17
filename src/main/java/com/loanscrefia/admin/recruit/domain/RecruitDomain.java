package com.loanscrefia.admin.recruit.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("recruit")
public class RecruitDomain extends BaseDomain {
	
	private int masterSeq;
	private int atchNo;

}
