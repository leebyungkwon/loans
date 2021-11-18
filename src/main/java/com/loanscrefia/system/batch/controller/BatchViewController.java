package com.loanscrefia.system.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.service.BatchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BatchViewController {

	@Autowired 
	private BatchService batchService;
	
	// Batch관리 페이지
	@GetMapping("/batch/batch")
	public ModelAndView batch() {
		ModelAndView mv = new ModelAndView(CosntPage.BoMainPage + "batch/batch");
		mv.addObject("batchList", batchService.selectAllBatchList());
		return mv;
	}
	
	@PostMapping("/batch/batchErrHist")
	public ResponseEntity<ResponseMsg> batchErrHist(BatchDomain batch) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(batchService.selectBatchErrHistList(batch));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	@PostMapping("/batch/batchErr")
	public ResponseEntity<ResponseMsg> batchErr(BatchDomain batch) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(batchService.selectBatchErrList(batch));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
}
