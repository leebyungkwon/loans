package com.loanscrefia.admin.company.controller;

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

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.service.CompanyService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;

import lombok.extern.java.Log;
@Log
@Controller
@RequestMapping(value="/admin/company")
public class CompanyController {
	
	
	@Autowired private CompanyService companyService;
	@Autowired private CommonService commonService;
	
	// 협회 - 회원사 담당자 조회 페이지
	@GetMapping(value="/companyPage")
	public String companyPage() {
		return CosntPage.BoCompanyPage+"/companyList";
	}
	
	// 협회 - 회원사 담당자 리스트 페이지
	@PostMapping(value="/companyList")
	public ResponseEntity<ResponseMsg> companyList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.selectCompanyList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 협회= 회원사 당담자 상세 페이지	  
	@GetMapping(value="/companyDetail")
	public ModelAndView companyDetail() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoCompanyPage+"/companyDetail");
        return mv;
	}
	
	@PostMapping(value="/companyDetail")
    public ModelAndView companyDetail(CompanyDomain companyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoCompanyPage+"/companyDetail");
    	//상세
    	CompanyDomain companyDetail	= companyService.getCompanyDetail(companyDomain);
    	//첨부파일 리스트
//    	FileDomain param 			= new FileDomain();
//    	param.setFileGrpSeq(companyDetail.getFileSeq());
//    	List<FileDomain> fileList 	= commonService.selectFileList(param);
//    	log.info(companyDetail);
    	//전달
    	mv.addObject("companyDetail", companyDetail);
//    	mv.addObject("fileList", fileList);
    	
        return mv;
    }
	
	//수정 처리
//	@PostMapping(value="/updateUserRegInfo")
//	public ResponseEntity<ResponseMsg> updateUserRegInfo(@RequestParam("files") MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
//		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
//    	responseMsg.setData(companyService.updateUserRegInfo(files,userDomain,fileDomain));
//		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
//	}
	
	 
}
