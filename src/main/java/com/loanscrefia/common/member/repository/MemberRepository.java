package com.loanscrefia.common.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;


@Mapper 
public interface MemberRepository {
	// email조회(id조회)
	MemberDomain findByEmail(String email);
	
	// 회원정보 저장
	MemberDomain save(MemberDomain memberEntity);
	
	// 회원권한정보 저장
	List<MemberRoleDomain> findRoles(MemberDomain memberDomain);
	
	// 로그인 실패횟수 증가
	void loginFailCnt(String email);
	
	// 회원정보 조회
	MemberDomain getMember(MemberDomain memberDomain);
	
}