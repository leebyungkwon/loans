package com.loanscrefia.admin.crefia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.crefia.repository.CrefiaRepository;
import com.loanscrefia.config.message.ResponseMsg;

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
	
	@Transactional
	public ResponseMsg saveCrefia(CrefiaDomain crefiaDomain) {
		int idCheck = crefiaRepository.getmemberId(crefiaDomain);
		if(idCheck > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이미 등록된 아이디 입니다.");
		}
		
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	crefiaDomain.setPassword(passwordEncoder.encode(crefiaDomain.getPassword()));
		
		if(crefiaDomain.getMemberSeq() > 0) {
			crefiaRepository.updateCrefia(crefiaDomain);
			return new ResponseMsg(HttpStatus.OK, "success", "수정되었습니다.");
		}else {
			crefiaRepository.insertCrefia(crefiaDomain);
			return new ResponseMsg(HttpStatus.OK, "success", "등록되었습니다.");
		}
	}
	
	@Transactional
	public ResponseMsg deleteCrefia(CrefiaDomain crefiaDomain) {
		crefiaRepository.deleteCrefia(crefiaDomain);
		return new ResponseMsg(HttpStatus.OK, "success", "삭제되었습니다.");
	}
	
	/* ========================================================================== */
	
	// 협회 관리자 관리 조회
	@Transactional(readOnly = true)
	public List<CrefiaDomain> selectCrefiaWorkMemberList(CrefiaDomain crefiaDomain){
		return crefiaRepository.selectCrefiaWorkMemberList(crefiaDomain);
	}
	
	// 협회 관리자 관리 조회
	@Transactional(readOnly = true)
	public List<CrefiaDomain> selectCrefiaWorkCompanyList(CrefiaDomain crefiaDomain){
		return crefiaRepository.selectCrefiaWorkCompanyList(crefiaDomain);
	}
	
}