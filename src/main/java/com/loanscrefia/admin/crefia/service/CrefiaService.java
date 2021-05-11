package com.loanscrefia.admin.crefia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.crefia.repository.CrefiaRepository;

@Service
public class CrefiaService {

	@Autowired 
	private CrefiaRepository crefiaRepository;
	
	// 협회 관리자 관리 조회
	@Transactional(readOnly = true)
	public List<CrefiaDomain> selectCrefiaList(CrefiaDomain crefiaDomain){
		return crefiaRepository.selectCrefiaList(crefiaDomain);
	}
	
	public CrefiaDomain crefiaDetail(CrefiaDomain crefiaDomain) {
		return crefiaRepository.crefiaDetail(crefiaDomain);
	}
	
	
}