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
	
	
	
	
	// ------------------------------- 회원사 관리 영역 --------------------------------------- //
	
	// 회원사 코드 관리
	@GetMapping(value="/company/companyCodePage")
	public String companyCodePage() {
		return CosntPage.BoCompanyCodePage+"/companyCodeList";
	}
	
	// 회원사 코드 관리 - 리스트
	@PostMapping(value="/company/companyCodeList")
	public ResponseEntity<ResponseMsg> companyCodeList(CompanyDomain companyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(companyService.selectCompanyCodeList(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 회원사 코드 관리 - 디테일 리스트
	@GetMapping(value="/company/companyCodeDetailPage")
	public ModelAndView getCompanyCodeDetail(CompanyDomain companyDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoCompanyCodePage+"/companyCodeDetail");
		return mv;
	}
	
	// 회원사 코드 관리 - 디테일 수정 리스트
	@PostMapping(value="/company/companyCodeDetailInsPage")
	public ModelAndView companyCodeDetailIns(CompanyDomain companyDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoCompanyCodePage+"/companyCodeDetail");
		
		CompanyDomain companyCodeInfo = companyService.getCompanyCodeDetail(companyDomain);
		mv.addObject("companyCodeInfo", companyCodeInfo);
		
		return mv;
	}
	
	// 회원사 코드 관리 - 디테일 리스트 -> Insert (글 등록)
	@PostMapping(value="/company/saveCompanyCodeDetail")
	public ResponseEntity<ResponseMsg> saveCompanyCodeDetail(@Valid CompanyDomain companyDomain, BindingResult bindingResult) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		
		// 2021-05-31 법인번호 암호화 진행예정
		// insert 및 update쿼리 -> REPLACE함수 제거 -> java에서 replace 제거 후 진행
		String encMerchantNo = CryptoUtil.encrypt(companyDomain.getPlMerchantNo()); // 암호화
		companyDomain.setPlMerchantNo(encMerchantNo);
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
	@PostMapping(value="/company/updCompanyCodeDetail")
	public ResponseEntity<ResponseMsg> updCompanyCodeDetail(@Valid CompanyDomain companyDomain, BindingResult bindingResult) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);		
		if(bindingResult.hasErrors()) {
			responseMsg = new ResponseMsg(HttpStatus.OK, null, null);
	    	responseMsg.setData(bindingResult.getAllErrors());
	    	return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		
		// 2021-05-31 법인번호 암호화 진행예정
		// insert 및 update쿼리 -> REPLACE함수 제거 -> java에서 replace 제거 후 진행
		String encMerchantNo = CryptoUtil.encrypt(companyDomain.getPlMerchantNo()); // 암호화
		companyDomain.setPlMerchantNo(encMerchantNo);
		
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
	@PostMapping(value="/company/delCompanyCodeDetail")
	public ResponseEntity<ResponseMsg> delCompanyCodeDetail(CompanyDomain companyDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(companyService.delCompanyCodeDetail(companyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	 
}