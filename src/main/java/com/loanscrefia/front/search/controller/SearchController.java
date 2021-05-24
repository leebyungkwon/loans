package com.loanscrefia.front.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.front.search.service.SearchService;

@Controller
@RequestMapping(value="/front")
public class SearchController {

	@Autowired private SearchService searchService;
	
	@GetMapping(value="/search/searchPage")
	public String companyPage() {
		return CosntPage.BoCompanyPage+"/companyList";
	}
}