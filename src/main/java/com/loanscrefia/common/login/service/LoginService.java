package com.loanscrefia.common.login.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;
import com.loanscrefia.common.member.repository.MemberRepository;
import com.loanscrefia.common.member.repository.MemberRoleRepository;

@Service
public class LoginService implements UserDetailsService {
    
	@Autowired private MemberRepository memberRepository;
	@Autowired private MemberRoleRepository memberRoleRepository;
    
    @Transactional
    public Long saveUser(MemberDomain memberEntity) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	memberEntity.setPassword(passwordEncoder.encode(memberEntity.getPassword()));

        return memberRepository.test(memberEntity);
    }

	/*
	 * public UserDetails loadUserByUsername(String email) throws
	 * UsernameNotFoundException { return
	 * Optional.ofNullable(memberRepository.findByEmail(email)).filter(member ->
	 * member != null) .map(member -> new SecurityMember((MemberDomain)
	 * member)).get(); }
	 */ 
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	MemberDomain mem = memberRepository.findByEmail(email);
    	List<MemberRoleDomain> role = memberRepository.findRoles(mem);
    	mem.setRoles(role);
		return 
			Optional.ofNullable(mem)
			.filter(member -> member!= null)
			.map(member -> new SecurityMember(mem)).get();
	}
	public void saveRole(MemberRoleDomain role) {
		role.setRoleName("MEMBER");
		memberRoleRepository.save(role);
	}

}