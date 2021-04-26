package com.loanscrefia.member.admin.controller;

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
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.admin.repository.AdminRepository;

@Controller
@RequestMapping(value="/member/admin")
public class AdminController {
	
	@Autowired
	private AdminRepository AdminRepository;
	
	// 관리자 조회 및 변경 페이지
	@GetMapping(value="/adminPage")
	public String adminPage() {
		return CosntPage.BoMemberAdminPage+"/adminList";
	}

	// 회원사 시스템 - 관리자 조회
	@PostMapping(value="/adminList")
	public ResponseEntity<ResponseMsg> adminList(AdminDomain AdminDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(AdminRepository.selectAdminList(AdminDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
}
