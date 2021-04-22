package com.loanscrefia.member.company.controller;

import org.apache.commons.lang3.StringUtils;
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
import com.loanscrefia.member.company.domain.CompanyDomain;
import com.loanscrefia.member.company.repository.CompanyRepository;

@Controller
@RequestMapping(value="/member/company")
public class CompanyController {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	// 회원사 관리
	@GetMapping(value="/companyPage")
	public String companyPage() {
		return CosntPage.BoCompanyPage+"/companyList";
	}

	// 회원사 조회
	@PostMapping(value="/companyList")
	public ResponseEntity<ResponseMsg> companyList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyRepository.selectCompanyList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 회원서 상세 팝업
	@GetMapping("/p/companyView")
    public ModelAndView companyView(CompanyDomain companyDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.BoCompanyPage+"/p/companyView");
    	
    	// 테스트용 임시
    	if(StringUtils.isNotEmpty(companyDomain.getComNo())) {
    		// 조회 후 데이터 던짐
        	CompanyDomain result = new CompanyDomain();
        	result.setComNm("테스터");
        	mv.addObject("testParam", result);
    	}
    	
        return mv;
    }
	
}
