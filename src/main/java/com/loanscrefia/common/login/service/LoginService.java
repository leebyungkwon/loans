package com.loanscrefia.common.login.service;

import java.util.List;
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
import com.loanscrefia.common.member.domain.MemberRoleDomain;
import com.loanscrefia.common.member.repository.MemberRepository;
import com.loanscrefia.common.member.repository.MemberRoleRepository;
import com.loanscrefia.config.CryptoUtil;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.code.domain.CodeMstDomain;

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
	public int idCheck(SignupDomain signupDomain) throws Exception {
	 	return memberRepository.idCheck(signupDomain);
	}
	
	@Transactional
	public ResponseMsg insertSignup(SignupDomain signupDomain){
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	signupDomain.setPassword(passwordEncoder.encode(signupDomain.getPassword()));
    	//String encTarget = CryptoUtil.encrypt(signupDomain.getDeptNm()); // 암호화
    	//signupDomain.setDeptNm(encTarget);
    	memberRepository.insertSignup(signupDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "회원가입 신청이 완료되었습니다. \n승인 후에 로그인 가능합니다.");
	}

}