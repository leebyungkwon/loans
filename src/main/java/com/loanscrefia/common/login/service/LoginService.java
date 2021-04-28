package com.loanscrefia.common.login.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.MemberRoleDomain;
import com.loanscrefia.common.member.repository.MemberRepository;
import com.loanscrefia.common.member.repository.MemberRoleRepository;

@Service
public class LoginService implements UserDetailsService {
    
	@Autowired private MemberRepository memberRepository;
	@Autowired private MemberRoleRepository memberRoleRepository;
    
    @Transactional
    public SignupDomain saveUser(SignupDomain signupDomain) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	signupDomain.setPassword(passwordEncoder.encode(signupDomain.getPassword()));
        return memberRepository.save(signupDomain);
    }


    // security 로그인
    public UserDetails loadUserByUsername(@Valid String memberId) throws UsernameNotFoundException {
    	MemberDomain mem = memberRepository.findById(memberId);
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
	
	
	@Transactional
	public void loginFailCnt(String memberId) {
		memberRepository.loginFailCnt(memberId);
	}
	
	public SignupDomain getMember(SignupDomain signupDomain) {
		return memberRepository.getMember(signupDomain);
	}

}