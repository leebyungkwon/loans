package com.loanscrefia.member.user.controller;

import java.util.List;

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
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.service.UserService;

@Controller
@RequestMapping(value="/member/user")
public class UserController {
	
	@Autowired private UserService userService;
	@Autowired private CommonService commonService;

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
	
	//등록 처리(엑셀 업로드)
	@PostMapping(value="/userRegInfoExcelUpload")
	public ResponseEntity<ResponseMsg> userRegInfoExcelUpload(@RequestParam("files") MultipartFile[] files, UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.insertUserRegInfoByExcel(files,userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//선택 승인요청 -> 첨부파일 하나라도 없으면 요청 불가
	@PostMapping(value="/updatePlRegStat")
	public ResponseEntity<ResponseMsg> updatePlRegStat(UserDomain userDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(userService.updatePlRegStat(userDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//상세 페이지
	@PostMapping(value="/userRegDetail")
    public ModelAndView userRegDetail(UserDomain userDomain) {
    	ModelAndView mv 			= new ModelAndView();
    	
    	//상세
    	UserDomain userRegInfo 		= userService.getUserRegDetail(userDomain);
    	
    	//첨부파일 리스트
    	FileDomain param 			= new FileDomain();
    	param.setFileGrpSeq(userRegInfo.getFileSeq());
    	List<FileDomain> fileList 	= commonService.selectFileList(param);
    	
    	//전달
    	mv.addObject("userRegInfo", userRegInfo);
    	mv.addObject("fileList", fileList);
    	
    	//페이지 분기
    	if(userRegInfo.getPlClass().equals("1")) {
    		//개인
    		mv.setViewName(CosntPage.BoUserRegPage+"/userRegIndvDetail");
    	}else {
    		//법인
    		mv.setViewName(CosntPage.BoUserRegPage+"/userRegCorpDetail");
    	}
    	
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
