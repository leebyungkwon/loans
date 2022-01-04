package com.loanscrefia.admin.company.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.repository.CompanyRepository;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class CompanyService {

	@Autowired 
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	//이메일 적용여부
	@Value("${email.apply}")
	public boolean emailApply;
	
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
		
		if(emailApply) {
			emailResult = emailRepository.sendEmail(emailDomain);
		}else {
			emailResult = 1;
		}
		
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
		CompanyDomain companyResult = companyRepository.getCompanyDetail(companyDomain);
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	String passWord = getTempPassword(6);
    	
    	int emailResult = 0;
		EmailDomain emailDomain = new EmailDomain();
		emailDomain.setName("여신금융협회");
		emailDomain.setEmail(companyResult.getEmail());
		emailDomain.setInstId("149");
		emailDomain.setSubsValue(companyResult.getMemberId()+"|"+passWord);
    	
    	companyDomain.setPassword(passwordEncoder.encode(passWord));
		int result = companyRepository.cleanPassword(companyDomain);
		
		if(emailApply) {
			emailResult = emailRepository.sendEmail(emailDomain);
		}else {
			emailResult = 1;
		}
		
		if(emailResult > 0 && result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else if(emailResult == 0){
			return new ResponseMsg(HttpStatus.OK, "fail", "이메일 발송에 실패하였습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	

	// 2022-01-04 보안취약점에 따른 로그인 잠김 해제 기능 추가
	@Transactional
	public ResponseMsg updLoginFail(CompanyDomain companyDomain){
		int result = companyRepository.updLoginFail(companyDomain);
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
	
	
	
	// 임시비밀번호 생성
	public static String getTempPassword(int length) {
	    int index = 0;
	    char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
	    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
	    'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
	    'w', 'x', 'y', 'z' };
	 
	    StringBuffer sb = new StringBuffer();
	 
	    for (int i = 0; i < length; i++) {
	        index = (int) (charArr.length * Math.random());
	        sb.append(charArr[index]);
	    }
	 
	    return sb.toString();
	}
	
}