package com.loanscrefia.common.main.domain;


import org.apache.ibatis.type.Alias;
import com.loanscrefia.common.common.domain.BaseDomain;
import lombok.Data;

@Data
@Alias("main")
public class MainDomain extends BaseDomain {

	private int regCnt;				// 등록신청 - 승인요청건수
	private int regUpdCnt;			// 등록신청 - 보완요청건수
	private int regFiCnt;			// 등록신청 - 5일 이내 처리 필요건수
	private int regAdminCnt;		// 등록신청 - 관리자 승인대기건수
	
	private int updCnt;				// 정보변경 - 변경요청건수
	private int updUpdCnt;			// 정보변경 - 보완요청건수
	private int updAdminCnt;		// 정보변경 - 관리자 승인대기건수
	
	private int dropCnt;			// 해지신청 - 해지요청건수
	private int dropUpdCnt;			// 해지신청 - 보완요청건수
	private int dropAdminCnt;		// 해지신청 - 관리자 승인대기건수

	private int memberNoRegCnt;		// 회원가입 - 가승인건수
	private int memberRegAdminCnt;	// 회원가입 - 관리자 승인대기건수
	
	//세션 정보
	private String creYn;			// 협회여부
	private String creGrp;			// 협회그룹 - [CRE001]1:실무자 / 2:관리자
}