package com.loanscrefia.admin.crefia.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("crefia")
public class CrefiaDomain extends BaseDomain {

	private Long memberSeq;					// 담당자시퀀스
    private String memberId;				// 담당자ID(로그인ID)
	private String password;				// 비밀번호
	private String memberName;				// 담당자이름
	private String email;					// 이메일
	private String extensionNo;				// 내선번호
	private String mobileNo;				// 휴대폰번호
	private String deptNm;					// 부서명
	private String creYn;					// 협회여부
	private String creGrp;					// 협회그룹
	private String creGrpNm;				// 협회그룹명
	private String joinDt;					// 가입일
	private int[] masterSeqArr;				// 삭제 배열
	
}
