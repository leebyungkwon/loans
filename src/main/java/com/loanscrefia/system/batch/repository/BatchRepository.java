package com.loanscrefia.system.batch.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
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
	
	// 금일 토큰 생성 확인
	int getTokenCheck(BatchDomain param);
	
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
	
	// 2021-11-19 주민등록번호, ci 수정
	int updateUsersSsnInfo(UsersDomain usersDomain);

	// 2021-11-19 계약별 주민등록번호, ci 수정
	int updateMasSsnInfo(NewApplyDomain newApplyDomain);
	
	
	
	// 개인정보수정 및 주민등록번호 수정 완료 카운트
	int selectReqSsnInfoCnt(BatchDomain batchDomain);
	
	
	// 계약 해지완료 상태 변경
	int updateDropMasInfo(NewApplyDomain newApplyDomain);
	
	//배치 예정건 삭제
	int deleteBatchPlanInfo(BatchDomain batchDomain);
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 2021-12-07 정보 삭제 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//해지정보 리스트
	List<NewApplyDomain> selectRegCancelInfoList(NewApplyDomain newApplyDomain);
	
	//승인요청 하지 않고 저장만 하고 있는 정보 리스트 : 미요청 건
	List<NewApplyDomain> selectNotApplyInfoList(NewApplyDomain newApplyDomain);
	
	//승인요청 하지 않고 저장만 하고 있는 정보 리스트 : 보완요청 건
	List<NewApplyDomain> selectNotApplyAgainInfoList(NewApplyDomain newApplyDomain);
	
	//승인완료건 취소정보 리스트
	List<NewApplyDomain> selectCancelInfoList(NewApplyDomain newApplyDomain);
	
	//등록요청 거절정보 리스트
	List<NewApplyDomain> selectRejectInfoList(NewApplyDomain newApplyDomain);
	
	//정보 조회
	NewApplyDomain getMasInfo(NewApplyDomain newApplyDomain);
	
	//마스터 테이블 정보 삭제
	int realDeleteMasInfo(NewApplyDomain newApplyDomain);
	
	//마스터 이력 테이블 정보 삭제
	int realDeleteMasHistInfo(NewApplyDomain newApplyDomain);
	
	//단계별 이력 정보 삭제
	int realDeleteMasStepInfo(NewApplyDomain newApplyDomain);
	
	//금융상품유형 상세 정보 삭제
	int realDeletePrdDtlInfo(NewApplyDomain newApplyDomain);
	
	//법인 임원 삭제 리스트
	List<ApplyImwonDomain> selectCorpImwonDelInfoList(ApplyImwonDomain applyImwonDomain);
	
	//법인 업무수행인력 삭제 리스트
	List<ApplyExpertDomain> selectCorpExpertDelInfoList(ApplyExpertDomain applyExpertDomain);
	
	//법인 전산인력 삭제 리스트
	List<ApplyItDomain> selectCorpItDelInfoList(ApplyItDomain applyItDomain);
	
	//법인 임원 삭제
	int realDeleteCorpImwonInfo(ApplyImwonDomain applyImwonDomain);
	
	//법인 업무수행인력 삭제
	int realDeleteCorpExpertInfo(ApplyExpertDomain applyExpertDomain);
	
	//법인 전산인력 삭제
	int realDeleteCorpItInfo(ApplyItDomain applyItDomain);
	
	
	
	
	
	
	
	
	
	
	
	
}







