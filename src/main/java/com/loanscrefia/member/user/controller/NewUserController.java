package com.loanscrefia.member.user.controller;

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
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExcelDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
import com.loanscrefia.member.user.service.NewUserService;
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/member")
public class NewUserController {
	
	@Autowired private NewUserService userService;
	
	// 2021-10-12 고도화 - 모집인 확인처리 페이지(회원사)
	@GetMapping(value="/newUser/newUserRegPage")
	public String newUserRegPage() {
		return CosntPage.BoNewUserRegPage+"/newUserRegList";
	}
	
	// 2021-10-12 고도화 - 모집인 확인처리 리스트(회원사)
	@PostMapping(value="/newUser/newUserRegList")
	public ResponseEntity<ResponseMsg> newUserRegList(NewUserDomain newUserDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.selectNewUserRegList(newUserDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 개인
	@PostMapping(value="/newUser/newUserRegIndvDetail")
    public ModelAndView newUserRegIndvDetail(NewUserDomain newUserDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewUserRegPage+"/newUserRegIndvDetail");
    	Map<String, Object> result 	= userService.getNewUserRegIndvDetail(newUserDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/newUser/newUserRegCorpDetail")
    public ModelAndView newUserRegCorpDetail(NewUserDomain newUserDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewUserRegPage+"/newUserRegCorpDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpDetail(newUserDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/newUser/newUserRegCorpImwonDetail")
    public ModelAndView newUserRegCorpImwonDetail(UserImwonDomain userImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewUserRegPage+"/newUserRegCorpImwonDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/newUser/newUserRegCorpExpertDetail")
    public ModelAndView newUserRegCorpExpertDetail(UserExpertDomain userExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewUserRegPage+"/newUserRegCorpExpertDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/newUser/newUserRegCorpItDetail")
    public ModelAndView newUserRegCorpItDetail(UserItDomain userItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewUserRegPage+"/newUserRegCorpItDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 확인처리 상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/newUser/newUserRegCorpEtcDetail")
    public ModelAndView newUserRegCorpEtcDetail(NewUserDomain newUserDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewUserRegPage+"/newUserRegCorpEtcDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpEtcDetail(newUserDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 상태 변경
	@PostMapping(value="/newUser/newUserApply")
	public ResponseEntity<ResponseMsg> newUserApply(NewUserDomain newUserDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.newUserApply(newUserDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 페이지(회원사)
	@GetMapping(value="/newConfirm/newConfirmPage")
	public String newConfirmPage() {
		return CosntPage.BoNewConfirmPage+"/newConfirmList";
	}
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 리스트
	@PostMapping(value="/newConfirm/newConfirmList")
	public ResponseEntity<ResponseMsg> newConfirmList(NewUserDomain newUserDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.selectNewConfirmList(newUserDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-12 고도화 - 모집인 확인처리 엑셀 다운로드
	@PostMapping("/newConfirm/newConfirmExcelDown")
	public void newConfirmExcelDown(NewUserDomain newUserDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		// 2021-07-27 페이징 false
		newUserDomain.setIsPaging("false");
 		List<NewUserDomain> excelDownList = userService.selectNewConfirmList(newUserDomain);
 		new UtilExcel().downLoad(excelDownList, UserExcelDomain.class, response.getOutputStream());
	}
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 상세 페이지 : 개인
	@PostMapping(value="/newConfirm/newConfirmIndvDetail")
    public ModelAndView newConfirmIndvDetail(NewUserDomain newUserDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewConfirmPage+"/newConfirmIndvDetail");
    	Map<String, Object> result 	= userService.getNewUserRegIndvDetail(newUserDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/newConfirm/newConfirmCorpDetail")
    public ModelAndView newConfirmCorpDetail(NewUserDomain newUserDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewConfirmPage+"/newConfirmCorpDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpDetail(newUserDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/newConfirm/newConfirmCorpImwonDetail")
    public ModelAndView newConfirmCorpImwonDetail(UserImwonDomain userImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewConfirmPage+"/newConfirmCorpImwonDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpImwonDetail(userImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/newConfirm/newConfirmCorpExpertDetail")
    public ModelAndView newConfirmCorpExpertDetail(UserExpertDomain userExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewConfirmPage+"/newConfirmCorpExpertDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpExpertDetail(userExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/newConfirm/newConfirmCorpItDetail")
    public ModelAndView newConfirmCorpItDetail(UserItDomain userItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewConfirmPage+"/newConfirmCorpItDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpItDetail(userItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/newConfirm/newConfirmCorpEtcDetail")
    public ModelAndView newConfirmCorpEtcDetail(NewUserDomain newUserDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewConfirmPage+"/newConfirmCorpEtcDetail");
    	Map<String, Object> result 	= userService.getNewUserRegCorpEtcDetail(newUserDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-12 고도화 - 모집인 조회 및 해지 - 해지요청
	@PostMapping(value="/newConfirm/newUserDropApply")
	public ResponseEntity<ResponseMsg> newUserDropApply(NewUserDomain newUserDomain){
		ResponseMsg responseMsg = userService.newUserDropApply(newUserDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-25 고도화 - 모집인 조회 및 해지 - 해지요청취소
	@PostMapping(value="/newConfirm/newUserDropApplyCancel")
	public ResponseEntity<ResponseMsg> newUserDropApplyCancel(NewUserDomain newUserDomain){
		ResponseMsg responseMsg = userService.newUserDropApplyCancel(newUserDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	//위반이력 삭제요청
	@PostMapping(value="/newConfirm/newApplyDeleteViolationInfo")
	public ResponseEntity<ResponseMsg> newApplyDeleteViolationInfo(NewUserDomain newUserDomain){
		ResponseMsg responseMsg = userService.newApplyDeleteViolationInfo(newUserDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//위반이력 삭제
	@PostMapping(value="/newConfirm/newDeleteNewUserViolationInfo")
	public ResponseEntity<ResponseMsg> newDeleteNewUserViolationInfo(NewUserDomain newUserDomain){
		ResponseMsg responseMsg = userService.newDeleteNewUserViolationInfo(newUserDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	//위반이력 등록 및 배치 insert
	@PostMapping(value="/newConfirm/newUpdateVio")
    public ResponseEntity<ResponseMsg> newUpdateVio(NewUserDomain newUserDomain) throws IOException {
		ResponseMsg responseMsg = userService.newUpdateVio(newUserDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
    }
	
	
	
}
