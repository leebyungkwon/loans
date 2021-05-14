package com.loanscrefia.admin.corp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.repository.CorpRepository;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class CorpService {

	@Autowired private CorpRepository corpRepo;
	
	//법인 리스트
	@Transactional(readOnly=true)
	public List<CorpDomain> selectCorpList(CorpDomain corpDomain) {
		return corpRepo.selectCorpList(corpDomain);
	}
	
	//법인 저장
	@Transactional
	public ResponseMsg saveCorpInfo(CorpDomain corpDomain){
		
		int result = 0;
		
		if(corpDomain.getCorpSeq() == null) {
			//저장
			result = corpRepo.insertCorpInfo(corpDomain);
		}else {
			//수정
			result = corpRepo.updateCorpInfo(corpDomain);
		}
		
		//결과
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//법인 상세
	@Transactional(readOnly=true)
	public CorpDomain getCorpInfo(CorpDomain corpDomain) {
		return corpRepo.getCorpInfo(corpDomain);
	}
	
	
}
