package com.loanscrefia.front.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.pay.service.PayService;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.repository.SearchRepository;

@Service
public class SearchService {

	@Autowired private SearchRepository searchRepo;
	
	//모집인 조회 : 개인
	@Transactional(readOnly = true)
	public SearchDomain selectIndvUserInfo(SearchDomain searchDomain) {
		return searchRepo.selectIndvUserInfo(searchDomain);
	}
	
	//모집인 조회 : 법인
	@Transactional(readOnly = true)
	public SearchDomain selectCorpUserInfo(SearchDomain searchDomain) {
		return searchRepo.selectCorpUserInfo(searchDomain);
	}
	
	//모집인 조회
	@Transactional(readOnly=true)
	public SearchDomain selectSearchUserInfo(SearchDomain searchDomain) {
		return searchRepo.selectSearchUserInfo(searchDomain);
	}
	
	//모집인 상태 변경 : 자격취득
	@Transactional
	public int updatePlRegStat(SearchDomain searchDomain) {
		return searchRepo.updatePlRegStat(searchDomain);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
