package com.loanscrefia.admin.recruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.service.RecruitService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/admin/recruit")
public class RecruitController {
	
	@Autowired
	private RecruitService recruitService;
	
	
	// 모집인 조회 페이지
	@GetMapping(value="/recruitPage")
	public String recruitPage() {
		return CosntPage.BoRecruitPage+"/recruitList";
	}

	// 모집인 조회
	@PostMapping(value="/recruitList")
	public ResponseEntity<ResponseMsg> recruitList(RecruitDomain recruitDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(recruitService.selectRecruitList(recruitDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 모집인 등록 팝업
	@GetMapping(value="/p/recruitReg")
	public ModelAndView recruitReg() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/p/recruitReg");
        return mv;
	}
	
	
	
}
