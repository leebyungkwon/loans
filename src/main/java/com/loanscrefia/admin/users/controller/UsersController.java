package com.loanscrefia.admin.users.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.service.UsersService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/admin")
public class UsersController {
	
	@Autowired 
	private UsersService usersService;
	
	@Autowired 
	private CommonService commonService;
	
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
	
	// 회원관리 상세
	@PostMapping(value="/users/usersDetail")
    public ModelAndView usersDetail(UsersDomain usersDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoUsersPage+"/usersDetail");
		UsersDomain usersInfo = usersService.getUsersDetail(usersDomain);
    	mv.addObject("usersInfo", usersInfo);
    	
    	//첨부파일
    	FileDomain file = new FileDomain();
    	if(usersInfo.getFileSeq() > 0) {
        	file.setFileSeq(usersInfo.getFileSeq());
        	file = commonService.getFile(file);    		
    	}
    	mv.addObject("file", file);
        return mv;
    }
	
	// 로그인 차단 해제
	@PostMapping(value="/users/loginStopUpdate")
	public ResponseEntity<ResponseMsg> loginStopUpdate(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.loginStopUpdate(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 회원관리 법인 승인처리
	@PostMapping(value="/users/usersCorpApply")
	public ResponseEntity<ResponseMsg> usersCorpApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.usersCorpApply(usersDomain);
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
