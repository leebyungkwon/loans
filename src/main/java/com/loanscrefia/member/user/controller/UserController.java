package com.loanscrefia.member.user.controller;

import java.util.Map;

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

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.service.UserService;

@Controller
@RequestMapping(value="/member/user")
public class UserController {
	
	@Autowired private UserService userService;

	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	
	//리스트
	
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/userRegPage")
	public String userRegPage() {
		return CosntPage.BoUserRegPage+"/userRegList";
	}
	
	//리스트
	@PostMapping(value="/userRegList")
	public ResponseEntity<ResponseMsg> userRegListAjax(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.selectUserRegList(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 업로드 팝업창
	@GetMapping(value="/userRegExcelPopup")
	public ModelAndView userRegExcelPopup() {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/userRegExcelPopup");
        return mav;
	}
	
	//등록 처리(엑셀 업로드) : 개인
	@PostMapping(value="/indvExcelUpload")
	public ResponseEntity<ResponseMsg> indvExcelUpload(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = userService.indvExcelUpload(files, userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인
	@PostMapping(value="/corpExcelUpload")
	public ResponseEntity<ResponseMsg> corpExcelUpload(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = userService.corpExcelUpload(files, userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 대표자 및 임원
	
	//등록 처리(엑셀 업로드) : 법인 > 업무 수행이 필요한 전문성을 갖춘 인력에 관한 사항
	
	//등록 처리(엑셀 업로드) : 법인 > 전산 설비 운영,유지 및 관리를 전문적으로 수행할 수 있는 인력에 관한 사항
	
	
	
	
	
	
	//처리상태 변경
	@PostMapping(value="/updatePlRegStat")
	public ResponseEntity<ResponseMsg> updatePlRegStat(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updatePlRegStat(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/userRegIndvDetail")
    public ModelAndView userRegIndvDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegIndvDetail");
    	Map<String, Object> result 	= userService.getUserRegIndvDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인
	@PostMapping(value="/userRegCorpDetail")
    public ModelAndView userRegCorpDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//수정 처리
	@PostMapping(value="/updateUserRegInfo")
	public ResponseEntity<ResponseMsg> updateUserRegInfo(@RequestParam("files") MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updateUserRegInfo(files,userDomain,fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
