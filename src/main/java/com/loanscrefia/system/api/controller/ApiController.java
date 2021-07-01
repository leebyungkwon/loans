package com.loanscrefia.system.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.api.service.ApiService;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/system")
public class ApiController {
	
	@Autowired
	private KfbApiService kfbApiService;
	
	@Autowired
	private ApiService apiService;

	// API관리 페이지
	@GetMapping("/api/apiPage")
	public ModelAndView apiPage() {
		ModelAndView mv = new ModelAndView(CosntPage.BoMainPage + "api/apiList");
		return mv;
	}

	// API리스트
	@PostMapping(value="/api/selectApiList")
	public ResponseEntity<ResponseMsg> selectApiList(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(apiService.selectApiList(kfbApiDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API네트워크 및 서버상태 확인
	@PostMapping(value="/api/getHealthCheck")
	public ResponseEntity<ResponseMsg> getHealthCheck(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		responseMsg.setData(kfbApiService.getHealthCheck());
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API코드발급
	@PostMapping(value="/api/getApiCode")
	public ResponseEntity<ResponseMsg> getApiCode(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		responseMsg.setData(kfbApiService.getAuthCode());
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API토큰발급
	@PostMapping(value="/api/getAuthToken")
	public ResponseEntity<ResponseMsg> getAuthToken(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		responseMsg.setData(kfbApiService.getAuthToken());
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}