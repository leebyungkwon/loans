package com.loanscrefia.member.user.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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
	
	//모집인 조회 및 변경 > 
	//모집인 조회 및 변경 > 
	//모집인 조회 및 변경 > 
	//모집인 조회 및 변경 > 
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 등록 > 리스트
	List<UserDomain> selectUserRegList(UserDomain userDomain);
	
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
	
	//모집인 등록 > 법인 : 대표자 및 임원 상세
	List<UserImwonDomain> selectUserRegCorpImwonList(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 상세
	List<UserExpertDomain> selectUserRegCorpExpertList(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 상세
	List<UserItDomain> selectUserRegCorpItList(UserItDomain userItDomain);
	
	//모집인 등록 > 수정
	int updateUserRegInfo(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 수정
	int updateUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 수정
	int updateUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 수정
	int updateUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 삭제
	int deleteUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 삭제
	int deleteUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 삭제
	int deleteUserRegCorpItInfo(UserItDomain userItDomain);

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
	
	//위반이력 저장
	int insertUserViolationInfo(UserDomain userDomain);
	
	//위반이력 리스트
	List<UserDomain> selectUserViolationInfoList(UserDomain userDomain);
	
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
	
}
