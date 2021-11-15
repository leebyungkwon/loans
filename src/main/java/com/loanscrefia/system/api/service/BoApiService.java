package com.loanscrefia.system.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;

@Service
public class BoApiService {

	@Autowired 
	private KfbApiRepository kfbApiRepository;
	
	// API 리스트 조회
	@Transactional(readOnly=true)
	public List<KfbApiDomain> selectApiList(KfbApiDomain kfbApiDomain){
		return kfbApiRepository.selectApiList(kfbApiDomain);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
