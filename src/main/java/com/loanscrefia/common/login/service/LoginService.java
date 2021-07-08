package com.loanscrefia.common.login.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.repository.MemberRepository;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class LoginService implements UserDetailsService {
    
	@Autowired private MemberRepository memberRepository;

    // security 로그인
    public UserDetails loadUserByUsername(@Valid String memberId) throws UsernameNotFoundException {
    	MemberDomain mem = memberRepository.findById(memberId);
		return 
			Optional.ofNullable(mem)
			.filter(member -> member!= null)
			.map(member -> new SecurityMember(mem)).get();
	}
	
    // 로그인 실패 횟수 증가
	@Transactional
	public void loginFailCnt(MemberDomain memberDomain) {
		memberRepository.loginFailCnt(memberDomain);
	}
	
	// 아이디 중복체크
	@Transactional
	public int idCheck(SignupDomain signupDomain) {
	 	return memberRepository.idCheck(signupDomain);
	}
	
	@Transactional
	public ResponseMsg insertSignup(SignupDomain signupDomain) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	signupDomain.setPassword(passwordEncoder.encode(signupDomain.getPassword()));
    	
    	int count = this.idCheck(signupDomain);
    	
    	if(count > 0) {
    		return new ResponseMsg(HttpStatus.OK, null, 1, "success");
    	}
    	
    	memberRepository.insertSignup(signupDomain);
		return new ResponseMsg(HttpStatus.OK, null, 0, "success");
	}

}