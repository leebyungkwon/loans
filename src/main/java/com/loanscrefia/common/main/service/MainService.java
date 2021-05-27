package com.loanscrefia.common.main.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.loanscrefia.common.main.domain.MainDomain;
import com.loanscrefia.common.main.repository.MainRepository;

@Service
public class MainService{
    
	@Autowired 
	private MainRepository mainRepository;
	
	//모집인 조회 및 변경 > 리스트
	@Transactional(readOnly=true)
	public MainDomain selectDashBoard(MainDomain mainDomain){
		return mainRepository.selectDashBoard(mainDomain);
	}

}