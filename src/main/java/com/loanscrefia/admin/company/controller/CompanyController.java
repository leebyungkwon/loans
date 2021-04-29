package com.loanscrefia.admin.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.service.CompanyService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/admin/company")
public class CompanyController {
	
	@Autowired private CompanyService companyService;
	
	// 협회 - 회원사 담당자 조회 페이지
	@GetMapping(value="/companyPage")
	public String companyPage() {
		return CosntPage.BoCompanyPage+"/companyList";
	}
	
	// 협회 - 회원사 담당자 리스트 페이지
	@PostMapping(value="/companyList")
	public ResponseEntity<ResponseMsg> companyList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.selectCompanyList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	  // 협회= 회원사 당담자 상세 페이지
//	@PostMapping(value="/companyDetail") 
//	public ResponseEntity<ResponseMsg> companyDetail(CompanyDomain companyDomain){ 
//		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
//		responseMsg.setData(companyService.selectCompanyDetail(companyDomain));
//		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
//	}
//	
//	@GetMapping(value="/companyDetail")
//	public ModelAndView getCompanyDetail(@RequestParam String url) {
//		ModelAndView mv = new ModelAndView(url);
//		return mv;
//	}
	
//	@GetMapping("/common/openPopup")
//    public ModelAndView openPopup(@RequestParam String url) { 
//    	ModelAndView mv = new ModelAndView(url);
//        return mv;
//    }
	  
	@GetMapping(value="/companyDetail")
	public ModelAndView companyDetail() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoCompanyPage+"/companyDetail");
        return mv;
	}
	  
	 
}
