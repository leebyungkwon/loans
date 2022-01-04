package com.loanscrefia.admin.crefia.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.crefia.service.CrefiaService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/admin")
public class CrefiaController {
	
	@Autowired 
	private CrefiaService crefiaService;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 협회 관리자 관리
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/crefia/crefiaPage")
	public String companyPage() {
		return CosntPage.BoCrefiaPage+"/crefiaList";
	}
	
	//리스트
	@PostMapping(value="/crefia/crefiaList")
	public ResponseEntity<ResponseMsg> crefiaList(CrefiaDomain crefiaDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(crefiaService.selectCrefiaList(crefiaDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록,상세 팝업
	@GetMapping("/crefia/crefiaSavePopup")
    public ModelAndView templeteSavePopup(@ModelAttribute CrefiaDomain crefiaDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/crefiaSavePopup");
    	if(crefiaDomain.getMemberSeq() > 0) {
    		CrefiaDomain result = crefiaService.crefiaDetail(crefiaDomain);
    		mv.addObject("result", result);
    	}
        return mv;
    }
	
	
	// 2022-01-04 보안취약점에 따른 등록, 상세 분리(상세팝업)
	@GetMapping("/crefia/crefiaUpdPopup")
    public ModelAndView crefiaUpdPopup(@ModelAttribute CrefiaDomain crefiaDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/crefiaUpdPopup");
    	if(crefiaDomain.getMemberSeq() > 0) {
    		CrefiaDomain result = crefiaService.crefiaDetail(crefiaDomain);
    		mv.addObject("result", result);
    	}
        return mv;
    }
	
	
	//저장
	@PostMapping(value="/crefia/saveCrefia")
	public ResponseEntity<ResponseMsg> saveCrefia(@Valid CrefiaDomain crefiaDomain, BindingResult bindingResult){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		responseMsg = crefiaService.saveCrefia(crefiaDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2022-01-04 보안취약점에 따른 수정 기능 추가
	@PostMapping(value="/crefia/updCrefia")
	public ResponseEntity<ResponseMsg> updCrefia(@Valid CrefiaDomain crefiaDomain, BindingResult bindingResult){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		responseMsg = crefiaService.updCrefia(crefiaDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	//삭제
	@PostMapping(value="/crefia/deleteCrefia")
	public ResponseEntity<ResponseMsg> deleteCrefia(CrefiaDomain crefiaDomain){
		ResponseMsg responseMsg = crefiaService.deleteCrefia(crefiaDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 협회 관리자 업무분장
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트
	@GetMapping(value="/crefiaWork/crefiaWorkPage")
	public ModelAndView crefiaWorkPage(CrefiaDomain crefiaDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoCrefiaPage+"/crefiaWorkList");
    	
		List<CrefiaDomain> memberInfo = crefiaService.selectCrefiaWorkMemberList(crefiaDomain);
		List<CrefiaDomain> companyInfo = crefiaService.selectCrefiaWorkCompanyList(crefiaDomain);
		
    	mv.addObject("memberInfo", memberInfo);
    	mv.addObject("companyInfo", companyInfo);
    	
		return mv;
	}
	
	//등록
	@PostMapping(value="/crefiaWork/insertCrefiaWork")
	public ResponseEntity<ResponseMsg> insertCrefiaWork(@Valid CrefiaDomain crefiaDomain, BindingResult bindingResult){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		responseMsg = crefiaService.insertCrefiaWork(crefiaDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
