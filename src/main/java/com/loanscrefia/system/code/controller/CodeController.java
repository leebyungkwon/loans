package com.loanscrefia.system.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.domain.CodeMstDomain;
import com.loanscrefia.system.code.service.CodeService;

@Controller
@RequestMapping(value="/system/code")
public class CodeController {

	@Autowired private CodeService codeService;
	
	//공통코드관리 리스트 페이지
	@GetMapping(value="/codePage")
	public String codeList() {
		return CosntPage.BoCodePage+"/codeList";
	}
	
	//공통코드관리 > 코드마스터 리스트
	@PostMapping(value="/codeMstList")
	public ResponseEntity<ResponseMsg> codeMstList(CodeMstDomain codeMstDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(codeService.selectCodeMstList(codeMstDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//공통코드관리 > 코드마스터 저장
	@PostMapping(value="/codeMstSave")
	public ResponseEntity<ResponseMsg> codeMstSave(CodeMstDomain codeMstDomain){
		ResponseMsg responseMsg = codeService.saveCodeMst(codeMstDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	//공통코드관리 > 코드상세 리스트
	@PostMapping(value="/codeDtlList")
	public ResponseEntity<ResponseMsg> codeDtlList(CodeDtlDomain codeDtlDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(codeService.selectCodeDtlList(codeDtlDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//공통코드관리 > 코드상세 저장
	@PostMapping(value="/codeDtlSave")
	public ResponseEntity<ResponseMsg> codeDtlSave(CodeDtlDomain codeDtlDomain){
		ResponseMsg responseMsg = codeService.saveCodeDtl(codeDtlDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
