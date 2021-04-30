package com.loanscrefia.common.common.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.VersionDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CommonController {
	
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired ResourceLoader resourceLoader;
	
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
	public ResponseEntity<ResponseMsg> selectCommonCodeList(CodeDtlDomain codeDtlDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
    	responseMsg.setData(codeService.selectCodeDtlList(codeDtlDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 2021-04-26 selectCompanyCodeList - selectBox
	@PostMapping(value="/common/selectCompanyCodeList")
	public ResponseEntity<ResponseMsg> selectCompanyCodeList(CodeDtlDomain codeDtlDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
    	responseMsg.setData(commonService.selectCompanyCodeList(codeDtlDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	@PostMapping("/common/fileDown")
	public ResponseEntity<Resource> fileDown(@RequestParam int fileSeq, @RequestHeader("User-Agent") String userAgent,  HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileSeq(fileSeq);

		// 첨부파일번호가 하나인 경우
		FileDomain getFile = commonService.getFile(fileDomain);
		String fileName = getFile.getFileSaveNm()+ "." + getFile.getFileExt();
 		try {
 			
 			Resource resource = resourceLoader.getResource("classpath:\\static\\upload\\"+getFile.getFilePath()+"\\"+ fileName);
 			//파일이 없는 경우 fileNotFoundException error가 난다.
 			File resultFile = resource.getFile();
 			
 			String orgfileName = getFile.getFileOrgNm()+ "." + getFile.getFileExt();
			String downloadName = URLEncoder.encode(orgfileName,"UTF-8").replace("\\+", "%20");
			
			String test = new String(orgfileName.getBytes("UTF-8"), "ISO-8859-1");

 			return ResponseEntity.ok()
 					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" +downloadName)	//다운 받아지는 파일 명 설정
 					.header("Content-Transfer-Encoding", "binary")
 					.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resultFile.length()))	//파일 사이즈 설정
 					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())	//바이너리 데이터로 받아오기 설정
 					.body(resource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(null);
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		//utilFile.setEntity(file).fileDownload(request,response);
	}
	
	  
	
}