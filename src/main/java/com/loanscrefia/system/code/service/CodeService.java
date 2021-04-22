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
	 * 코드 마스터
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//코드 마스터 리스트
	@Transactional(readOnly=true)
	public List<CodeMstDomain> selectCodeMstList(CodeMstDomain codeMstDomain){
		return codeRepo.selectCodeMstList(codeMstDomain);
	}
	
	/*
	//코드 마스터ID 중복체크
	@Transactional(readOnly=true)
	public ResponseMsg codeMstCdDupCheck(CodeMstDomain codeMstDomain){
		if(codeMstDomain.getCodeMstCd() == null || codeMstDomain.getCodeMstCd().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "코드마스터ID값 없음ㅜㅜ");
		}
		
		//중복체크
		int dupCheckResult = codeRepo.codeMstCdDupCheck(codeMstDomain);
		
		//결과
		if(dupCheckResult > 0) {
			//중복 O
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "사용불가ㅜㅜ");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0001", "사용가능^^");
	}
	*/
	
	//코드 마스터 저장
	@Transactional
	public ResponseMsg saveCodeMst(CodeMstDomain codeMstDomain){
		if(codeMstDomain.getSaveType() == null || codeMstDomain.getSaveType().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "저장타입값 없음");
		}
		
		if(codeMstDomain.getSaveType().equals("reg")) {
			//중복체크
			int dupCheckResult = codeRepo.codeMstCdDupCheck(codeMstDomain);
			
			if(dupCheckResult > 0) {
				//중복 O
				return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "코드마스터ID 중복");
			}
			
			//중복 X -> 등록
			codeRepo.insertCodeMst(codeMstDomain);
		}else if(codeMstDomain.getSaveType().equals("upd")) {
			//수정
			codeRepo.updateCodeMst(codeMstDomain);
		}else {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "저장타입값 이상해");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0001", null);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드 상세
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//코드 상세 리스트
	@Transactional(readOnly=true)
	public List<CodeDtlDomain> selectCodeDtlList(CodeDtlDomain codeDtlDomain){
		return codeRepo.selectCodeDtlList(codeDtlDomain);
	}
	
	/*
	//코드 상세ID 중복체크
	@Transactional(readOnly=true)
	public ResponseMsg codeDtlCdDupCheck(CodeDtlDomain codeDtlDomain){
		if(codeDtlDomain.getCodeMstCd() == null || codeDtlDomain.getCodeMstCd().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "코드마스터ID값 없음ㅜㅜ");
		}
		if(codeDtlDomain.getCodeDtlCd() == null || codeDtlDomain.getCodeDtlCd().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "코드상세ID값 없음ㅜㅜ");
		}
		
		//중복체크
		int dupCheckResult = codeRepo.codeDtlCdDupCheck(codeDtlDomain);
		
		//결과
		if(dupCheckResult > 0) {
			//중복 O
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "사용불가ㅜㅜ");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0001", "사용가능^^");
	}
	*/
	
	//코드 상세 저장
	@Transactional
	public ResponseMsg saveCodeDtl(CodeDtlDomain codeDtlDomain){
		if(codeDtlDomain.getSaveType() == null || codeDtlDomain.getSaveType().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "저장타입값 없음");
		}
		
		if(codeDtlDomain.getSaveType().equals("reg")) {
			//중복체크
			int dupCheckResult = codeRepo.codeDtlCdDupCheck(codeDtlDomain);
			
			if(dupCheckResult > 0) {
				//중복 O
				return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "코드상세ID 중복");
			}
			//중복 X -> 등록
			codeRepo.insertCodeDtl(codeDtlDomain);
		}else if(codeDtlDomain.getSaveType().equals("upd")) {
			//수정
			codeRepo.updateCodeDtl(codeDtlDomain);
		}else {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", "저장타입값 이상해");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0001", null);
	}
	
	
}
