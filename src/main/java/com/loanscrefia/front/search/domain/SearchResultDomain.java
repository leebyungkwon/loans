package com.loanscrefia.front.search.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("searchResult")
public class SearchResultDomain extends BaseDomain {
	
	private String name;							// 성명
	private String ssn;								// 주민번호
	private String mobile;							// 전화번호
	private String careerYn;						// 경력여부
	private String conNum;							// 계약번호
	private String bizCode;							// 업권코드(은행/저축은행/보험사등)
	private String corpNum;							// 법인등록번호(법인사용인)
	private String finCode;							// 금융기관코드 
	private String finName;							// 금융기관명	
	private String finPhone;						// 금융기관연락처
	private String conDate;							// 계약일
	private String loanType;						// 대출모집인유형코드(취급상품 01:대출, 05:리스, 03:TM대출, 06:TM대출)
	private String cancelDate;						// 해지일 
	private String cancelCode;						// 해지코드
	
	
}
