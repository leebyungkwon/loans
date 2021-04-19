package com.loanscrefia.system.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.code.domain.CodeDomain;
import com.loanscrefia.system.code.repository.CodeRepository;

@Service
public class CodeService {

	@Autowired private CodeRepository codeRepo;
	
	//코드 마스터 리스트
	@Transactional(readOnly=true)
	public List<CodeDomain> selectCodeMstList(CodeDomain codeDomain){
		return codeRepo.selectCodeMstList(codeDomain);
	}
	
	//코드 마스터ID 중복체크
	@Transactional(readOnly=true)
	public int codeMstCdDupCheck(CodeDomain codeDomain){
		return codeRepo.codeMstCdDupCheck(codeDomain);
	}
	
	//코드 마스터 저장
	@Transactional
	public ResponseMsg saveCodeMst(CodeDomain codeDomain){
		//저장타입값 없으면 에러
		if(codeDomain.getSaveType() == null || codeDomain.getSaveType().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002");
		}
		
		if(codeDomain.getSaveType().equals("reg")) {
			codeRepo.insertCodeMst(codeDomain);
		}else if(codeDomain.getSaveType().equals("upd")) {
			//수정 시 코드마스터ID값 없으면 에러
			if(codeDomain.getCodeMstCd() == null || codeDomain.getCodeMstCd().equals("")) {
				return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002");
			}
			codeRepo.updateCodeMst(codeDomain);
		}
		return new ResponseMsg(HttpStatus.OK, "COM0001", codeDomain, null);
	}
	
	//코드 마스터 상세
	
	//코드 상세 리스트
	@Transactional(readOnly=true)
	public List<CodeDomain> selectCodeDtlList(CodeDomain codeDomain){
		return codeRepo.selectCodeDtlList(codeDomain);
	}
	
	//코드 상세ID 중복체크
	@Transactional(readOnly=true)
	public int codeDtlCdDupCheck(CodeDomain codeDomain){
		return codeRepo.codeDtlCdDupCheck(codeDomain);
	}
	
	//코드 상세 저장
	@Transactional
	public ResponseMsg saveCodeDtl(CodeDomain codeDomain){
		//저장타입값 없으면 에러
		if(codeDomain.getSaveType() == null || codeDomain.getSaveType().equals("")) {
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002");
		}
		
		if(codeDomain.getSaveType().equals("reg")) {
			codeRepo.insertCodeDtl(codeDomain);
		}else if(codeDomain.getSaveType().equals("upd")) {
			//수정 시 코드마스터ID값,코드상세ID값 없으면 에러
			if(codeDomain.getCodeMstCd() == null || codeDomain.getCodeMstCd().equals("") || codeDomain.getCodeDtlCd() == null || codeDomain.getCodeDtlCd().equals("")) {
				return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002");
			}
			codeRepo.updateCodeDtl(codeDomain);
		}
		return new ResponseMsg(HttpStatus.OK, "COM0001", codeDomain, null);
	}
	
	//코드 상세
	
}
