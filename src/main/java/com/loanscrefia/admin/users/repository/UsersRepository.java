package com.loanscrefia.admin.users.repository;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.member.user.domain.NewUserDomain;


@Mapper
public interface UsersRepository {
	
	// 개인회원 조회
	List<UsersDomain> selectIndvUsersList(UsersDomain usersDomain);
	
	// 개인회원 상세조회
	UsersDomain getIndvUsersDetail(UsersDomain usersDomain);
	
	// 법인회원 조회
	List<UsersDomain> selectCorpUsersList(UsersDomain usersDomain);
	
	// 법인회원 상세조회
	UsersDomain getCorpUsersDetail(UsersDomain usersDomain);
	
	// 로그인 잠금 해제
	int loginStopUpdate(UsersDomain usersDomain);
	
	// 법인회원 승인처리
	int usersCorpApply(UsersDomain usersDomain);
	
	// 휴면회원 활성화
	int updateBoInactiveUser(UsersDomain usersDomain);
	
	// 휴면회원 활성화 후 삭제
	int deleteBoInactiveUser(UsersDomain usersDomain);
	
	// 휴면회원 조회
	List<UsersDomain> selectInactiveList(UsersDomain usersDomain);
	
	// 휴면회원 상세조회
	UsersDomain getInactiveDetail(UsersDomain usersDomain);
	
	// 개인회원 결격요건 엑셀 업로드
	int indvUsersDisExcelUpload(UsersDomain usersDomain);
	
	// 법인회원 결격요건 엑셀 업로드
	int corpUsersDisExcelUpload(UsersDomain usersDomain);
	
	// 개인회원 결격요건 수정
	int updateIndvUserDis(UsersDomain usersDomain);
	
	// 법인회원 결격요건 수정
	int updateCorpUserDis(UsersDomain usersDomain);

	// 금융감독원 승인여부 수정
	int updatePassYn(UsersDomain usersDomain);
	
	// 2021-11-04 주민등록번호로 개인정보 조회
	UsersDomain getUsersDetailByZId(UsersDomain usersDomain);
	
	// 2021-11-04 회원 결격요건 상세 조회
	List<UsersDomain> selectUsersDisList(UsersDomain usersDomain);	
	
	// 2021-11-08 개인회원 정보변경관리 리스트 조회
	List<UsersDomain> selectUpdateIndvUsersList(UsersDomain usersDomain);
	
	// 2021-11-08 개인회원 정보변경관리 상세
	UsersDomain getUpdateIndvUsersDetail(UsersDomain usersDomain);
	
	// 2021-11-08 법인회원 정보변경관리 리스트 조회
	List<UsersDomain> selectUpdateCorpUsersList(UsersDomain usersDomain);
	
	// 2021-11-08 법인회원 정보변경관리 상세
	UsersDomain getUpdateCorpUsersDetail(UsersDomain usersDomain);
	
	// 2021-11-08 개인회원 정보변경관리 상태변경
	int updateIndvUsersStat(UsersDomain usersDomain);

	// 2021-11-08 법인회원 정보변경관리 상태변경
	int updateCorpUsersStat(UsersDomain usersDomain);
	
	// 2021-11-04 개인회원정보 조회 리스트
	List<NewApplyDomain> selectUserSeqIndvList(NewApplyDomain newApplyDomain);
	
	// 2021-11-04 법인회원정보 조회 리스트
	List<NewApplyDomain> selectUserSeqCorpList(NewApplyDomain newApplyDomain);
	
	// 2021-11-22 개인 정보변경 승인 후 user테이블 정보 변경
	int updateIndvUsersApplyInfo(UsersDomain usersDomain);
	
	// 2021-11-22 법인 정보변경 승인 후 user테이블 정보 변경
	int updateCorpUsersApplyInfo(UsersDomain usersDomain);
	
	// 2021-11-22 법인 정보변경 승인 후 corp테이블 정보 변경
	int updateCorpApplyInfo(UsersDomain usersDomain);
	
	
	
}
