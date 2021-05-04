package com.loanscrefia.member.admin.controller;

import javax.validation.Valid;

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
import com.loanscrefia.member.admin.service.AdminService;

@Controller
@RequestMapping(value="/member/admin")
public class AdminController {
	
	@Autowired private AdminService adminService;
	
	// 관리자 조회 및 변경 페이지
	@GetMapping(value="/adminPage")
	public String adminPage() {
		return CosntPage.BoMemberAdminPage+"/adminList";
	}

	// 회원사 시스템 - 관리자 조회
	@PostMapping(value="/adminList")
	public ResponseEntity<ResponseMsg> adminList(AdminDomain adminDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(adminService.selectAdminList(adminDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 회원사 시스템 - 상세 페이지
	@PostMapping(value="/adminDetail")
    public ModelAndView adminDetail(AdminDomain adminDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.BoMemberAdminPage+"/adminDetail");
    	AdminDomain adminInfo = adminService.getAdminDetail(adminDomain);
    	mv.addObject("adminInfo", adminInfo);
        return mv;
    }
	
	// 회원사 시스템 - 관리자 수정 페이지
	@PostMapping(value="/adminDetailUpdate")
	public ModelAndView adminDetailUpdate(AdminDomain adminDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoMemberAdminPage+"/adminDetailUpdate");
		AdminDomain adminInfo = adminService.getAdminDetailUpd(adminDomain);
		
		// denied 페이지에서 진입시 가승인 회원 체크
		adminInfo.setTempMemberCheck(adminDomain.getTempMemberCheck());
		
		mv.addObject("adminInfo", adminInfo);
		return mv;
	}
	
	// 관리자 수정 페이지 -> Insert
	@PostMapping(value="/saveAdminUpdate")
	public ResponseEntity<ResponseMsg> saveAdminUpdate(@Valid AdminDomain adminDomain) {
		ResponseMsg responseMsg = adminService.saveAdminUpdate(adminDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 체크박스 선택시 삭제
	@PostMapping(value="/adminCheckDelete")
	public ResponseEntity<ResponseMsg> adminCheckDelete(AdminDomain adminDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(adminService.adminCheckDelete(adminDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
}
