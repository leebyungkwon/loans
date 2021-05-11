package com.loanscrefia.member.user.controller;

import java.util.Map;

import javax.validation.Valid;

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
	
	//처리상태 변경
	@PostMapping(value="/updatePlRegStat")
	public ResponseEntity<ResponseMsg> updatePlRegStat(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updatePlRegStat(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 업로드 팝업창
	@GetMapping(value="/userRegExcelPopup")
	public ModelAndView userRegExcelPopup() {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/userRegExcelPopup");
        return mav;
	}
	
	//등록 처리(엑셀 업로드) : 개인
	@PostMapping(value="/insertUserRegIndvInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegIndvInfoByExcel(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = userService.insertUserRegIndvInfoByExcel(files, userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인
	@PostMapping(value="/insertUserRegCorpInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpInfoByExcel(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpInfoByExcel(files, userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 대표자 및 임원
	@PostMapping(value="/insertUserRegCorpImwonInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpImwonInfoByExcel(@RequestParam("files") MultipartFile[] files, UserImwonDomain userImwonDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpImwonInfoByExcel(files, userImwonDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(수동) : 법인 > 대표자 및 임원
	@PostMapping(value="/insertUserRegCorpImwonInfo")
	public ResponseEntity<ResponseMsg> insertUserRegCorpImwonInfo(@RequestParam("files") MultipartFile[] files, UserImwonDomain userImwonDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpImwonInfo(files, userImwonDomain, fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 전문인력
	@PostMapping(value="/insertUserRegCorpExpertInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpExpertInfoByExcel(@RequestParam("files") MultipartFile[] files, UserExpertDomain userExpertDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpExpertInfoByExcel(files, userExpertDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(수동) : 법인 > 전문인력
	@PostMapping(value="/insertUserRegCorpExpertInfo")
	public ResponseEntity<ResponseMsg> insertUserRegCorpExpertInfo(@RequestParam("files") MultipartFile[] files, UserExpertDomain userExpertDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpExpertInfo(files, userExpertDomain, fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 전산인력
	@PostMapping(value="/insertUserRegCorpItInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpItInfoByExcel(@RequestParam("files") MultipartFile[] files, UserItDomain userItDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpItInfoByExcel(files, userItDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(수동) : 법인 > 전산인력
	@PostMapping(value="/insertUserRegCorpItInfo")
	public ResponseEntity<ResponseMsg> insertUserRegCorpItInfo(@RequestParam("files") MultipartFile[] files, UserItDomain userItDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpItInfo(files, userItDomain, fileDomain);
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
		ResponseMsg responseMsg = userService.updateUserRegInfo(files,userDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/updateUserRegCorpImwonInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpImwonInfo(@RequestParam("files") MultipartFile[] files, @Valid UserImwonDomain userImwonDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegCorpImwonInfo(files,userImwonDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 전문인력 탭
	@PostMapping(value="/updateUserRegCorpExpertInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpExpertInfo(@RequestParam("files") MultipartFile[] files, @Valid UserExpertDomain userExpertDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegCorpExpertInfo(files,userExpertDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 전산인력 탭
	@PostMapping(value="/updateUserRegCorpItInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpItInfo(@RequestParam("files") MultipartFile[] files, @Valid UserItDomain userItDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegCorpItInfo(files,userItDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//작성 영역 추가 : 법인 > 대표자 및 임원관련사항 탭
	@GetMapping(value="/callUserRegCorpImwonForm")
	public ModelAndView callUserRegCorpImwonForm(UserImwonDomain userImwonDomain){
		ModelAndView mv 			= new ModelAndView(CosntPage.Include+"/userRegCorpImwon");
    	Map<String, Object> result 	= userService.getUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
	}
	
	//작성 영역 추가 : 법인 > 전문인력 탭
	@GetMapping(value="/callUserRegCorpExpertForm")
	public ModelAndView callUserRegCorpExpertForm(UserExpertDomain userExpertDomain){
		ModelAndView mv 			= new ModelAndView(CosntPage.Include+"/userRegCorpExpert");
    	Map<String, Object> result 	= userService.getUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
	}
	
	//작성 영역 추가 : 법인 > 전산인력 탭
	@GetMapping(value="/callUserRegCorpItForm")
	public ModelAndView callUserRegCorpItForm(UserItDomain userItDomain){
		ModelAndView mv 			= new ModelAndView(CosntPage.Include+"/userRegCorpIt");
    	Map<String, Object> result 	= userService.getUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
	}
	
	
	
	
}
