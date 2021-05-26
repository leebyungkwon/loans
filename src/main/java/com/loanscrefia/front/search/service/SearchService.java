package com.loanscrefia.front.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.config.message.ResponseMsg;
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
	public ResponseMsg selectUserInfo(SearchDomain searchDomain) {
		if(searchDomain.getPlClass() == null || searchDomain.getPlClass().equals("")) {
			return new ResponseMsg(HttpStatus.OK, "fail", "모집인 분류값이 없습니다.");
		}
		if(searchDomain.getPlClass().equals("1")) {
			return new ResponseMsg(HttpStatus.OK, "", selectIndvUserInfo(searchDomain));
		}else if(searchDomain.getPlClass().equals("2")) {
			return new ResponseMsg(HttpStatus.OK, "", selectCorpUserInfo(searchDomain));
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패");
	}
	
	//모집인 상태 변경 : 자격취득
	@Transactional
	public int updatePlRegStat(SearchDomain searchDomain) {
		return searchRepo.updatePlRegStat(searchDomain);
	}
}
