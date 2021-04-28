package com.loanscrefia.common.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;


@Mapper 
public interface MemberRepository {
	
	// 로그인ID
	MemberDomain findById(String memberId);
	
	// 권한
	List<MemberRoleDomain> findRoles(MemberDomain memberDomain);
	
	// 로그인 실패횟수 증가
	void loginFailCnt(String memberId);
	
	
	
	
	
	
	
	// 회원정보 저장
	SignupDomain save(SignupDomain signupDomain);
	
	// 회원정보 조회
	SignupDomain getMember(SignupDomain signupDomain);
	
}