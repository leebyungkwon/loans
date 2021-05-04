package com.loanscrefia.admin.company.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.repository.CompanyRepository;
import com.loanscrefia.admin.company.service.CompanyService;
import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;

import lombok.extern.java.Log;
@Log
@Controller
@RequestMapping(value="/admin/company")
public class CompanyController {
	
	
	@Autowired private CompanyService companyService;
	@Autowired private CommonService commonService;
	@Autowired UtilFile utilFile;
	
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
    	mv.addObject("companyDetail", companyDetail);
    	
    	
    	FileDomain file = new FileDomain();
    	file.setFileSeq(companyDetail.getFileSeq());
    	file = commonService.getFile(file);
    	mv.addObject("file", file);
    	
        return mv;
    }
	
	//
	@PostMapping(value="/updateCompanyStat")
	public ResponseEntity<ResponseMsg> updateCompanyStat(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.updateCompanyStat(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//삭제 
	@PostMapping(value="/deleteCompany")
	public ResponseEntity<ResponseMsg> deleteCompany(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.deleteCompany(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 업로드
	@PostMapping("/excelDown")
	public void writeExcel(CompanyDomain companyDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
 		List<CompanyDomain> b = companyService.selectCompanyList(companyDomain);
 		new UtilExcel().downLoad(b, CompanyDomain.class, response.getOutputStream());
	}
	 
}
