package com.loanscrefia.admin.company.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.company.domain.CompanyDomain;
import com.loanscrefia.admin.company.service.CompanyService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;

import sinsiway.CryptoUtil;

@Controller
@RequestMapping(value="/admin")
public class CompanyController {
	
	
	@Autowired private CompanyService companyService;
	@Autowired private CommonService commonService;
	@Autowired UtilFile utilFile;
	
	// 협회 - 회원사 담당자 조회 페이지
	@GetMapping(value="/mng/companyPage")
	public String companyPage() {
		return CosntPage.BoCompanyPage+"/companyList";
	}
	
	// 협회 - 회원사 담당자 리스트 페이지
	@PostMapping(value="/mng/companyList")
	public ResponseEntity<ResponseMsg> companyList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.selectCompanyList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 협회 - 회원사 당담자 상세 페이지
	@PostMapping(value="/mng/companyDetail")
    public ModelAndView companyDetail(CompanyDomain companyDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.BoCompanyPage+"/companyDetail");
    	//상세
    	CompanyDomain companyDetail	= companyService.getCompanyDetail(companyDomain);
    	mv.addObject("companyDetail", companyDetail);
    	//파일 업로드
    	FileDomain file = new FileDomain();
    	file.setFileSeq(companyDetail.getFileSeq());
    	file = commonService.getFile(file);
    	mv.addObject("file", file);
    	
        return mv;
    }
	
	//처리 상태변경
	@PostMapping(value="/mng/updateCompanyStat")
	public ResponseEntity<ResponseMsg> updateCompanyStat(@Valid CompanyDomain companyDomain, BindingResult bindingResult){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
    	responseMsg.setData(companyService.updateCompanyStat(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//삭제 
	@PostMapping(value="/mng/deleteCompany")
	public ResponseEntity<ResponseMsg> deleteCompany(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.deleteCompany(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 업로드
	@PostMapping("/mng/excelDown")
	public void writeExcel(CompanyDomain companyDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
 		List<CompanyDomain> b = companyService.selectCompanyList(companyDomain);
 		new UtilExcel().downLoad(b, CompanyDomain.class, response.getOutputStream());
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 회원사 관리
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/company/companyCodePage")
	public String companyCodePage() {
		return CosntPage.BoCompanyCodePage+"/companyCodeList";
	}
	
	//리스트
	@PostMapping(value="/company/companyCodeList")
	public ResponseEntity<ResponseMsg> companyCodeList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.selectCompanyCodeList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//등록 팝업
	@GetMapping(value="/company/companyCodeSavePopup")
	public ModelAndView companyCodeSavePopup() {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/companyCodeSavePopup");
        return mav;
	}
	
	//등록
	@PostMapping(value="/company/saveCompanyCode")
	public ResponseEntity<ResponseMsg> saveCompanyCodeDetail(CompanyDomain companyDomain, BindingResult bindingResult) {
		ResponseMsg responseMsg = companyService.saveCompanyCode(companyDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	
	 
}