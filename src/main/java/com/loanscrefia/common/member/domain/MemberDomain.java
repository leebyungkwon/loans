package com.loanscrefia.common.member.domain;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Builder;
import lombok.Data;

@Data
@Alias("member")
public class MemberDomain extends BaseDomain{
	
	private Long memberSeq;					// 담당자시퀀스
    private String memberId;				// 담당자ID(로그인ID)
	private String password;				// 비밀번호
	private int comCode;					// 회원사코드
	private String comCodeNm;				// 회원사코드명
	private String memberName;				// 담당자이름
	private String email;					// 이메일
	private String mobileNo;				// 전화번호
	private String deptNm;					// 부서명
	private String positionNm;				// 직위명
	private String joinDt;					// 가입일
	private String roleName;				// 권한명
	private String apprStat;				// 승인여부
	public boolean apprYnCheck() {
		if("1".equals(apprStat)) {
			return false;
		}else {
			return true;
		}
	}
	private int fileSeq;					// 첨부파일시퀀스
	private int fileCnt;					
	private int failCnt;					// 로그인실패횟수
	private String failYn;					// 로그인실패잠금여부
	public boolean failCntCheck() {
		if(failCnt >= 10) {
			return false;
		}else {
			return true;
		}
	}
	
	private String msg;
	private String creYn;					// 협회여부
	private String creGrp;
	private String dropYn;					// 탈퇴여부
    
    
}