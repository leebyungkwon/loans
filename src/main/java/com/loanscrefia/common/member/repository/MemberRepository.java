package com.loanscrefia.common.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;


@Mapper 
public interface MemberRepository {
	// email조회(id조회)
	SignupDomain findByEmail(String email);
	
	// 회원정보 저장
	SignupDomain save(SignupDomain signupDomain);
	
	// 회원권한정보 저장
	List<MemberRoleDomain> findRoles(SignupDomain signupDomain);
	
	// 로그인 실패횟수 증가
	void loginFailCnt(String email);
	
	// 회원정보 조회
	SignupDomain getMember(SignupDomain signupDomain);
	
}