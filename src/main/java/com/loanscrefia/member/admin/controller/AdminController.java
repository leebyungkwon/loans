package com.loanscrefia.member.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.admin.service.AdminService;

@Controller
@RequestMapping(value="/member/admin")
public class AdminController {
	
	@Autowired private AdminService adminService;
	@Autowired private CommonService commonService;
	
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
	@PostMapping(value="/adminDetailPage")
    public ModelAndView adminDetail(AdminDomain adminDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.BoMemberAdminPage+"/adminDetail");
    	
    	//상세
    	AdminDomain adminInfo = adminService.getAdminDetail(adminDomain);
    	mv.addObject("adminInfo", adminInfo);
    	
    	//첨부파일
    	FileDomain file = new FileDomain();
    	if(adminInfo.getFileSeq() > 0) {
        	file.setFileSeq(adminInfo.getFileSeq());
        	file = commonService.getFile(file);    		
    	}
    	mv.addObject("file", file);
    	
        return mv;
    }
	
	// 회원사 시스템 - 관리자 수정 페이지
	@PostMapping(value="/adminDetailUpdPage")
	public ModelAndView adminDetailUpdate(HttpServletRequest request ,AdminDomain adminDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoMemberAdminPage+"/adminDetailUpdate");
	
		//상세
		AdminDomain adminInfo = adminService.getAdminDetailUpd(adminDomain);
		adminInfo.setTempMemberCheck(adminDomain.getTempMemberCheck()); //denied 페이지에서 진입시 가승인 회원 체크
		mv.addObject("adminInfo", adminInfo);

		//첨부파일
		FileDomain file = new FileDomain();
		if(adminInfo.getFileSeq() > 0) {
			file.setFileSeq(adminInfo.getFileSeq());
	    	file = commonService.getFile(file);
		}
    	mv.addObject("file", file);
    	
    	//메뉴
    	mv.addObject("prevMn", request.getParameter("prevMn"));
		
		return mv;
	}
	
	// 관리자 수정 페이지 -> Insert
	@PostMapping(value="/saveAdminUpdate")
	public ResponseEntity<ResponseMsg> saveAdminUpdate(@RequestParam("files") MultipartFile[] files, @Valid AdminDomain adminDomain, BindingResult bindingResult) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		ResponseMsg resultResponseMsg = adminService.saveAdminUpdate(files, adminDomain);
		return new ResponseEntity<ResponseMsg>(resultResponseMsg ,HttpStatus.OK);
	}
	
	// 체크박스 선택시 삭제
	@PostMapping(value="/adminCheckDelete")
	public ResponseEntity<ResponseMsg> adminCheckDelete(AdminDomain adminDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(adminService.adminCheckDelete(adminDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 재승인 요청
	@PostMapping(value="/reAppr")
	public ResponseEntity<ResponseMsg> reAppr(@RequestParam("files") MultipartFile[] files, AdminDomain adminDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(adminService.reAppr(files, adminDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
}
