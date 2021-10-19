package com.loanscrefia.admin.users.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("users")
public class UsersDomain extends BaseDomain {

	//사용자정보(tb_lc_users)
	private int userSeq;
	@ExcelColumn(headerName = "아이디", order = 0)
	private String userId;					// 회원ID
	private String password;				// 비밀번호
	@ExcelColumn(headerName = "이름", order = 1)
	private String userName;				// 회원명
	@ExcelColumn(headerName = "연락처", order = 2)
	private String mobileNo;				// 전화번호
	private String plClass;					// 구분(개인 / 법인)
	@ExcelColumn(headerName = "구분", order = 3)
	private String plClassNm;				// 구분명
	private String plMZId;					// 주민등록번호
	@ExcelColumn(headerName = "이메일", order = 4)
	private String email;					// 이메일
	private String userCi;					// 사용자CI값
	private String managerName;				// 담당자이름
	private String managerMobileNo;			// 담당자연락처
	private String managerEmail;			// 담당자이메일
	private String corpApprYn;			// 법인승인여부
	private String corpApprDt;				// 법인승인일
	private int fileSeq;					// 첨부파일시퀀스
	@ExcelColumn(headerName = "가입일", order = 5)
	private String joinDt;					// 가입일
	private String termsYn;					// 약관동의여부
	private String termsDt;					// 약관동의일시
	@ExcelColumn(headerName = "탈퇴여부", order = 9)
	private String dropYn;					// 탈퇴여부
	private String dropDt;					// 탈퇴일시
	@ExcelColumn(headerName = "휴면여부", order = 7)
	private String inactiveYn;				// 휴면여부
	private String inactiveDt;				// 휴면일시
	@ExcelColumn(headerName = "마지막로그인일시", order = 6)
	private String lastLoginDt;				// 마지막로그인일시
	
	// 로그인에 사용되는 flag - 법인승인여부
	public boolean apprYnCheck() {
		if("Y".equals(corpApprYn)) {
			return true;
		}else {
			return false;
		}
	}
	
	private int failCnt;					// 로그인실패횟수
	private String failStopDt;				// 로그인5회실패시잠금일시
	@ExcelColumn(headerName = "로그인차단여부", order = 8)
	private String failStopYn;				// 로그인닫힘여부(30분간 로그인 불가)
	private String pwChangeDt;				// 비밀번호변경일시
	
	// 로그인에 사용되는 flag - 로그인 5회 실패시 닫힘여부
	public boolean failStopCheck() {
		if("Y".equals(failStopYn)) {
			return true;
		}else {
			return false;
		}
	}
	
	private String pwToken;					// 비밀번호찾기에 필요한 토큰
	private String pwTokenDt;				// 토큰유효일시
	private String srchDate1;				// 가입일(시작)
	private String srchDate2;				// 가입일(종료)
	private int[] userSeqArr;				// 그리드 체크박스
}
