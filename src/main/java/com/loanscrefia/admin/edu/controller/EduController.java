package com.loanscrefia.admin.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.admin.edu.domain.EduDomain;
import com.loanscrefia.admin.edu.service.EduService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;


@Controller
@RequestMapping(value="/admin/edu")
public class EduController {

	@Autowired private EduService eduService;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 여신금융협회 시스템 > 교육이수번호 조회
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	// 리스트 페이지
	@GetMapping(value="/eduPage")
	public String eduPage() {
		return CosntPage.BoEduPage+"/eduList";
	}
	
	// 리스트
	@PostMapping(value="/eduList")
	public ResponseEntity<ResponseMsg> selectEduList(EduDomain eduDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(eduService.selectEduList(eduDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
