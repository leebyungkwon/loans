package com.loanscrefia.common.main.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.loanscrefia.common.main.domain.MainDomain;
import com.loanscrefia.common.main.repository.MainRepository;
import com.loanscrefia.common.member.domain.MemberDomain;

@Service
public class MainService{
    
	@Autowired 
	private MainRepository mainRepository;
	
	//대쉬보드 조회
	@Transactional(readOnly=true)
	public MainDomain selectDashBoard(MainDomain mainDomain){
		
		//세션 체크
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		if(loginInfo != null) {
			mainDomain.setCreYn(loginInfo.getCreYn());
			mainDomain.setCreGrp(loginInfo.getCreGrp());
		}
		
		MainDomain dashBoard = mainRepository.selectDashBoard(mainDomain);
		
		return dashBoard;
	}

}