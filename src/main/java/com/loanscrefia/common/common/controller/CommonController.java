package com.loanscrefia.common.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
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
import sinsiway.CryptoUtil;

@Slf4j
@RestController
public class CommonController {
	
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired ResourceLoader resourceLoader;
	
	@Value("${download.filePath}")
	public String filePath;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
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
	
	// 첨부파일 다운로드
	@GetMapping("/common/fileDown")
	public void testFileDown(@RequestParam int fileSeq, HttpServletRequest request, @RequestHeader("User-Agent") String userAgent, HttpServletResponse response) throws IOException {
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileSeq(fileSeq);
		fileDomain.setUseYn("down");
		FileDomain f 	= commonService.getFile(fileDomain);
		
		File file 		= null;
		
		if(cryptoApply) {
			// 암호화 해제
			String oFile = filePath+ "/" +f.getFilePath()+"/"+f.getFileSaveNm() + "." + f.getFileExt();
			String chFile = filePath+ "/" +f.getFilePath()+"/"+f.getFileSaveNm() + "_dnc." + f.getFileExt();
			CryptoUtil.decryptFile(oFile, chFile);
			file = new File(filePath+ "/" +f.getFilePath(), f.getFileSaveNm() + "_dnc." + f.getFileExt());
			// 해제 끝
		}else {
			file = new File(filePath+ "/" +f.getFilePath(), f.getFileSaveNm() + "." + f.getFileExt());
		}
		
		if(file != null) {
			String name = f.getFileOrgNm() + "." + f.getFileExt();
	        String header = request.getHeader("User-Agent");
	        String filename = "";
	        if (header.contains("MSIE") || header.contains("Trident")) {
	        	filename = URLEncoder.encode(name,"UTF-8").replaceAll("\\+", "%20");
	            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ";");
	        } else {
	        	filename = new String(name.getBytes("UTF-8"), "ISO-8859-1");
	           response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
	        }
	        /*
			String mimeType = URLConnection.guessContentTypeFromName(filename); // --- 파일의 mime타입을 확인합니다.
			if (mimeType == null) { // --- 마임타입이 없을 경우 application/octet-stream으로 설정합니다.
				mimeType = "application/octet-stream";
			}
			*/
			
			//response.setContentType(mimeType);
	        response.setContentType( "application/download; UTF-8");
	        response.setHeader("Content-Type", "application/octet-stream");
	        response.setHeader("Content-Transfer-Encoding", "binary");
			response.setContentLength((int) file.length());
			response.setHeader("Paragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			FileInputStream fis = new FileInputStream(file);
			//InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(fis, response.getOutputStream());
		}
	}
	
	//첨부파일 삭제
	@PostMapping(value="/common/fileDelete")
	public ResponseEntity<ResponseMsg> fileDelete(FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
    	responseMsg.setData(commonService.deleteFile(fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//첨부파일 real 삭제
	@PostMapping(value="/common/fileRealDelete")
	public ResponseEntity<ResponseMsg> fileRealDelete(FileDomain fileDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		responseMsg.setData(commonService.realDeleteFile(fileDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
}