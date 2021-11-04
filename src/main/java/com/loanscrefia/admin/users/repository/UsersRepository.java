package com.loanscrefia.admin.users.repository;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.admin.users.domain.UsersDomain;


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
	
	
	
	
}
