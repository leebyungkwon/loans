package com.loanscrefia.common.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;


@Mapper 
public interface MemberRepository {
	MemberDomain findByEmail(String email);

	MemberDomain save(MemberDomain memberEntity);
	
	List<MemberRoleDomain> findRoles(MemberDomain memberDomain);

	
	long test(MemberDomain memberEntity);
	
}