package com.loanscrefia.admin.users.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.service.UsersService;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/admin")
public class UsersController {
	
	@Autowired 
	private UsersService usersService;
	
	// 회원관리 리스트 페이지
	@GetMapping(value="/users/usersPage")
	public String usersPage() {
		return CosntPage.BoUsersPage+"/usersList";
	}
	
	// 회원관리 리스트 조회
	@PostMapping(value="/users/usersList")
	public ResponseEntity<ResponseMsg> selectUsersList(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(usersService.selectUsersList(usersDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	// 로그인 차단 해제
	@PostMapping(value="/users/loginStopUpdate")
	public ResponseEntity<ResponseMsg> loginStopUpdate(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.loginStopUpdate(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	//엑셀 다운로드
	@PostMapping("/users/usersExcelListDown")
	public void applyListExcelDown(UsersDomain usersDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		usersDomain.setIsPaging("false");
 		List<UsersDomain> excelDownList = usersService.selectUsersList(usersDomain);
 		new UtilExcel().downLoad(excelDownList, UsersDomain.class, response.getOutputStream());
	}
	
	
	
	
}
