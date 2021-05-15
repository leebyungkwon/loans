package com.loanscrefia.admin.crefia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	
	// 협회 관리자 관리 페이지
	@GetMapping(value="/crefia/crefiaPage")
	public String companyPage() {
		return CosntPage.BoCrefiaPage+"/crefiaList";
	}
	
	// 협회 관리자 관리 조회
	@PostMapping(value="/crefia/crefiaList")
	public ResponseEntity<ResponseMsg> crefiaList(CrefiaDomain crefiaDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(crefiaService.selectCrefiaList(crefiaDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 협회 관리자 관리 등록 팝업
	@GetMapping("/crefia/crefiaSavePopup")
    public ModelAndView templeteSavePopup(@ModelAttribute CrefiaDomain crefiaDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/crefiaSavePopup");
    	if(crefiaDomain.getMemberSeq() > 0) {
    		CrefiaDomain result = crefiaService.crefiaDetail(crefiaDomain);
    		mv.addObject("result", result);
    	}
        return mv;
    }
	
	// 협회 관리자 관리 등록 / 상세
	@PostMapping(value="/crefia/crefiaDetail")
    public ModelAndView crefiaDetail(CrefiaDomain crefiaDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.BoCrefiaPage+"/crefiaDetail");
    	if(crefiaDomain.getMemberSeq() > 0) {
    		CrefiaDomain crefiaResult = crefiaService.crefiaDetail(crefiaDomain);
        	mv.addObject("result", crefiaResult);    		
    	}
        return mv;
    }
	
	
	/*----------------  업무분장 영역   -----------------------------------*/

	
	// 협회 관리자 업무분장 페이지
	@GetMapping(value="/crefiaWork/crefiaWorkPage")
	public String crefiaWorkPage() {
		return CosntPage.BoCrefiaPage+"/crefiaWorkList";
	}
}
