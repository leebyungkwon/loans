package com.loanscrefia.system.batch.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.system.batch.domain.BatchDomain;

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
	
	// 2021-11-09 계약정보(가등록번호)
	int updatePreLcNum(NewApplyDomain newApplyDomain);
	
	// 2021-11-15 계약정보(등록번호, 계약번호)
	int updateLcNum(NewApplyDomain newApplyDomain);
	
	// 2021-11-10 배치 시작 이력 저장
	int insertScheduleHist(BatchDomain param);
	
	// 2021-11-10 배치 종료 이력 저장
	void updateScheduleHist(BatchDomain param);
	
	// 2021-11-11 해지신청 리스트 조회
	List<NewApplyDomain> selectDropApplyList(BatchDomain param);
	
	// 2021-11-11 해지완료 후 상태변경
	int updateDropApply(NewApplyDomain newApplyDomain);
	
	// 2021-11-11 개인회원 계약정보변경 완료 
	int updateIndvMasInfo(NewApplyDomain newApplyDomain);
	
	// 2021-11-11 개인회원 건별 계약정보변경 완료 
	int updateCaseIndvMasInfo(NewApplyDomain newApplyDomain);	
	
	// 2021-11-11 법인회원 계약정보변경 완료	
	int updateCorpMasInfo(NewApplyDomain newApplyDomain);
	
	// 2021-11-11 법인회원 건별 계약정보변경 완료	
	int updateCaseCorpMasInfo(NewApplyDomain newApplyDomain);
	
	// 2021-11-11 개인회원 회원정보 수정
	int updateIndvUsersInfo(UsersDomain usersDomain);
	
	// 2021-11-11 법인회원 회원정보 수정
	int updateCorpUsersInfo(UsersDomain usersDomain);	
	
	// 2021-11-11 법인정보 수정
	int updateCorpInfo(UsersDomain usersDomain);	
	
	
	// 2021-11-11 위반이력 등록
	int updateUserViolationInfo(NewUserDomain newUserDomain);
	
	// 2021-11-12 위반이력 삭제
	int deleteUserViolationInfo(NewUserDomain newUserDomain);
	
	
	// 2021-11-15 가등록 삭제
	int deletePreLcNum(NewApplyDomain newApplyDomain);

	void deleteShedLock();

	List<BatchDomain> selectAllBatchList();

	List<BatchDomain> selectBatchErrList(BatchDomain param);

	List<BatchDomain> selectBatchErrHistList(BatchDomain batch);
	
	
	// 개인정보수정시 해당 사용자의 전체 데이터 완료 체크 카운트
	int selectReqCnt(BatchDomain batchDomain);
	
	// 2021-11-18 개인정보 변경신청 테이블 상태 변경
	int updateIndvReq(UsersDomain usersDomain);	
	
	// 2021-11-18 법인정보 변경신청 테이블 상태 변경
	int updateCorpReq(UsersDomain usersDomain);	
	
	int refreshBatch(BatchDomain batch);
	
	
	
}







