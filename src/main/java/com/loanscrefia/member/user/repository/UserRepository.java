package com.loanscrefia.member.user.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;

@Mapper
public interface UserRepository {
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 조회 및 변경 > 리스트
	List<UserDomain> selectUserConfirmList(UserDomain userDomain);
	
	//모집인 조회 및 변경 > 법인사용인 존재유무
	int selectCorpUserCnt(UserDomain userDomain);
	
	//모집인 조회 및 변경 > 법인사용인 리스트
	List<UserDomain> selectCorpUserList(UserDomain userDomain);
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 등록 > 리스트
	List<UserDomain> selectUserRegList(UserDomain userDomain);
	
	//모집인 중복체크
	int userRegDupChk(UserDomain userDomain);
	
	//모집인 등록 > 개인 등록
	int insertUserRegIndvInfoByExcel(UserDomain userDomain);
	
	//모집인 등록 > 법인 등록
	int insertUserRegCorpInfoByExcel(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 등록
	int insertUserRegCorpImwonInfoByExcel(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 등록
	int insertUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 등록
	int insertUserRegCorpExpertInfoByExcel(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 등록
	int insertUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 정보 등록
	int insertUserRegCorpItInfoByExcel(UserItDomain userItDomain);
	
	//모집인 등록 > 법인 : 전산인력 정보 등록
	int insertUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 > 상세
	UserDomain getUserRegDetail(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 리스트
	List<UserImwonDomain> selectUserRegCorpImwonList(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 상세
	UserImwonDomain getUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 리스트
	List<UserExpertDomain> selectUserRegCorpExpertList(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전문인력 상세
	UserExpertDomain getUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 리스트
	List<UserItDomain> selectUserRegCorpItList(UserItDomain userItDomain);
	
	//모집인 등록 > 법인 : 전산인력 상세
	UserItDomain getUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 > 수정
	int updateUserRegInfo(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 수정
	int updateUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 수정
	int updateUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 정보 수정
	int updateUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 : 모집인 정보 삭제
	int deleteUserRegInfo(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 삭제
	int deleteUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 삭제
	int deleteUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 삭제
	int deleteUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 삭제(masterSeq 사용)
	//int deleteUserRegCorpImwonInfoByMasterSeq(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 삭제(masterSeq 사용)
	//int deleteUserRegCorpExpertInfoByMasterSeq(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 삭제(masterSeq 사용)
	//int deleteUserRegCorpItInfoByMasterSeq(UserItDomain userItDomain);

	/* -------------------------------------------------------------------------------------------------------
	 * 모집인 상태 / 처리상태 변경 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	int updateUserStat(UserDomain userDomain);
	
	//법인 승인여부 체크
	int corpStatCheck(UserDomain userDomain);
	
	/* -------------------------------------------------------------------------------------------------------
	 * 모집인 위반이력 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//위반이력 삭제
	int deleteUserViolationInfo(UserDomain userDomain);
	
	//위반이력 삭제요청
	int applyDeleteUserViolationInfo(UserDomain userDomain);
	
	//위반이력 저장
	int insertUserViolationInfo(UserDomain userDomain);
	
	//위반이력 리스트
	List<UserDomain> selectUserViolationInfoList(UserDomain userDomain);
	
	//위반이력 수정
	int updateUserViolationInfo(UserDomain userDomain);
	
	/* -------------------------------------------------------------------------------------------------------
	 * 모집인 기본 이력 / 단계별 이력 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 기본 이력 저장
	int insertUserHistory(UserDomain userDomain);
	
	//모집인 단계별 이력 저장
	int insertUserStepHistory(UserDomain userDomain);
	
	//모집인 단계별 이력 리스트
	List<UserDomain> selectUserStepHistoryList(UserDomain userDomain);
	
	//가등록번호로 본등록번호 등록 배치
	List<UserDomain> selectApiBatchList(UserDomain userDomain);
	
	//가등록번호로 본등록번호 수동등록
	UserDomain getApiReg(UserDomain userDomain);
	
	//승인완료인 건 결제정보가 있을시 자격취득으로 변경 - 개인
	List<UserDomain> selectIndvApiApplyList(UserDomain userDomain);
	
	//승인완료인 건 결제정보가 있을시 자격취득으로 변경 - 법인
	List<UserDomain> selectCorpApiApplyList(UserDomain userDomain);	
	
	//승인완료이면서 기등록자인 경우 전체 자격취득
	List<UserDomain> selectPreRegYnApiApplyList(UserDomain userDomain);
	
	//등록번호가 있는데 계약번호가 없는 케이스
	List<UserDomain> selectApiConNumList(UserDomain userDomain);	
	
	//TM 계약 리스트(결제한 내역이 있을경우)
	List<UserDomain> selectPayCompTmContract(UserDomain userDomain);
}
