package com.loanscrefia.admin.users.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.repository.UsersRepository;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.user.domain.UserDomain;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	//이메일 적용여부
	@Value("${email.apply}")
	public boolean emailApply;
	
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
	
	// 회원관리 법인 승인처리
	@Transactional
	public ResponseMsg usersCorpApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int result = usersRepository.usersCorpApply(usersDomain);
		if(result <= 0) {
			responseMsg.setMessage("실패하였습니다");
			responseMsg.setCode("fail");
			return responseMsg;
		}else {
			// 2021-09-16 법인회원 승인시 이메일 발송 여부 확인
			
			/*
			
			if(emailApply) {
				UsersDomain usersResult = usersRepository.getUsersDetail(usersDomain);
				int emailResult = 0;
				EmailDomain emailDomain = new EmailDomain();
				emailDomain.setName("여신금융협회");
				emailDomain.setEmail(usersDomain.getEmail());
				emailDomain.setInstId("139");
				emailDomain.setSubsValue(usersResult.getUserId());
				emailResult = emailRepository.sendEmail(emailDomain);
				if(emailResult == 0) {
					responseMsg.setMessage("메일발송에 실패하였습니다");
					responseMsg.setCode("fail");
					return responseMsg;
				}
			}
			
			*/
		}
		return responseMsg;
	}
	

	
	
	
	
	
	
	
	
	
	
	// 휴면회원관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectInactiveList(UsersDomain usersDomain){
		return usersRepository.selectInactiveList(usersDomain);
	}
	
	// 휴면회원관리 상세
	@Transactional(readOnly=true)
	public UsersDomain getInactiveDetail(UsersDomain usersDomain){
		return usersRepository.getInactiveDetail(usersDomain);
	}
	
	// 휴면회원 활성화
	@Transactional
	public ResponseMsg boInactiveUser(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int[] userSeqArr 	= usersDomain.getUserSeqArr();
		for(int i = 0; i < userSeqArr.length; i++) {
			UsersDomain resultDomain = new UsersDomain();
			resultDomain.setUserSeq(userSeqArr[i]);
			usersRepository.updateBoInactiveUser(resultDomain);
			usersRepository.deleteBoInactiveUser(resultDomain);
		}
		return responseMsg;
	}
	
}
