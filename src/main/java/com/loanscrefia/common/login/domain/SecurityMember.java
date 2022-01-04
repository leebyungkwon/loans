package com.loanscrefia.common.login.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityMember extends User {
	private static final String ROLE_PREFIX = "ROLE_";
	private static final long serialVersionUID = 1L;
	
	public SecurityMember(MemberDomain memberDomain) {
		super(memberDomain.getMemberSeq().toString(), memberDomain.getPassword(), memberDomain.apprYnCheck(),true,true,memberDomain.failCntCheck(), makeGrantedAuthority(memberDomain.getRoleName()));
	}
	private static List<GrantedAuthority> makeGrantedAuthority(String roleName){
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority(ROLE_PREFIX + roleName));
		return list;
	}
	
}