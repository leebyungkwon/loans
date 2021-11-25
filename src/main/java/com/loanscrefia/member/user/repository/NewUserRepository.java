package com.loanscrefia.member.user.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.ProductDtlDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;

@Mapper
public interface NewUserRepository {
	
	
	// 2021-10-12 고도화 - 모집인 확인처리 리스트(회원사)
	List<NewUserDomain> selectNewUserRegList(NewUserDomain newUserDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 개인 조회
	NewUserDomain getNewUserRegDetail(NewUserDomain newUserDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 위반이력 조회
	List<NewUserDomain> selectNewUserViolationInfoList(NewUserDomain newUserDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 : 대표자 및 임원 리스트
	List<UserImwonDomain> selectNewUserRegCorpImwonList(UserImwonDomain userImwonDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 : 대표자 및 임원 상세
	UserImwonDomain getNewUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 : 전문인력 리스트
	List<UserExpertDomain> selectNewUserRegCorpExpertList(UserExpertDomain userExpertDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 : 전문인력 상세
	UserExpertDomain getNewUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 : 전산인력 리스트
	List<UserItDomain> selectNewUserRegCorpItList(UserItDomain userItDomain);
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 : 전산인력 상세
	UserItDomain getNewUserRegCorpItInfo(UserItDomain userItDomain);
	
	// 2021-10-12 고도화 - 모집인 기본 이력 저장
	int insertNewUserHistory(NewUserDomain newUserDomain);
	
	// 2021-10-12 고도화 - 모집인 단계별 이력 저장
	int insertNewMasterStep(NewUserDomain newUserDomain);
	
	// 2021-10-12 모집인 상태변경	
	int newUserApply(NewUserDomain newUserDomain);

	// 2021-10-12 고도화 - 모집인 조회 및 해지 - 해지요청
	int newUserDropApply(NewUserDomain newUserDomain);
	
	// 2021-10-25 고도화 - 모집인 조회 및 해지 - 해지요청취소
	int newUserDropApplyCancel(NewUserDomain newUserDomain);
	
	
	// 2021-10-12 고도화 - 모집인 확인처리 리스트(회원사)
	List<NewUserDomain> selectNewConfirmList(NewUserDomain newUserDomain);
	
	//위반이력 삭제
	int newDeleteNewUserViolationInfo(NewUserDomain userDomain);
	
	//위반이력 삭제요청
	int newApplyDeleteViolationInfo(NewUserDomain userDomain);
	
	//위반이력 저장
	int insertNewUserViolationInfo(NewUserDomain userDomain);
	
	//위반이력 수정
	int updateNewUserViolationInfo(NewUserDomain userDomain);
	
	// 2021-10-25 금융상품세부내용 리스트 조회
	List<ProductDtlDomain> selectPlProductDetailList(NewUserDomain newUserDomain);
	
	// 2021-11-24 배치 등록해야 할 위반이력 
	List<NewUserDomain> selectNewUserInsertViolationInfoList(NewUserDomain newUserDomain);
	
	
}
