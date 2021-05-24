package com.loanscrefia.front.user.repository;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.front.user.domain.UserDomain;

@Mapper
public interface UserRepository {

	//모집인 조회 : 개인
	UserDomain selectIndvUserInfo(UserDomain userDomain);
	
	//모집인 조회 : 법인
	UserDomain selectCorpUserInfo(UserDomain userDomain);
	
	//모집인 상태 변경 : 자격취득
	int updatePlRegStat(UserDomain userDomain);
}
