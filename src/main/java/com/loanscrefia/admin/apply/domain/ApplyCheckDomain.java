package com.loanscrefia.admin.apply.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("applyCheck")
public class ApplyCheckDomain extends BaseDomain {
	private int fileSeq;
	private String checkCd;
}
