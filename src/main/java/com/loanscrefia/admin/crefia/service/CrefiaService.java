package com.loanscrefia.admin.crefia.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.crefia.repository.CrefiaRepository;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class CrefiaService {

	@Autowired private CrefiaRepository crefiaRepository;
	@Autowired private CommonService commonService;
	
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
		
		if(StringUtils.isEmpty(crefiaDomain.getPassword()) || StringUtils.isEmpty(crefiaDomain.getPasswordChk())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호를 입력해 주세요.");
		}else {
			String pwdChkResultMsg = commonService.pwdValidation(crefiaDomain.getMemberId(),crefiaDomain.getPassword(),crefiaDomain.getPasswordChk());
			
			if(StringUtils.isNotEmpty(pwdChkResultMsg)) {
				return new ResponseMsg(HttpStatus.OK, "fail", pwdChkResultMsg);
			}
		}
		
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
	public ResponseMsg updCrefia(CrefiaDomain crefiaDomain, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		MemberDomain loginInfo = (MemberDomain)session.getAttribute("member");
		
		if(loginInfo == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "로그인이 필요합니다.");
		}
		if(crefiaDomain.getMemberSeq() <= 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "필수값이 누락되었습니다. 새로고침 후 다시 시도해 주세요.");
		}
		
		//상세
		CrefiaDomain detail = this.crefiaDetail(crefiaDomain);
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		
		if(loginInfo.getCreGrp().equals("1")) { //로그인 계정이 실무자 권한일 때
			if(loginInfo.getMemberSeq() != detail.getMemberSeq()) {
				return new ResponseMsg(HttpStatus.OK, "fail", "[E1]수정 권한이 없습니다.");
			}
			if(!detail.getCreGrp().equals(crefiaDomain.getCreGrp())) {
				return new ResponseMsg(HttpStatus.OK, "fail", "[E2]수정 권한이 없습니다.");
			}
		}
		if(StringUtils.isEmpty(crefiaDomain.getOldPassword())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "현재 비밀번호를 입력해 주세요.");
		}else {
			if(!enc.matches(crefiaDomain.getOldPassword(), detail.getPassword())){
    			return new ResponseMsg(HttpStatus.OK, "fail", "현재 비밀번호를 확인해 주세요.");
    		}
		}
		if(StringUtils.isEmpty(crefiaDomain.getPassword()) || StringUtils.isEmpty(crefiaDomain.getPasswordChk())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호를 입력해 주세요.");
		}else {
			String pwdChkResultMsg = commonService.pwdValidation(detail.getMemberId(),crefiaDomain.getPassword(),crefiaDomain.getPasswordChk());
			
			if(StringUtils.isNotEmpty(pwdChkResultMsg)) {
				return new ResponseMsg(HttpStatus.OK, "fail", pwdChkResultMsg);
			}
		}
		
		int idCheck = crefiaRepository.getmemberId(crefiaDomain);
		
		if(idCheck > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이미 등록된 아이디 입니다.");
		}
		
		//비밀번호 암호화
		crefiaDomain.setPassword(enc.encode(crefiaDomain.getPassword()));
    	int result = crefiaRepository.updateCrefia(crefiaDomain);
    	
    	if(result > 0) {
    		return new ResponseMsg(HttpStatus.OK, "success", "수정되었습니다.");
    	}else {
    		return new ResponseMsg(HttpStatus.OK, "success", "오류가 발생하였습니다.");
    	}
	}
	
	@Transactional
	public ResponseMsg deleteCrefia(CrefiaDomain crefiaDomain) {
		crefiaRepository.deleteCrefia(crefiaDomain);
		return new ResponseMsg(HttpStatus.OK, "success", "삭제되었습니다.");
	}
	
	/* ========================================================================== */
	
	// 협회 관리자 관리 - 담당자 코드  조회
	@Transactional(readOnly = true)
	public List<CrefiaDomain> selectCrefiaWorkMemberList(CrefiaDomain crefiaDomain){
		return crefiaRepository.selectCrefiaWorkMemberList(crefiaDomain);
	}
	
	// 협회 관리자 관리 - 회원사 코드 조회
	@Transactional(readOnly = true)
	public List<CrefiaDomain> selectCrefiaWorkCompanyList(CrefiaDomain crefiaDomain){
		return crefiaRepository.selectCrefiaWorkCompanyList(crefiaDomain);
	}
	
	// 협회 관리자 업무분장 - 등록
	@Transactional
	public ResponseMsg insertCrefiaWork(@Valid CrefiaDomain crefiaDomain) {
		this.deleteCrefiaWork(crefiaDomain);
		int insertResult 	= 0;
		int memberSeqArr[] 	= crefiaDomain.getMemberSeqArr();
		int comCodeArr[] 	= crefiaDomain.getComCodeArr();
		
		for(int i=0;i < memberSeqArr.length;i++) {
			crefiaDomain.setMemberSeq(memberSeqArr[i]);
			crefiaDomain.setComCode(comCodeArr[i]);
			insertResult += crefiaRepository.insertCrefiaWork(crefiaDomain);
		}
		if(insertResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0002");
	}
	
	//협회 관리자 업무분장 - 삭제
	@Transactional
	public int deleteCrefiaWork(@Valid CrefiaDomain crefiaDomain) {
		return crefiaRepository.deleteCrefiaWork(crefiaDomain);
	}
	
}