package com.loanscrefia.admin.users.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("users")
public class UsersDomain extends BaseDomain {

	//사용자정보(tb_lc_users)
	private int userSeq;
	private String userId;					// 회원ID
	private String password;				// 비밀번호
	private String userName;				// 회원명
	private String mobileNo;				// 전화번호
	private String plClass;					// 구분(개인 / 법인)
	private String plClassNm;				// 구분명
	private String plMZId;					// 주민등록번호
	private String email;					// 이메일
	private String userCi;					// 사용자CI값
	private String managerName;				// 담당자이름
	private String managerMobileNo;			// 담당자연락처
	private String managerEmail;			// 담당자이메일
	private String corpApprYn;				// 법인승인여부
	private String corpApprDt;				// 법인승인일
	private int fileSeq;					// 첨부파일시퀀스
	private String joinDt;					// 가입일
	private String termsYn;					// 약관동의여부
	private String termsDt;					// 약관동의일시
	private String dropYn;					// 탈퇴여부
	private String dropDt;					// 탈퇴일시
	private String inactiveYn;				// 휴면여부
	private String inactiveDt;				// 휴면일시
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
	
	private Integer corpSeq;
	private String plMerchantNo;
	private String plMerchantName;
	private String plBusinessNo;
	private String pathTyp;
	private String passYn;
	
	private String dis1;					// 결격사유 조회 필드
	private String dis2;					// 범죄경력 조회 필드
	private String disCd;					// 결격사유테이블 코드
	private String disVal;					// 결격사유테이블 코드값
	private String updDis1;					// 결격사유 수정일시
	private String updDis2;					// 범죄경력 수정일시
	
	private String dis9;					// 범죄이력
	private String dis10;					// 부실금융기관
	private String dis11;					// 영업취소
	private String dis12;					// 대부업자
	private String dis13;					// 다단계판매업자
	
	private String updDis9;					// 범죄이력 수정일시
	private String updDis10;				// 부실금융기관 수정일시
	private String updDis11;				// 영업취소 수정일시
	private String updDis12;				// 대부업자 수정일시
	private String updDis13;				// 다단계판매업자 수정일시
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	private Map<String, Object> excelMapParam;
	
	
	
	// 2021-11-08 정보변경 필드 추가
	private int userIndvReqSeq;				// 개인정보변경 시퀀스
	private String reqUserName;				// 정보변경 이름
	private String reqPlMZId;				// 정보변경 주민등록번호
	private String reqMobileNo;				// 정보변경 연락처
	private int reqFileSeq;					// 정보변경 첨부파일
	private String stat;					// 정보변경 상태
	private String statNm;					// 정보변경 상태명
	private String txt;						// 정보변경 사유
	private String reqDate;					// 정보변경 신청일
	private String compDate;				// 정보변경 완료일
	
	private int userCorpReqSeq;				// 법인정보변경 시퀀스
	private String reqPlMerchantName;		// 정보변경 법인명
	private String reqUserCi;				// 정보변경 CI
	
	private String apiResMsg;				// API결과메세지
	private String apiCode;					// API코드
	private String apiSuccessCode;			// API결과코드

	
}
