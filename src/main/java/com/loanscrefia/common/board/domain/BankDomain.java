package com.loanscrefia.common.board.domain;


import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("bank")
public class BankDomain extends BaseDomain{
    
	private String plOrCode;					// 협회구분
	private String plRegYn;						// 상태구분(0:활동, 1:적격해지, 2:부적격해지)
	private String plGubun;						// 등록대상구분(1:개인, 3:법인)
	private String plRegistNo;					// 협회등록번호
	private String plZId;						// pl_z_id 대문자 비교확인해야함, 주민등록번호대체값
	private String plName;						// 모집인명/법인명
	private String comName;						// 법인명
	private String plRegDate;					// 협회등록일
	private String plHaejiDate;					// 협회해지일
	private String plMerchant;					// 모집법인명
	private String comManTel;					// 금융회사 전화번호
	private String plCellphone;					// 휴대폰번호
	
	private String plCi;						// ci값
	private String plHaejiCd2;					// 협회공유 해지코드
	private String plBreakCount;				// 위반이력횟수
	private String plBreakCode1;				// 위반이력코드1
	private String plBreakCode2;				// 위반이력코드2
	private String plBreakCode3;				// 위반이력코드3
	private String plBreakCode4;				// 위반이력코드4
	private String plBreakCode5;				// 위반이력코드5
	private String plBreakCode6;				// 위반이력코드6
	private String plBreakCode7;				// 위반이력코드7
	private String plBreakCode8;				// 위반이력코드8
	private String plBreakCode9;				// 위반이력코드9
	private String plBreakCode10;				// 위반이력코드10
	
	private String plAwareDate1;				// 위반이력1 위반확정일
	private String plAwareDate2;				// 위반이력2 위반확정일
	private String plAwareDate3;				// 위반이력3 위반확정일
	private String plAwareDate4;				// 위반이력4 위반확정일
	private String plAwareDate5;				// 위반이력5 위반확정일
	private String plAwareDate6;				// 위반이력6 위반확정일
	private String plAwareDate7;				// 위반이력7 위반확정일
	private String plAwareDate8;				// 위반이력8 위반확정일
	private String plAwareDate9;				// 위반이력9 위반확정일
	private String plAwareDate10;				// 위반이력10 위반확정일
	
	private String plFixDate1;					// 위반이력1 등록일자
	private String plFixDate2;					// 위반이력2 등록일자
	private String plFixDate3;					// 위반이력3 등록일자
	private String plFixDate4;					// 위반이력4 등록일자
	private String plFixDate5;					// 위반이력5 등록일자
	private String plFixDate6;					// 위반이력6 등록일자
	private String plFixDate7;					// 위반이력7 등록일자
	private String plFixDate8;					// 위반이력8 등록일자
	private String plFixDate9;					// 위반이력9 등록일자
	private String plFixDate10;					// 위반이력10 등록일자
	
	private String plHaejiDate2;				// 해지코드 협회등록일자
    
}