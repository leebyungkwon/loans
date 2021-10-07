package com.loanscrefia.system.batch.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.member.user.domain.UserDomain;

@Mapper
public interface BatchRepository {
	
	//모집인 엑셀 업로드 후 1개월동안 처리상태가 미요청 + 모집인상태가 승인전인 경우 -> 모집인 데이터 및 모집인 관련 첨부파일 삭제
	List<UserDomain> selectExcelUploadUserAndFileDelete();
	
	// 마지막로그인일시 기준 3개월 지난 회원 리스트
	List<UsersDomain> selectInactiveUser(UsersDomain usersDomain);
	
	// 휴면회원 테이블로 insert
	void insertInactiveUserBatch(UsersDomain usersDomain);
	
	// 휴면회원 테이블 update - null 처리
	void updateInactiveUserBatch(UsersDomain usersDomain);
	
}
