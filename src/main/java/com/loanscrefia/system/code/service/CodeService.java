package com.loanscrefia.system.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.domain.CodeMstDomain;
import com.loanscrefia.system.code.repository.CodeRepository;

@Service
public class CodeService {

	@Autowired private CodeRepository codeRepo;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드마스터
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//코드마스터 리스트
	@Transactional(readOnly=true)
	public List<CodeMstDomain> selectCodeMstList(CodeMstDomain codeMstDomain){
		return codeRepo.selectCodeMstList(codeMstDomain);
	}
	
	//코드마스터 저장
	@Transactional
	public ResponseMsg saveCodeMst(CodeMstDomain codeMstDomain){
		if(codeMstDomain.getSaveType() == null || codeMstDomain.getSaveType().equals("")) {
			return new ResponseMsg(HttpStatus.OK, "fail", "저장타입이 누락되었습니다.");
		}
		
		if(codeMstDomain.getSaveType().equals("reg")) {
			//중복체크
			int dupCheckResult = codeRepo.codeMstCdDupCheck(codeMstDomain);
			
			if(dupCheckResult > 0) {
				//중복 O
				return new ResponseMsg(HttpStatus.OK, "fail", "입력하신 코드마스터ID가 이미 존재합니다.");
			}
			
			//중복 X -> 등록
			codeRepo.insertCodeMst(codeMstDomain);
		}else if(codeMstDomain.getSaveType().equals("upd")) {
			//수정
			codeRepo.updateCodeMst(codeMstDomain);
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "저장타입을 확인해 주세요.");
		}
		return new ResponseMsg(HttpStatus.OK, "success", "저장되었습니다.");
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드상세
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//코드상세 리스트
	@Transactional(readOnly=true)
	public List<CodeDtlDomain> selectCodeDtlList(CodeDtlDomain codeDtlDomain){
		return codeRepo.selectCodeDtlList(codeDtlDomain);
	}
	
	//코드상세 저장
	@Transactional
	public ResponseMsg saveCodeDtl(CodeDtlDomain codeDtlDomain){
		if(codeDtlDomain.getSaveType() == null || codeDtlDomain.getSaveType().equals("")) {
			return new ResponseMsg(HttpStatus.OK, "fail", "저장타입이 누락되었습니다.");
		}
		
		if(codeDtlDomain.getSaveType().equals("reg")) {
			//중복체크
			int dupCheckResult = codeRepo.codeDtlCdDupCheck(codeDtlDomain);
			
			if(dupCheckResult > 0) {
				//중복 O
				return new ResponseMsg(HttpStatus.OK, "fail", "입력하신 코드상세ID가 이미 존재합니다.");
			}
			//중복 X -> 등록
			codeRepo.insertCodeDtl(codeDtlDomain);
		}else if(codeDtlDomain.getSaveType().equals("upd")) {
			//수정
			codeRepo.updateCodeDtl(codeDtlDomain);
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "저장타입을 확인해 주세요.");
		}
		return new ResponseMsg(HttpStatus.OK, "success", "저장되었습니다.");
	}
	
	
}
