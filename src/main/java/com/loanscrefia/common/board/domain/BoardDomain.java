package com.loanscrefia.common.board.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("board")
public class BoardDomain extends BaseDomain{

	private String 	BoardNo;            // (?)								// (삭제 가능 ? - templete 에서 사용중)
	private String 	BoardType;         // (?)								// (삭제 가능 ? - templete 에서 사용중)
	
	private long 	groupNo;            // 회원사그룹코드(key) 	// test 데이터 (삭제 가능 O)
	private int 		comCode;           // 회원사 코드					// test 데이터 (삭제 가능 O)
	private String 	comCodeNm;      // 회원사 코드명				// test 데이터 (삭제 가능 O)
	private String 	memberId;         	// ID								// test 데이터 (삭제 가능 O)
	private String 	password;         	// 패스워드						// test 데이터 (삭제 가능 O)
	private String 	deptNm;            // 부서명						// test 데이터 (삭제 가능 O)
	private String 	memberName;    // 담당자이름					// test 데이터 (삭제 가능 O)
	private String 	positionNm;      	// 직위명						// test 데이터 (삭제 가능 O)
	private String 	email;              	// 이메일						// test 데이터 (삭제 가능 O)
	private String 	extensionNo;      	// 회사전화번호				// test 데이터 (삭제 가능 O)
	private String 	mobileNo;         	// 휴대폰번호					// test 데이터 (삭제 가능 O)
	private String 	joinDt;            	// 가입일						// test 데이터 (삭제 가능 O)
	private String 	apprStat;         	// 승인여부						// test 데이터 (삭제 가능 O)
	private int 		fileSeq;            	// 첨부파일ID					// test 데이터 (삭제 가능 O)
	private int 		failCnt;            	// 로그인 실패횟수			// test 데이터 (삭제 가능 O)
	private String 	creYn;            	// 협회여부						// test 데이터 (삭제 가능 O)
	private String 	dropYn;           	// 탈퇴여부						// test 데이터 (삭제 가능 O)
	private int 		updSeq;             // 수정자 시퀀스				// test 데이터 (삭제 가능 O)
	private String 	updTimestamp;   // 수정일시						// test 데이터 (삭제 가능 O)
	private Long 	memberSeq;       // 담당자 시퀀스				// test 데이터 (삭제 가능 O)
	private String 	tempMemberCheck;									// test 데이터 (삭제 가능 O)
	private int[] 	memberSeqArr; 										// test 데이터 (삭제 가능 O)
	private int 		AttchNo1; 			// ?								// (삭제 가능 ? - templete 에서 사용중)
	private int 		AttchNo2; 			// ?								// (삭제 가능 ? - templete 에서 사용중)

}