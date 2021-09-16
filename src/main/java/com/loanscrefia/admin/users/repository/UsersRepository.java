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
}
