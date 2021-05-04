package com.loanscrefia.common.member.repository;


import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberDomain;


@Mapper 
public interface MemberRepository {
	
	// 로그인ID(security)
	MemberDomain findById(String memberId);
	
	// 로그인 실패횟수 증가
	void loginFailCnt(MemberDomain memberDomain);
	
	// 회원가입
	SignupDomain insertSignup(SignupDomain signupDomain);

	// 아이디 중복 체크
	int idCheck(SignupDomain signupDomain);
	
}