package com.loanscrefia.common.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;


@Mapper
public interface MemberRoleRepository{

	void save(MemberRoleDomain role);
	
	List<MemberRoleDomain> findRoles(SignupDomain signupDomain);

}