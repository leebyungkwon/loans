package com.loanscrefia.member.user.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/member")
public class UserController {
	
	@Autowired private UserService userService;

	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/confirm/userConfirmPage")
	public String userConfirmPage() {
		return CosntPage.BoUserConfirmPage+"/userConfirmList";
	}
	
	//리스트
	@PostMapping(value="/confirm/userConfirmList")
	public ResponseEntity<ResponseMsg> userConfirmListAjax(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.selectUserConfirmList(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 다운로드
	@PostMapping("/confirm/userConfirmListExcelDown")
	public void userConfirmListExcelDown(UserDomain userDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
 		List<UserDomain> excelDownList = userService.selectUserConfirmList(userDomain);
 		new UtilExcel().downLoad(excelDownList, UserDomain.class, response.getOutputStream());
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/confirm/userConfirmIndvDetail")
    public ModelAndView userConfirmIndvDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmIndvDetail");
    	Map<String, Object> result 	= userService.getUserRegIndvDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/confirm/userConfirmCorpDetail")
    public ModelAndView userConfirmCorpDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/confirm/userConfirmCorpImwonDetail")
    public ModelAndView userConfirmCorpImwonDetail(UserImwonDomain userImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpImwonDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/confirm/userConfirmCorpExpertDetail")
    public ModelAndView userConfirmCorpExpertDetail(UserExpertDomain userExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpExpertDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/confirm/userConfirmCorpItDetail")
    public ModelAndView userConfirmCorpItDetail(UserItDomain userItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpItDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/confirm/userConfirmCorpEtcDetail")
    public ModelAndView userConfirmCorpEtcDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpEtcDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpEtcDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//위반이력 삭제
	@PostMapping(value="/confirm/deleteViolationInfo")
	public ResponseEntity<ResponseMsg> deleteViolationInfo(UserDomain userDomain){
		ResponseMsg responseMsg = userService.deleteViolationInfo(userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//위반이력 삭제요청
	@PostMapping(value="/confirm/applyDeleteViolationInfo")
	public ResponseEntity<ResponseMsg> applyDeleteViolationInfo(UserDomain userDomain){
		ResponseMsg responseMsg = userService.applyDeleteViolationInfo(userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/user/userRegPage")
	public String userRegPage() {
		return CosntPage.BoUserRegPage+"/userRegList";
	}
	
	//리스트
	@PostMapping(value="/user/userRegList")
	public ResponseEntity<ResponseMsg> userRegListAjax(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.selectUserRegList(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//승인요청
	@PostMapping(value="/user/userAcceptApply")
	public ResponseEntity<ResponseMsg> userAcceptApply(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.userAcceptApply(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//승인요청 : 상세에서 모집인 정보 수정 후 승인요청했을 때 모두 반영될 수 있도록
	@PostMapping(value="/user/userAcceptApply2")
	public ResponseEntity<ResponseMsg> userAcceptApply2(@RequestParam(value = "files", required = false) MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.userAcceptApply2(files, userDomain, fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 업로드 팝업창
	@GetMapping(value="/user/userRegExcelPopup")
	public ModelAndView userRegExcelPopup() {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/userRegExcelPopup");
        return mav;
	}
	
	//등록 처리(엑셀 업로드) : 개인
	@PostMapping(value="/user/insertUserRegIndvInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegIndvInfoByExcel(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = userService.insertUserRegIndvInfoByExcel(files, userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인
	@PostMapping(value="/user/insertUserRegCorpInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpInfoByExcel(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpInfoByExcel(files, userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 대표자 및 임원
	@PostMapping(value="/user/insertUserRegCorpImwonInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpImwonInfoByExcel(@RequestParam("files") MultipartFile[] files, UserImwonDomain userImwonDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpImwonInfoByExcel(files, userImwonDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(수동) : 법인 > 대표자 및 임원
	@PostMapping(value="/user/insertUserRegCorpImwonInfo")
	public ResponseEntity<ResponseMsg> insertUserRegCorpImwonInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, @Valid UserImwonDomain userImwonDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpImwonInfo(files, userImwonDomain, fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 전문인력
	@PostMapping(value="/user/insertUserRegCorpExpertInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpExpertInfoByExcel(@RequestParam("files") MultipartFile[] files, UserExpertDomain userExpertDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpExpertInfoByExcel(files, userExpertDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(수동) : 법인 > 전문인력
	@PostMapping(value="/user/insertUserRegCorpExpertInfo")
	public ResponseEntity<ResponseMsg> insertUserRegCorpExpertInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, @Valid UserExpertDomain userExpertDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpExpertInfo(files, userExpertDomain, fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(엑셀 업로드) : 법인 > 전산인력
	@PostMapping(value="/user/insertUserRegCorpItInfoByExcel")
	public ResponseEntity<ResponseMsg> insertUserRegCorpItInfoByExcel(@RequestParam("files") MultipartFile[] files, UserItDomain userItDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpItInfoByExcel(files, userItDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 처리(수동) : 법인 > 전산인력
	@PostMapping(value="/user/insertUserRegCorpItInfo")
	public ResponseEntity<ResponseMsg> insertUserRegCorpItInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, @Valid UserItDomain userItDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.insertUserRegCorpItInfo(files, userItDomain, fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/user/userRegIndvDetail")
    public ModelAndView userRegIndvDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegIndvDetail");
    	Map<String, Object> result 	= userService.getUserRegIndvDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/user/userRegCorpDetail")
    public ModelAndView userRegCorpDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/user/userRegCorpImwonDetail")
    public ModelAndView userRegCorpImwonDetail(UserImwonDomain userImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpImwonDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/user/userRegCorpExpertDetail")
    public ModelAndView userRegCorpExpertDetail(UserExpertDomain userExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpExpertDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/user/userRegCorpItDetail")
    public ModelAndView userRegCorpItDetail(UserItDomain userItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpItDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/user/userRegCorpEtcDetail")
    public ModelAndView userRegCorpEtcDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserRegPage+"/userRegCorpEtcDetail");
    	Map<String, Object> result 	= userService.getUserRegCorpEtcDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//수정 처리
	@PostMapping(value="/user/updateUserRegInfo")
	public ResponseEntity<ResponseMsg> updateUserRegInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegInfo(files,userDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/user/updateUserRegCorpImwonInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpImwonInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, @Valid UserImwonDomain userImwonDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegCorpImwonInfo(files,userImwonDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 전문인력 탭
	@PostMapping(value="/user/updateUserRegCorpExpertInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpExpertInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, @Valid UserExpertDomain userExpertDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegCorpExpertInfo(files,userExpertDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//수정 처리 : 법인 > 전산인력 탭
	@PostMapping(value="/user/updateUserRegCorpItInfo")
	public ResponseEntity<ResponseMsg> updateUserRegCorpItInfo(@RequestParam(value = "files", required = false) MultipartFile[] files, @Valid UserItDomain userItDomain, FileDomain fileDomain){
		ResponseMsg responseMsg = userService.updateUserRegCorpItInfo(files,userItDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//작성 영역 추가 : 법인 > 대표자 및 임원관련사항 탭
	@GetMapping(value="/user/callUserRegCorpImwonForm")
	public ModelAndView callUserRegCorpImwonForm(UserImwonDomain userImwonDomain){
		ModelAndView mv 			= new ModelAndView(CosntPage.Include+"/userRegCorpImwon");
    	Map<String, Object> result 	= userService.getUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
	}
	
	//작성 영역 추가 : 법인 > 전문인력 탭
	@GetMapping(value="/user/callUserRegCorpExpertForm")
	public ModelAndView callUserRegCorpExpertForm(UserExpertDomain userExpertDomain){
		ModelAndView mv 			= new ModelAndView(CosntPage.Include+"/userRegCorpExpert");
    	Map<String, Object> result 	= userService.getUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
	}
	
	//작성 영역 추가 : 법인 > 전산인력 탭
	@GetMapping(value="/user/callUserRegCorpItForm")
	public ModelAndView callUserRegCorpItForm(UserItDomain userItDomain){
		ModelAndView mv 			= new ModelAndView(CosntPage.Include+"/userRegCorpIt");
    	Map<String, Object> result 	= userService.getUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
	}
	
	//삭제 처리 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/user/deleteUserRegCorpImwonInfo")
	public ResponseEntity<ResponseMsg> deleteUserRegCorpImwonInfo(UserImwonDomain userImwonDomain){
		ResponseMsg responseMsg = userService.deleteUserRegCorpImwonInfo(userImwonDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//삭제 처리 : 법인 > 전문인력 탭
	@PostMapping(value="/user/deleteUserRegCorpExpertInfo")
	public ResponseEntity<ResponseMsg> deleteUserRegCorpExpertInfo(UserExpertDomain userExpertDomain){
		ResponseMsg responseMsg = userService.deleteUserRegCorpExpertInfo(userExpertDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//삭제 처리 : 법인 > 전산인력 탭
	@PostMapping(value="/user/deleteUserRegCorpItInfo")
	public ResponseEntity<ResponseMsg> deleteUserRegCorpItInfo(UserItDomain userItDomain){
		ResponseMsg responseMsg = userService.deleteUserRegCorpItInfo(userItDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 모집인 상태 / 처리상태 변경 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	@PostMapping(value="/user/updateUserStat")
	public ResponseEntity<ResponseMsg> updatePlRegStat(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updateUserStat(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//삭제
	@PostMapping(value="/user/deleteUserRegInfo")
	public ResponseEntity<ResponseMsg> deleteUserRegInfo(UserDomain userDomain){
		ResponseMsg responseMsg = userService.deleteUserRegInfo(userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//변경요청 페이지 : 개인
	@PostMapping(value="/confirm/userConfirmIndvChangeApply")
    public ModelAndView userConfirmIndvChangeApply(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmIndvChangeApply");
    	Map<String, Object> result 	= userService.getUserRegIndvDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//변경요청 페이지 : 법인
	@PostMapping(value="/confirm/userConfirmCorpChangeApply")
    public ModelAndView userConfirmCorpChangeApply(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpChangeApply");
    	Map<String, Object> result 	= userService.getUserRegCorpDetail(userDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//변경요청 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/confirm/userConfirmCorpImwonChangeApply")
	public ModelAndView userConfirmCorpImwonChangeApply(UserImwonDomain userImwonDomain) {
		ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpImwonChangeApply");
		Map<String, Object> result 	= userService.getUserRegCorpImwonDetail(userImwonDomain);
		mv.addObject("result", result);
		return mv;
	}
	
	//변경요청 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/confirm/userConfirmCorpExpertChangeApply")
	public ModelAndView userConfirmCorpExpertChangeApply(UserExpertDomain userExpertDomain) {
		ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpExpertChangeApply");
		Map<String, Object> result 	= userService.getUserRegCorpExpertDetail(userExpertDomain);
		mv.addObject("result", result);
		return mv;
	}
	
	//변경요청 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/confirm/userConfirmCorpItChangeApply")
	public ModelAndView userConfirmCorpItChangeApply(UserItDomain userItDomain) {
		ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpItChangeApply");
		Map<String, Object> result 	= userService.getUserRegCorpItDetail(userItDomain);
		mv.addObject("result", result);
		return mv;
	}
	
	//변경요청 페이지 : 법인 > 기타 탭
	@PostMapping(value="/confirm/userConfirmCorpEtcChangeApply")
	public ModelAndView userConfirmCorpEtcChangeApply(UserDomain userDomain) {
		ModelAndView mv 			= new ModelAndView(CosntPage.BoUserConfirmPage+"/userConfirmCorpEtcChangeApply");
		Map<String, Object> result 	= userService.getUserRegCorpEtcDetail(userDomain);
		mv.addObject("result", result);
		return mv;
	}
	
	//즉시취소
	@PostMapping(value="/confirm/userCancel")
    public ResponseEntity<ResponseMsg> userCancel(UserDomain userDomain) {
		ResponseMsg responseMsg = userService.userCancel(userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
    }
	
	//변경요청
	@PostMapping(value="/confirm/userChangeApply")
    public ResponseEntity<ResponseMsg> userChangeApply(@RequestParam(value = "files", required = false) MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain) {
		ResponseMsg responseMsg = userService.userChangeApply(files,userDomain,fileDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
    }
	
	//해지요청 
	@PostMapping(value="/confirm/userDropApply")
	public ResponseEntity<ResponseMsg> userDropApply(UserDomain userDomain){
		ResponseMsg responseMsg = userService.userDropApply(userDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//이력보기
	@GetMapping(value="/user/userStepHistoryPopup")
	public ModelAndView userStepHistoryPopup(UserDomain userDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/userStepHistoryPopup");
		mav.addObject("stepHisList", userService.selectUserStepHistoryList(userDomain));
        return mav;
	}
	
	
	
	
	
	
}
