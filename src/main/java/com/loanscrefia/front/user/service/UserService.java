package com.loanscrefia.front.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.front.user.domain.UserDomain;
import com.loanscrefia.front.user.repository.UserRepository;

@Service
public class UserService {

	@Autowired private UserRepository userRepo;
	
	//모집인 조회 : 개인
	@Transactional(readOnly=true)
	public UserDomain selectIndvUserInfo(UserDomain userDomain) {
		return userRepo.selectIndvUserInfo(userDomain);
	}
	
	//모집인 조회 : 법인
	@Transactional(readOnly=true)
	public UserDomain selectCorpUserInfo(UserDomain userDomain) {
		return userRepo.selectCorpUserInfo(userDomain);
	}
	
	//모집인 상태 변경 : 자격취득
	@Transactional
	public int updatePlRegStat(UserDomain userDomain) {
		return userRepo.updatePlRegStat(userDomain);
	}
}
