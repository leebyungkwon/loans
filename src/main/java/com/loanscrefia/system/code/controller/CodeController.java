package com.loanscrefia.system.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.code.domain.CodeDomain;
import com.loanscrefia.system.code.service.CodeService;

@Controller
@RequestMapping(value="/system/code")
public class CodeController {

	@Autowired private CodeService codeService;
	
	//공통코드관리 리스트 페이지
	@GetMapping(value="/codeList")
	public String codeList() {
		return CosntPage.BoCodePage+"/codeList";
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드 마스터
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//공통코드관리 > 코드 마스터 리스트
	@PostMapping(value="/codeMstList")
	public ResponseEntity<ResponseMsg> codeMstList(CodeDomain codeVO){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(codeService.selectCodeMstList(codeVO));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//공통코드관리 > 코드 마스터 아이디 중복체크
	@PostMapping(value="/codeMstCdDupCheck")
	public ResponseEntity<ResponseMsg> codeMstCdDupCheck(CodeDomain codeVO){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(codeService.codeMstCdDupCheck(codeVO));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	/*
	//공통코드관리 > 코드 마스터 저장 레이어 팝업
	@GetMapping(value="/p/codeMstSavePop")
	public ModelAndView codeMstSavePop(CodeDomain codeVO) {
		ModelAndView mav 	= new ModelAndView();
		String saveType 	= codeVO.getSaveType();
		
		mav.addObject("saveType", saveType);
		mav.setViewName(CosntPage.BoCodePage+"/p/codeMstSavePop");
		
        return mav;
	}
	*/
	
	//공통코드관리 > 코드 마스터 저장
	@PostMapping(value="/codeMstSave")
	public ResponseEntity<ResponseMsg> codeMstSave(CodeDomain codeVO){
		ResponseMsg responseMsg = codeService.saveCodeMst(codeVO);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 코드 상세
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//공통코드관리 > 코드 상세 리스트
	@PostMapping(value="/codeDtlList")
	public ResponseEntity<ResponseMsg> codeDtlList(CodeDomain codeVO){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(codeService.selectCodeDtlList(codeVO));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//공통코드관리 > 코드 마스터 아이디 중복체크
	@PostMapping(value="/codeDtlCdDupCheck")
	public ResponseEntity<ResponseMsg> codeDtlCdDupCheck(CodeDomain codeVO){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(codeService.codeDtlCdDupCheck(codeVO));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	/*
	//공통코드관리 > 코드 상세 저장 레이어 팝업
	@GetMapping(value="/p/codeDtlSavePop")
	public ModelAndView codeDtlSavePop(CodeDomain codeVO) {
		ModelAndView mav 	= new ModelAndView();
		String saveType 	= codeVO.getSaveType();
		
		mav.addObject("saveType", saveType);
		mav.setViewName(CosntPage.BoCodePage+"/p/codeDtlSavePop");
		
        return mav;
	}
	*/
	
	//공통코드관리 > 코드 상세 저장
	@PostMapping(value="/codeDtlSave")
	public ResponseEntity<ResponseMsg> codeDtlSave(CodeDomain codeVO){
		ResponseMsg responseMsg = codeService.saveCodeDtl(codeVO);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
