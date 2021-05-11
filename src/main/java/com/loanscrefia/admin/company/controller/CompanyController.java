package com.loanscrefia.admin.company.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	
	// 협회 - 회원사 당담자 상세 페이지	  
	@GetMapping(value="/companyDetail")
	public ModelAndView companyDetail() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoCompanyPage+"/companyDetail");
        return mv;
	}
	
	// 협회 - 회원사 당담자 상세 페이지
	@PostMapping(value="/companyDetail")
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
	
	
	
	
	// ------------------------------- 회원사 관리 영역 --------------------------------------- //
	
	// 회원사 코드 관리
	@GetMapping(value="/companyCodePage")
	public String companyCodePage() {
		return CosntPage.BoCompanyCodePage+"/companyCodeList";
	}
	
	// 회원사 코드 관리 - 리스트
	@PostMapping(value="/companyCodeList")
	public ResponseEntity<ResponseMsg> companyCodeList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.selectCompanyCodeList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 회원사 코드 관리 - 디테일 리스트
	@PostMapping(value="/companyCodeDetailPage")
	public ModelAndView getCompanyCodeDetail(CompanyDomain companyDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoCompanyCodePage+"/companyCodeDetail");
		return mv;
	}
	
	// 회원사 코드 관리 - 디테일 수정 리스트
	@PostMapping(value="/companyCodeDetailInsPage")
	public ModelAndView companyCodeDetailIns(CompanyDomain companyDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoCompanyCodePage+"/companyCodeDetail");
		
		CompanyDomain companyCodeInfo = companyService.getCompanyCodeDetail(companyDomain);
		mv.addObject("companyCodeInfo", companyCodeInfo);
		
		return mv;
	}
	
	// 회원사 코드 관리 - 디테일 리스트 -> Insert (글 등록)
	@PostMapping(value="/saveCompanyCodeDetail")
	public ResponseEntity<ResponseMsg> saveCompanyCodeDetail(@Valid CompanyDomain companyDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		
		int count = companyService.plMerchantNoCheck(companyDomain);
		
		if(count > 0) {
			responseMsg = new ResponseMsg(HttpStatus.OK, "COM0001", "해당 법인등록번호가 이미 등록되어 있습니다.");
			responseMsg.setData("0");
		}else {
			responseMsg = companyService.saveCompanyCodeDetail(companyDomain);
			responseMsg.setData("1");
		}
		
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	// 회원사 코드 관리 - 디테일 리스트 -> Update (글 수정)
	@PostMapping(value="/updCompanyCodeDetail")
	public ResponseEntity<ResponseMsg> updCompanyCodeDetail(CompanyDomain companyDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		
		int count = companyService.plMerchantNoCheck(companyDomain);
		
		if(count > 0) {
			responseMsg = new ResponseMsg(HttpStatus.OK, "COM0001", "해당 법인등록번호가 이미 등록되어 있습니다.");
			responseMsg.setData("0");
		}else {
			responseMsg = companyService.updCompanyCodeDetail(companyDomain);
			responseMsg.setData("1");
		}
		
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	// 회원사 코드 관리 - 디테일 리스트 -> Delete (글 삭제)
	@PostMapping(value="/delCompanyCodeDetail")
	public ResponseEntity<ResponseMsg> delCompanyCodeDetail(@Valid CompanyDomain companyDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(companyService.delCompanyCodeDetail(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	 
}