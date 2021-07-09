package com.loanscrefia.common.login.service;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.login.domain.SecurityMember;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.repository.MemberRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.util.UtilFile;

@Service
public class LoginService implements UserDetailsService {
    
	@Autowired private MemberRepository memberRepository;
	@Autowired private UtilFile utilFile;

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
	public ResponseMsg insertSignup(MultipartFile[] files, SignupDomain signupDomain) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	signupDomain.setPassword(passwordEncoder.encode(signupDomain.getPassword()));
    
    	int count = this.idCheck(signupDomain);
    	
    	if(count > 0) {
    		return new ResponseMsg(HttpStatus.OK, null, 1, "success");
    	}
    	
    	Map<String, Object> ret = utilFile.setPath("signup") 
				.setFiles(files)
				.setExt("all") 
				.upload();
    	
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				signupDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, null, ret.get("message"), "success");
		}

    	// 이메일 전체 소문자 변경
    	signupDomain.setEmail(signupDomain.getEmail().toLowerCase());
    	memberRepository.insertSignup(signupDomain);
		return new ResponseMsg(HttpStatus.OK, null, 0, "success");
	}

}