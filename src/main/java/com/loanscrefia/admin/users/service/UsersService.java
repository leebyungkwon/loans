package com.loanscrefia.admin.users.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.repository.UsersRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.user.domain.UserDomain;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	// 회원관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectUsersList(UsersDomain usersDomain){
		return usersRepository.selectUsersList(usersDomain);
	}
	
	
	// 회원관리 상세
	@Transactional(readOnly=true)
	public UsersDomain getUsersDetail(UsersDomain usersDomain){
		return usersRepository.getUsersDetail(usersDomain);
	}
	
	// 로그인 차단 해제
	@Transactional
	public ResponseMsg loginStopUpdate(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int[] userSeqArr 	= usersDomain.getUserSeqArr();
		for(int i = 0; i < userSeqArr.length; i++) {
			UsersDomain resultDomain = new UsersDomain();
			resultDomain.setUserSeq(userSeqArr[i]);
			usersRepository.loginStopUpdate(resultDomain);
		}
		
		return responseMsg;
	}
}
