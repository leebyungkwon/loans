package com.loanscrefia.member.user.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.user.domain.UserDomain;

@Mapper
public interface UserRepository {
	
	//모집인 등록 > 리스트
	List<UserDomain> selectUserRegList(UserDomain userDomain);
	
	//모집인 등록 > 개인 등록
	int insertUserRegIndvInfoByExcel(UserDomain userDomain);
	
	//모집인 등록 > 법인 등록
	int insertUserRegCorpInfoByExcel(UserDomain userDomain);
	
	//모집인 등록 > 승인요청
	int updatePlRegStat(UserDomain userDomain);
	
	//모집인 등록 > 상세
	UserDomain getUserRegDetail(UserDomain userDomain);
	
	//모집인 등록 > 수정
	int updateUserRegInfo(UserDomain userDomain);

}
