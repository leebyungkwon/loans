package com.loanscrefia.system.batch.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.domain.BatchReqDomain;

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

	List<BatchDomain> selectReqBatchList(BatchDomain param);
	
	//배치 예정건 등록
	int insertBatchPlanInfo(BatchDomain batchDomain);
	
	// 2021-11-09 스케줄러 테이블 상태변경
	int updateSchedule(BatchDomain param);
	
	// 2021-11-09 스케쥴명으로 조회
	List<BatchDomain> selectBatchList(BatchDomain param);
	
	// 2021-11-09 가등록번호 update 
	int updatePreLcNum(NewApplyDomain newApplyDomain);
	
	// 2021-11-10 배치 시작 이력 저장
	int insertScheduleHist(BatchDomain param);
	
	// 2021-11-10 배치 종료 이력 저장
	void updateScheduleHist(BatchDomain param);
	
	// 2021-11-11 해지신청 리스트 조회
	List<NewApplyDomain> selectDropApplyList(BatchDomain param);
	
	// 2021-11-11 해지완료 후 상태변경
	int updateDropApply(NewApplyDomain newApplyDomain);
	
	// 2021-11-11 정보변경 완료 
	int updateLoanUsersInfo(NewApplyDomain newApplyDomain);
	
	
}
