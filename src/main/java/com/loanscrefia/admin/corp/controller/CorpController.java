package com.loanscrefia.admin.corp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.service.CorpService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;

import sinsiway.CryptoUtil;

@Controller
@RequestMapping(value="/admin/corp")
public class CorpController {

	@Autowired private CorpService corpService;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 여신금융협회 시스템 > 법인 관리
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/corpPage")
	public String corpPage() {
		return CosntPage.BoCorpPage+"/corpList";
	}
	
	//리스트
	@PostMapping(value="/corpList")
	public ResponseEntity<ResponseMsg> corpListAjax(CorpDomain corpDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(corpService.selectCorpList(corpDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록,수정 팝업
	@GetMapping(value="/corpSavePopup")
	public ModelAndView corSavePopup(CorpDomain corpDomain) {
		ModelAndView mv 	= new ModelAndView(CosntPage.Popup+"/corpSavePopup");
		
		CorpDomain corpInfo = new CorpDomain();
		if(corpDomain.getCorpSeq() != null) {
			corpInfo = corpService.getCorpInfo(corpDomain);
		}
		
		mv.addObject("corpInfo", corpInfo);
		return mv;
	}
	
	//저장
	@PostMapping(value="/saveCorpInfo")
	public ResponseEntity<ResponseMsg> saveCorpInfo(@Valid CorpDomain corpDomain, BindingResult bindingResult){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		
		responseMsg = corpService.saveCorpInfo(corpDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
