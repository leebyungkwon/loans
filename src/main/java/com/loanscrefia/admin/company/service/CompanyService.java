package com.loanscrefia.admin.company.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.repository.CompanyRepository;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.config.message.ResponseMsg;

import sinsiway.CryptoUtil;

@Service
public class CompanyService {

	@Autowired 
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 회원사 담당자 관리
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//회원사 담당자 리스트 조회
	@Transactional(readOnly = true)
	public List<CompanyDomain> selectCompanyList(CompanyDomain companyDomain){
		return companyRepository.selectCompanyList(companyDomain);
	}
	
	//회원사 당담자 상세 보기
	@Transactional(readOnly=true)
	public CompanyDomain getCompanyDetail(CompanyDomain companyDomain){
		return companyRepository.getCompanyDetail(companyDomain);
	}
	
	//회원사 담당자 승인 요청 
	@Transactional
	public ResponseMsg updateCompanyStat(CompanyDomain companyDomain){

		//가승인 및 승인처리시 이메일 발송
		if(StringUtils.isEmpty(companyDomain.getEmail())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이메일을 확인해 주세요.");
		}
		int emailResult = 0;
		EmailDomain emailDomain = new EmailDomain();
		emailDomain.setName("여신금융협회");
		emailDomain.setEmail(companyDomain.getEmail());
		if("3".equals(companyDomain.getApprStat())) {
			emailDomain.setInstId("139");
			emailDomain.setSubsValue(companyDomain.getMemberId());
		}else if("2".equals(companyDomain.getApprStat())) {
			emailDomain.setInstId("140");
			emailDomain.setSubsValue(companyDomain.getMemberId());
			emailDomain.setSubsValue(companyDomain.getMemberId()+"|"+companyDomain.getMsg());
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "승인상태가 올바르지 않습니다.\n새로고침 후 다시 시도해 주세요.");
		}
		
		int result = companyRepository.updateCompanyStat(companyDomain);
		emailResult = emailRepository.sendEmail(emailDomain);
		// 임시 통과
		//emailResult = 1;
		
		if(emailResult > 0 && result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else if(emailResult == 0){
			return new ResponseMsg(HttpStatus.OK, "fail", "메일발송에 실패하였습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	//회원사 담당자 삭제 
	@Transactional
	public int deleteCompany(CompanyDomain companyDomain){
		
		int result 			= 0;
		int[] memberSeqArr 	= companyDomain.getMemberSeqArr();
		
		for(int i = 0;i < memberSeqArr.length;i++) {
			companyDomain.setMemberSeq(memberSeqArr[i]);
			result += companyRepository.deleteCompany(companyDomain);
		}
		
		return result;
	}
	
	
	
	

	// 비밀번호 초기화 
	@Transactional
	public ResponseMsg cleanPassword(CompanyDomain companyDomain){
		
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	String passWord = "qwer1234!";
    	companyDomain.setPassword(passwordEncoder.encode(passWord));
		
		int result = companyRepository.cleanPassword(companyDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 회원사 관리
	 * -------------------------------------------------------------------------------------------------------
	 */

	//리스트
	@Transactional(readOnly = true)
	public List<CompanyDomain> selectCompanyCodeList(CompanyDomain companyDomain){
		return companyRepository.selectCompanyCodeList(companyDomain);
	}
	
	//등록
	@Transactional
	public ResponseMsg saveCompanyCode(CompanyDomain companyDomain) {
		
		//회원사코드 중복체크
		int dupChkResult = companyRepository.comCodeDupChk(companyDomain);
		
		if(dupChkResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "이미 등록된 회원사코드 입니다.");
		}
		
		//등록
		int insertResult = companyRepository.saveCompanyCode(companyDomain);
		
		if(insertResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	
	
}