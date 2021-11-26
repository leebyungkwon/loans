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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.users.domain.CorpUsersExcelDownDomain;
import com.loanscrefia.admin.users.domain.IndvUsersExcelDownDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.service.UsersService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.admin.domain.AdminDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/admin")
public class UsersController {
	
	@Autowired 
	private UsersService usersService;
	
	@Autowired 
	private CommonService commonService;
	
	// 개인 회원관리 리스트 페이지
	@GetMapping(value="/users/indvUsersPage")
	public String indvUsersPage() {
		return CosntPage.BoUsersPage+"/indvUsersList";
	}
	
	// 개인 회원관리 리스트 조회
	@PostMapping(value="/users/indvUsersList")
	public ResponseEntity<ResponseMsg> selectIndvUsersList(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(usersService.selectIndvUsersList(usersDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 개인 회원관리 상세
	@PostMapping(value="/users/indvUsersDetail")
    public ModelAndView indvUsersDetail(UsersDomain usersDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoUsersPage+"/indvUsersDetail");
		UsersDomain usersInfo = usersService.getIndvUsersDetail(usersDomain);
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
	
	
	// 개인회원 결격요건 수정
	@PostMapping(value="/users/updateIndvUserDis")
	public ResponseEntity<ResponseMsg> updateIndvUserDis(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.updateIndvUserDis(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 개인회원 결격요건 엑셀 업로드 팝업창
	@GetMapping(value="/users/indvUserDisExcelPopup")
	public ModelAndView indvUserDisExcelPopup() {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/indvUserDisExcelPopup");
        return mav;
	}
	
	// 개인회원 결격요건 엑셀 업로드
	@PostMapping(value="/users/indvUsersDisExcelUpload")
	public ResponseEntity<ResponseMsg> indvUsersDisExcelUpload(@RequestParam("files") MultipartFile[] files, UsersDomain usersDomain) throws IOException{
		
		//2021.10.21
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		responseMsg = usersService.indvUsersDisExcelUpload(files, usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 로그인 잠금 해제
	@PostMapping(value="/users/loginStopUpdate")
	public ResponseEntity<ResponseMsg> loginStopUpdate(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.loginStopUpdate(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 개인회원 엑셀 다운로드
	@PostMapping("/users/indvUsersExcelListDown")
	public void indvUsersExcelListDown(UsersDomain usersDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		usersDomain.setIsPaging("false");
 		List<UsersDomain> excelDownList = usersService.selectIndvUsersList(usersDomain);
 		new UtilExcel().downLoad(excelDownList, IndvUsersExcelDownDomain.class, response.getOutputStream());
	}
	
	
	// 법인 회원관리 리스트 페이지
	@GetMapping(value="/corpUsers/corpUsersPage")
	public ModelAndView corpUsersPage(String historyback) {
		ModelAndView mv =  new ModelAndView(CosntPage.BoUsersPage+"/corpUsersList");
		mv.addObject("historyback", historyback);
		return mv;
	}
	
	
	// 법인 회원관리 리스트 조회
	@PostMapping(value="/corpUsers/corpUsersList")
	public ResponseEntity<ResponseMsg> selectCorpUsersList(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(usersService.selectCorpUsersList(usersDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 법인 회원관리 상세
	@PostMapping(value="/corpUsers/corpUsersDetail")
    public ModelAndView corpUsersDetail(UsersDomain usersDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoUsersPage+"/corpUsersDetail");
		UsersDomain usersInfo = usersService.getCorpUsersDetail(usersDomain);
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
	
	// 법인회원 승인처리
	@PostMapping(value="/corpUsers/usersCorpApply")
	public ResponseEntity<ResponseMsg> usersCorpApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.usersCorpApply(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 법인회원 가승인처리
	@PostMapping(value="/corpUsers/usersCorpTempApply")
	public ResponseEntity<ResponseMsg> usersCorpTempApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.usersCorpTempApply(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 법인회원 결격요건 수정
	@PostMapping(value="/corpUsers/updateCorpUserDis")
	public ResponseEntity<ResponseMsg> updateCorpUserDis(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.updateCorpUserDis(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 금융감독원 승인여부 수정
	@PostMapping(value="/corpUsers/updatePassYn")
	public ResponseEntity<ResponseMsg> updatePassYn(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.updatePassYn(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 법인회원 결격요건 엑셀 업로드 팝업창
	@GetMapping(value="/corpUsers/corpUserDisExcelPopup")
	public ModelAndView corpUserDisExcelPopup() {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/corpUserDisExcelPopup");
        return mav;
	}
	
	// 법인회원 결격요건 엑셀 업로드
	@PostMapping(value="/corpUsers/corpUsersDisExcelUpload")
	public ResponseEntity<ResponseMsg> corpUsersDisExcelUpload(@RequestParam("files") MultipartFile[] files, UsersDomain usersDomain) throws IOException{
		
		//2021.10.21
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		responseMsg = usersService.corpUsersDisExcelUpload(files, usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 법인회원 엑셀 다운로드
	@PostMapping("/corpUsers/corpUsersExcelListDown")
	public void corpUsersExcelListDown(UsersDomain usersDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		usersDomain.setIsPaging("false");
 		List<UsersDomain> excelDownList = usersService.selectCorpUsersList(usersDomain);
 		new UtilExcel().downLoad(excelDownList, CorpUsersExcelDownDomain.class, response.getOutputStream());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	// 휴면회원관리 리스트 페이지
	@GetMapping(value="/inactive/inactivePage")
	public String inactiveUsersPage() {
		return CosntPage.BoInactivePage+"/inactiveList";
	}
	
	// 휴면회원관리 리스트 조회
	@PostMapping(value="/inactive/inactiveList")
	public ResponseEntity<ResponseMsg> inactiveList(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(usersService.selectInactiveList(usersDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 회원관리 상세
	@PostMapping(value="/inactive/inactiveDetail")
    public ModelAndView inactiveDetail(UsersDomain usersDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoInactivePage+"/inactiveDetail");
		UsersDomain usersInfo = usersService.getInactiveDetail(usersDomain);
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
	
	
	//엑셀 다운로드
	@PostMapping("/inactive/inactiveExcelListDown")
	public void inactiveExcelListDown(UsersDomain usersDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		usersDomain.setIsPaging("false");
 		List<UsersDomain> excelDownList = usersService.selectInactiveList(usersDomain);
 		new UtilExcel().downLoad(excelDownList, UsersDomain.class, response.getOutputStream());
	}
	
	// 휴면회원 활성화
	@PostMapping(value="/inactive/boInactiveUser")
	public ResponseEntity<ResponseMsg> boInactiveUser(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.boInactiveUser(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	// 개인회원 정보변경관리 리스트 페이지
	@GetMapping(value="/updateIndvUsers/updateIndvUsersPage")
	public String updateIndvUsersPage() {
		return CosntPage.BoUpdateUsersPage+"/updateIndvUsersList";
	}
	
	// 개인회원 정보변경관리 리스트 조회
	@PostMapping(value="/updateIndvUsers/updateIndvUsersList")
	public ResponseEntity<ResponseMsg> selectUpdateIndvUsersList(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(usersService.selectUpdateIndvUsersList(usersDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 개인회원 정보변경관리 상세
	@PostMapping(value="/updateIndvUsers/updateIndvUsersDetail")
    public ModelAndView updateIndvUsersDetail(UsersDomain usersDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoUpdateUsersPage+"/updateIndvUsersDetail");
		UsersDomain usersInfo = usersService.getUpdateIndvUsersDetail(usersDomain);
    	mv.addObject("usersInfo", usersInfo);

    	// 첨부파일
		FileDomain fileParam = new FileDomain();
		if(usersInfo.getReqFileSeq() > 0) {
			fileParam.setFileGrpSeq(usersInfo.getReqFileSeq());
			List<FileDomain> fileList = commonService.selectFileList(fileParam);
			mv.addObject("fileList", fileList);
		}
        return mv;
    }
	
	// 개인회원 정보변경관리 상태변경
	@PostMapping(value="/updateIndvUsers/updateIndvUsersStat")
	public ResponseEntity<ResponseMsg> updateIndvUsersStat(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.updateIndvUsersStat(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 법인회원 정보변경관리 리스트 페이지
	@GetMapping(value="/updateCorpUsers/updateCorpUsersPage")
	public String updateCorpUsersPage() {
		return CosntPage.BoUpdateUsersPage+"/updateCorpUsersList";
	}
	
	// 법인회원 정보변경관리 리스트 조회
	@PostMapping(value="/updateCorpUsers/updateCorpUsersList")
	public ResponseEntity<ResponseMsg> selectUpdateCorpUsersList(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(usersService.selectUpdateCorpUsersList(usersDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 법인회원 정보변경관리 상세
	@PostMapping(value="/updateCorpUsers/updateCorpUsersDetail")
    public ModelAndView updateCorpUsersDetail(UsersDomain usersDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoUpdateUsersPage+"/updateCorpUsersDetail");
		UsersDomain usersInfo = usersService.getUpdateCorpUsersDetail(usersDomain);
    	mv.addObject("usersInfo", usersInfo);

    	// 첨부파일
		FileDomain fileParam = new FileDomain();
		if(usersInfo.getReqFileSeq() > 0) {
			fileParam.setFileGrpSeq(usersInfo.getReqFileSeq());
			List<FileDomain> fileList = commonService.selectFileList(fileParam);
			mv.addObject("fileList", fileList);
		}
        return mv;
    }
	
	
	// 법인회원 정보변경관리 상태변경
	@PostMapping(value="/updateCorpUsers/updateCorpUsersStat")
	public ResponseEntity<ResponseMsg> updateCorpUsersStat(UsersDomain usersDomain){
		ResponseMsg responseMsg = usersService.updateCorpUsersStat(usersDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	

	
	
}
