package com.loanscrefia.common.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.common.domain.VersionDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.code.domain.CodeDomain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CommonController {
	
	@Autowired private CommonService commonService;
	
	@GetMapping(value="/isConnecting")
	public ResponseEntity<ResponseMsg> isConnecting(HttpServletRequest request){
		Long logId = (Long) request.getAttribute("lid");
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,"A0001" ,"connected..");
		responseMsg.setLogId(logId);
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	@GetMapping(value="/staticVersion")
	public ResponseEntity<ResponseMsg> staticVersion(Long verId, String versionNm, Long versionNum, HttpServletRequest request){
		Long logId = (Long) request.getAttribute("lid");
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,"A0001" ,"staticVersion up..");
		VersionDomain versionDomain = new VersionDomain();
		versionDomain.setVerId(verId);
		versionDomain.setVersionNm(versionNm);
		versionDomain.setVersionNum(versionNum);
		commonService.verSave(versionDomain);
		responseMsg.setLogId(logId);
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	@GetMapping("/common/openPopup")
    public ModelAndView openPopup(@RequestParam String url) { 
    	ModelAndView mv = new ModelAndView(url);
        return mv;
    }
	
	
	// 2021-04-16 commonCodeList - selectBox
	@PostMapping(value="/common/selectCommonCodeList")
	public ResponseEntity<ResponseMsg> selectCommonCodeList(CodeDomain code){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
    	responseMsg.setData(commonService.selectCommonCodeList(code));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
}