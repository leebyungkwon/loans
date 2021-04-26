package com.loanscrefia.admin.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/admin/company")
public class CompanyController {
	
	// 협회 - 회원사 담당자 조회 페이지
	@GetMapping(value="/companyPage")
	public String companyPage() {
		return CosntPage.BoCompanyPage+"/companyList";
	}

}
