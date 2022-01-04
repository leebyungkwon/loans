package com.loanscrefia.admin.crefia.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.crefia.repository.CrefiaRepository;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class CrefiaService {

	@Autowired 
	private CrefiaRepository crefiaRepository;
	
	@Autowired private CommonRepository commonRepository;
	
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
		
		// 2022-01-04 비밀번호 영문(대소문자 구분), 숫자, 특수문자 조합, 9~12자리
		String userId = crefiaDomain.getMemberId();
		String password = crefiaDomain.getPassword();
		 
		String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$";
		Matcher matcher = Pattern.compile(pwPattern).matcher(password);
		if(!matcher.matches()){
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호는 영문(대소문자 구분), 숫자, 특수문자 조합, 8~20자리");
		}
		
		if(password.contains(userId)){
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호에 아이디는 포함할 수 없습니다.");
		}
		
		pwPattern = "(\\w)\\1\\1";
		Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);
		if(matcher2.find()){
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호는 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용이 불가능 합니다.");
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
	public ResponseMsg updCrefia(CrefiaDomain crefiaDomain) {
		
		if(StringUtils.isEmpty(crefiaDomain.getOldPassword())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "현재비밀번호를 입력해 주세요.");
		}
		
		if(StringUtils.isEmpty(crefiaDomain.getPassword())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호를 입력해 주세요.");
		}
		
		
		// 2022-01-04 비밀번호 영문(대소문자 구분), 숫자, 특수문자 조합, 9~12자리
		String userId = crefiaDomain.getMemberId();
		String password = crefiaDomain.getPassword();
		 
		String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$";
		Matcher matcher = Pattern.compile(pwPattern).matcher(password);
		if(!matcher.matches()){
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호는 영문(대소문자 구분), 숫자, 특수문자 조합, 8~20자리");
		}
		
		if(password.contains(userId)){
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호에 아이디는 포함할 수 없습니다.");
		}
		
		pwPattern = "(\\w)\\1\\1";
		Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);
		if(matcher2.find()){
			return new ResponseMsg(HttpStatus.OK, "fail", "비밀번호는 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용이 불가능 합니다.");
		}
		
		
		int idCheck = crefiaRepository.getmemberId(crefiaDomain);
		if(idCheck > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이미 등록된 아이디 입니다.");
		}
		
		//비밀번호 암호화
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		String newPwd = crefiaDomain.getPassword();
		crefiaDomain.setPassword(enc.encode(newPwd));
    	if(StringUtils.isNotEmpty(crefiaDomain.getOldPassword())) {
    		MemberDomain memberDomain = new MemberDomain();
    		MemberDomain result = commonRepository.getMemberDetail(memberDomain);
    		if(!enc.matches(crefiaDomain.getOldPassword(), result.getPassword())){
    			return new ResponseMsg(HttpStatus.OK, "pwFail", "현재 비밀번호를 확인해 주세요.");
    		}
    	}
    	
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