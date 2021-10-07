package com.loanscrefia.admin.users.repository;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.admin.users.domain.UsersDomain;


@Mapper
public interface UsersRepository {
	List<UsersDomain> selectUsersList(UsersDomain usersDomain);
	int loginStopUpdate(UsersDomain usersDomain);
	int usersCorpApply(UsersDomain usersDomain);
	UsersDomain getUsersDetail(UsersDomain usersDomain);
	int updateBoInactiveUser(UsersDomain usersDomain);
	int deleteBoInactiveUser(UsersDomain usersDomain);
	
	List<UsersDomain> selectInactiveList(UsersDomain usersDomain);
	UsersDomain getInactiveDetail(UsersDomain usersDomain);
	
}
