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
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
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
	@PostMapping(value="/corpImwonExcelUpload")
	public ResponseEntity<ResponseMsg> corpImwonExcelUpload(@RequestParam("files") MultipartFile[] files, UserImwonDomain userImwonDomain){
		ResponseMsg responseMsg = userService.corpImwonExcelUpload(files, userImwonDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 전문인력
	@PostMapping(value="/corpExpertExcelUpload")
	public ResponseEntity<ResponseMsg> corpExpertExcelUpload(@RequestParam("files") MultipartFile[] files, UserExpertDomain userExpertDomain){
		ResponseMsg responseMsg = userService.corpExpertExcelUpload(files, userExpertDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 전산인력
	@PostMapping(value="/corpItExcelUpload")
	public ResponseEntity<ResponseMsg> corpItExcelUpload(@RequestParam("files") MultipartFile[] files, UserItDomain userItDomain){
		ResponseMsg responseMsg = userService.corpItExcelUpload(files, userItDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
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
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/userRegCorpDetail")
    public ModelAndView userRegCorpDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/userRegCorpImwonDetail")
    public ModelAndView userRegCorpImwonDetail(UserImwonDomain userImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpImwonDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/userRegCorpExpertDetail")
    public ModelAndView userRegCorpExpertDetail(UserExpertDomain userExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpExpertDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/userRegCorpItDetail")
    public ModelAndView userRegCorpItDetail(UserItDomain userItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpItDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/userRegCorpEtcDetail")
    public ModelAndView userRegCorpEtcDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpEtcDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpEtcDetail(userDomain);
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
	
	//수정 처리 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/updateUserRegCorpImwonInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpImwonInfo(@RequestParam("files") MultipartFile[] files, UserImwonDomain userImwonDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updateUserRegCorpImwonInfo(files,userImwonDomain,fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 전문인력 탭
	@PostMapping(value="/updateUserRegCorpExpertInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpExpertInfo(@RequestParam("files") MultipartFile[] files, UserExpertDomain userExpertDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updateUserRegCorpExpertInfo(files,userExpertDomain,fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 전산인력 탭
	@PostMapping(value="/updateUserRegCorpItInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpItInfo(@RequestParam("files") MultipartFile[] files, UserItDomain userItDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updateUserRegCorpItInfo(files,userItDomain,fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
