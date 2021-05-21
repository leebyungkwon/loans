package com.loanscrefia.admin.crefia.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.crefia.service.CrefiaService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.admin.domain.AdminDomain;

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
	
	// 협회 관리자 관리 등록 / 상세 팝업
	@GetMapping("/crefia/crefiaSavePopup")
    public ModelAndView templeteSavePopup(@ModelAttribute CrefiaDomain crefiaDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/crefiaSavePopup");
    	if(crefiaDomain.getMemberSeq() > 0) {
    		CrefiaDomain result = crefiaService.crefiaDetail(crefiaDomain);
    		mv.addObject("result", result);
    	}
        return mv;
    }
	
	// 협회 관리자 관리 저장
	@PostMapping(value="/crefia/saveCrefia")
	public ResponseEntity<ResponseMsg> saveCrefia(@Valid CrefiaDomain crefiaDomain){
		ResponseMsg responseMsg = crefiaService.saveCrefia(crefiaDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 협회 관리자 관리 저장
	@PostMapping(value="/crefia/deleteCrefia")
	public ResponseEntity<ResponseMsg> deleteCrefia(CrefiaDomain crefiaDomain){
		ResponseMsg responseMsg = crefiaService.deleteCrefia(crefiaDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	/*----------------  업무분장 영역   -----------------------------------*/

	
	// 협회 관리자 업무분장 페이지
	@GetMapping(value="/crefiaWork/crefiaWorkPage")
	public ModelAndView crefiaWorkPage(CrefiaDomain crefiaDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoCrefiaPage+"/crefiaWorkList");
    	
		List<CrefiaDomain> memberInfo = crefiaService.selectCrefiaWorkMemberList(crefiaDomain);
		List<CrefiaDomain> companyInfo = crefiaService.selectCrefiaWorkCompanyList(crefiaDomain);
    	mv.addObject("memberInfo", memberInfo);
    	mv.addObject("companyInfo", companyInfo);
    	
		return mv;
	}
	
	// 협회 관리자 업무분장 조회
	@PostMapping(value="/crefia/crefiaWorkList")
	public ModelAndView crefiaWorkList(CrefiaDomain crefiaDomain){
		ModelAndView mv = new ModelAndView(CosntPage.BoCrefiaPage+"/crefiaWorkList");
		return mv;
	}
	
}
