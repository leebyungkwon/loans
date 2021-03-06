package com.loanscrefia.admin.apply.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.apply.domain.ApplyExcelDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.repository.ApplyRepository;
import com.loanscrefia.admin.apply.service.ApplyService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilExcel;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import sinsiway.CryptoUtil;

@Controller
@RequestMapping(value="/admin")
public class ApplyController {
	
	@Autowired 
	private ApplyService applyService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired 
	private ApplyRepository applyRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	@Value("${upload.filePath}")
	public String filePath;
	
	//????????? ????????????
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//????????? ????????????
	@Value("${email.apply}")
	public boolean emailApply;

	/* -------------------------------------------------------------------------------------------------------
	 * ?????? ????????? > ????????? ?????? ??? ??????
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//????????? ?????????
	@GetMapping(value="/apply/applyPage")
	public ModelAndView applyPage(String historyback) {
		ModelAndView mv =  new ModelAndView(CosntPage.BoApplyPage+"/applyList");
		mv.addObject("historyback", historyback);
		return mv;
	}
	
	//?????????
	@PostMapping(value="/apply/applyList")
	public ResponseEntity<ResponseMsg> selectApplyList(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.selectApplyList(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//?????? ????????????
	@PostMapping("/apply/applyListExcelDown")
	public void applyListExcelDown(ApplyDomain applyDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		// 2021-07-27 ????????? false
		applyDomain.setIsPaging("false");
 		List<ApplyDomain> excelDownList = applyService.selectApplyList(applyDomain);
 		new UtilExcel().downLoad(excelDownList, ApplyExcelDomain.class, response.getOutputStream());
	}
	
	//?????? ????????? : ??????
	@PostMapping(value="/apply/applyIndvDetail")
    public ModelAndView applyIndvDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyIndvDetail");
    	Map<String, Object> result 	= applyService.getApplyIndvDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//?????? ????????? : ?????? > ???????????? ???
	@PostMapping(value="/apply/applyCorpDetail")
    public ModelAndView recruitCorpDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//?????? ????????? : ?????? > ????????? ??? ?????????????????? ???
	@PostMapping(value="/apply/applyCorpImwonDetail")
    public ModelAndView recruitCorpImwonDetail(ApplyImwonDomain applyImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpImwonDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpImwonDetail(applyImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//?????? ????????? : ?????? > ???????????? ???
	@PostMapping(value="/apply/applyCorpExpertDetail")
    public ModelAndView recruitCorpExpertDetail(ApplyExpertDomain applyExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpExpertDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpExpertDetail(applyExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//?????? ????????? : ?????? > ???????????? ???
	@PostMapping(value="/apply/applyCorpItDetail")
    public ModelAndView recruitCorpItDetail(ApplyItDomain applyItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpItDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpItDetail(applyItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//?????? ????????? : ?????? > ?????? ???
	@PostMapping(value="/apply/applyCorpEtcDetail")
    public ModelAndView recruitCorpEtcDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpEtcDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpEtcDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// ??????????????????
	@PostMapping(value="/apply/updatePlStat")
	public ResponseEntity<ResponseMsg> updateRecruitPlStat(ApplyDomain applyDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.updateApplyPlStat(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// ?????????????????? ??????
	@PostMapping(value="/apply/insertApplyCheck")
	public ResponseEntity<ResponseMsg> insertApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.insertApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// ?????????????????? ??????
	@PostMapping(value="/apply/deleteApplyCheck")
	public ResponseEntity<ResponseMsg> deleteApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.deleteApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// ???????????????
	@PostMapping(value="/apply/applyCheck")
	public ResponseEntity<ResponseMsg> applyCheck(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyCheck(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// ???????????????
	@PostMapping(value="/apply/applyAdminCheck")
	public ResponseEntity<ResponseMsg> applyAdminCheck(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyAdminCheck(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// ????????? ??????
	@PostMapping(value="/apply/appDateHold")
	public ResponseEntity<ResponseMsg> appDateHold(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.appDateHold(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// ?????? OCR
	@PostMapping("/apply/indvOcr")
	public ResponseEntity<ResponseMsg> indvOcr(ApplyDomain applyDomain) throws IOException { 
		//??????
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileGrpSeq(applyInfo.getFileSeq());
		List<FileDomain> files = commonService.selectFileList(fileDomain);
		Tesseract tesseract = new Tesseract();
    	tesseract.setLanguage("kor");										
        tesseract.setDatapath("C:\\tessdata");
        ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
        	if(files.size() > 0) {
        		for(FileDomain file : files) {
        			if("2".equals(file.getFileType()) || "3".equals(file.getFileType()) || "4".equals(file.getFileType())) {
        				String realfilePath = this.filePath.toString() + "/userReg";
        				
        				// ????????? ??????
        				File imageFile = null;
        				
        				if(cryptoApply) {
        					String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
            				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
            				CryptoUtil.decryptFile(oFile, chFile);
            				imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				}else {
        					imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				}
        				
            			// ???????????? ????????? ????????? ???????????????
            			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            			File outputfile = new File(realfilePath, randomFileNm + ".png");
            			String ocrText = "";
            			
            			// PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
            			if("pdf".equals(file.getFileExt())) {
            				PDDocument pdf = PDDocument.load(imageFile);
            				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
            				ImageIO.write(imageObj, "png", outputfile);
            				pdf.close();
            				
            			}else {
            				// pdf??? ???????????? image??? ???????????? ??????
            				BufferedImage buImage = ImageIO.read(imageFile);
            				for(int y = 0; y < buImage.getHeight(); y++) {
            					for(int x = 0; x < buImage.getWidth(); x++) {
            						Color colour = new Color(buImage.getRGB(x, y));
            						int Y = (int) (0.2126 * colour.getRed() + 0.7152 * colour.getGreen() + 0.0722 * colour.getBlue());
            						buImage.setRGB(x, y, new Color(Y, Y, Y).getRGB());
            					}
            				}
            				ImageIO.write(buImage, "png", outputfile);
            			}
            			
            			if(outputfile.exists()) {
            				ocrText = tesseract.doOCR(outputfile);
            				String replaceText = "";
            				// ??????????????? ???????????? ??? ???????????? ??????
            				if(ocrText != null) {
            					replaceText = ocrText.replace(" ", "");
            				}else {
            					msgMap.put("fileType"+file.getFileType(), "??????????????? ??????");
            			    	responseMsg.setData(msgMap);
            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
            				}
            				
            				// ?????? ???????????? ???????????? ??????
            				if("2".equals(file.getFileType())) {
    	            			// ??????????????? ?????? ????????? ??????????????? ????????????.
    	            			String jumin = applyInfo.getPlMZId();
    	            			String resultJumin = "";
    	    	                String patternType = "\\d{6}\\-[1-4]\\d{6}";
    	    	                Pattern pattern = Pattern.compile(patternType);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultJumin =  matcher.group(0);
    	    	                }
            					if(jumin.equals(resultJumin)) {
            						msgMap.put("fileType"+file.getFileType(), "??????");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "?????????");
            					}
            					
            				}else if("3".equals(file.getFileType())){
            					// ??????
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	            			int st = replaceText.indexOf("(????????????:");
    	            			if(st > 0) {
        	    	                String patternType = "\\S{13}\\-[0-9]\\S{4}";
        	    	                Pattern pattern = Pattern.compile(patternType);
        	    	                Matcher matcher = pattern.matcher(replaceText);
        	    	                while(matcher.find()) {
        	    	                	resultEduNo =  matcher.group(0);
        	    	                }
                					if(eduNo.equals(resultEduNo)) {
                						msgMap.put("fileType"+file.getFileType(), "??????");
                					}else {
                						msgMap.put("fileType"+file.getFileType(), "?????????");
                					}
    	            			}
    	            			
    	            			String patternType = "\\d{10}";
    	    	                Pattern pattern = Pattern.compile(patternType);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultEduNo =  matcher.group(0);
    	    	                }
    	    	                if(StringUtils.isNotEmpty(resultEduNo)) {
    	    	                	if(eduNo.equals(resultEduNo)) {
    	    	                		msgMap.put("fileType"+file.getFileType(), "??????");
    	    	                	}else {
    	    	                		msgMap.put("fileType"+file.getFileType(), "?????????");
    	    	                	}
    	    	                }else {
                					int start = replaceText.indexOf("????????????:");
                					if(start > 0) {
                    					resultEduNo = replaceText.substring(start+5, start+23);
                    					if(eduNo.equals(resultEduNo)) {
                    						msgMap.put("fileType"+file.getFileType(), "??????");
                    					}else {
                    						msgMap.put("fileType"+file.getFileType(), "?????????");
                    					}
                					}
    	    	                }
            					
            				}else if("4".equals(file.getFileType())){ 
            					// ??????????????? ????????? ??????
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	    	                String eduPattern = "\\d{10}";
    	    	                Pattern pattern = Pattern.compile(eduPattern);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultEduNo =  matcher.group(0);
    	    	                }
            					if(eduNo.equals(resultEduNo)) {
            						msgMap.put("fileType"+file.getFileType(), "??????");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "?????????");
            					}
            				}else {
            					// fileType??? ??????X
            					msgMap.put("fileType"+file.getFileType(), "fileType??????");
            				}
            			}else {
            				msgMap.put("fileType"+file.getFileType(), "??????????????? ??????");
            			}
        			}
        		}// for??? ??????
        	}else {
        		msgMap.put("error", "????????? ??????????????? ????????????.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
    	responseMsg.setData(msgMap);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// ?????? ???????????? OCR
	@PostMapping("/apply/corpOcr")
	public ResponseEntity<ResponseMsg> corpOcr(ApplyDomain applyDomain) throws IOException { 
		//??????
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileGrpSeq(applyInfo.getFileSeq());
		List<FileDomain> files = commonService.selectFileList(fileDomain);
		Tesseract tesseract = new Tesseract();
    	tesseract.setLanguage("kor");										
        tesseract.setDatapath("C:\\tessdata");
        ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
        	if(files.size() > 0) {
        		for(FileDomain file : files) {
        			if("2".equals(file.getFileType())) {
        				String realfilePath = this.filePath.toString() + "/userReg";
        				
        				// ????????? ??????
        				File imageFile = null;
        				
        				if(cryptoApply) {
        					String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
            				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
            				CryptoUtil.decryptFile(oFile, chFile);
            				imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				}else {
        					imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				}
        				
            			// ???????????? ????????? ????????? ???????????????
            			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            			File outputfile = new File(realfilePath, randomFileNm + ".png");
            			String ocrText = "";
            			
            			// PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
            			if("pdf".equals(file.getFileExt())) {
            				PDDocument pdf = PDDocument.load(imageFile);
            				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
            				ImageIO.write(imageObj, "png", outputfile);
            				pdf.close();
            				
            			}else {
            				// pdf??? ???????????? image??? ???????????? ??????
            				BufferedImage buImage = ImageIO.read(imageFile);
            				for(int y = 0; y < buImage.getHeight(); y++) {
            					for(int x = 0; x < buImage.getWidth(); x++) {
            						Color colour = new Color(buImage.getRGB(x, y));
            						int Y = (int) (0.2126 * colour.getRed() + 0.7152 * colour.getGreen() + 0.0722 * colour.getBlue());
            						buImage.setRGB(x, y, new Color(Y, Y, Y).getRGB());
            					}
            				}
            				ImageIO.write(buImage, "png", outputfile);
            			}
            			
            			if(outputfile.exists()) {
            				ocrText = tesseract.doOCR(outputfile);
            				String replaceText = "";
            				// ??????????????? ???????????? ??? ???????????? ??????
            				if(ocrText != null) {
            					replaceText = ocrText.replace(" ", "");
            				}else {
            					msgMap.put("fileType"+file.getFileType(), "??????????????? ??????");
            			    	responseMsg.setData(msgMap);
            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
            				}
            				
            				// ?????? ???????????? ???????????? ??????
            				if("2".equals(file.getFileType())) {
    	            			// ?????????????????????
    	            			String corpNo = applyInfo.getPlMerchantNo();
    	            			//String corpNo = applyInfo.getPlMerchantNo();
    	            			String resultCorpNo = "";
    	    	                String patternType = "\\d{6}\\-[0-9]\\d{6}";
    	    	                Pattern pattern = Pattern.compile(patternType);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultCorpNo =  matcher.group(0);
    	    	                }
            					if(corpNo.equals(resultCorpNo)) {
            						msgMap.put("fileType"+file.getFileType()+"_12", "??????");
            					}else {
            						msgMap.put("fileType"+file.getFileType()+"_12", "?????????");
            					}
            					
            					// ???????????????
            					String foundDate = applyInfo.getOcrCorpFoundDate();
            					String ocrFoundDate = "";
	            				int start = replaceText.indexOf("?????????????????????");
	            				if(start <= 0) {
	            					msgMap.put("fileType"+file.getFileType()+"_13", "Read??????");
	            			    	responseMsg.setData(msgMap);
	            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	            				}
	            				ocrFoundDate = replaceText.substring(start+7, start+28);
            					if(foundDate.equals(ocrFoundDate)) {
            						msgMap.put("fileType"+file.getFileType()+"_13", "??????");
            					}else {
            						msgMap.put("fileType"+file.getFileType()+"_13", "?????????");
            					}
            					
            				}else {
            					// fileType??? ??????X
            					msgMap.put("fileType"+file.getFileType(), "fileType??????");
            				}
            			}else {
            				msgMap.put("fileType"+file.getFileType(), "??????????????? ??????");
            			}
        			}
        		}// for??? ??????
        	}else {
        		msgMap.put("error", "????????? ??????????????? ????????????.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
    	responseMsg.setData(msgMap);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	// ?????? ???????????? OCR
	@PostMapping("/apply/corpImwonOcr")
	public ResponseEntity<ResponseMsg> corpImwonOcr(ApplyImwonDomain applyImwonDomain) throws IOException { 
		//??????
		ApplyImwonDomain applyInfo = applyRepository.getApplyImwonDetail(applyImwonDomain);
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileGrpSeq(applyInfo.getFileSeq());
		List<FileDomain> files = commonService.selectFileList(fileDomain);
		Tesseract tesseract = new Tesseract();
    	tesseract.setLanguage("kor");										
        tesseract.setDatapath("C:\\tessdata");
        ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
        	if(files.size() > 0) {
        		for(FileDomain file : files) {
        			if("12".equals(file.getFileType()) || "13".equals(file.getFileType())) {
        				String realfilePath = this.filePath.toString() + "/userReg";
        				
        				// ????????? ??????
        				File imageFile = null;
        				
        				if(cryptoApply) {
        					String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
            				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
            				CryptoUtil.decryptFile(oFile, chFile);
            				imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				}else {
        					imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				}
        				
            			// ???????????? ????????? ????????? ???????????????
            			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            			File outputfile = new File(realfilePath, randomFileNm + ".png");
            			String ocrText = "";
            			
            			// PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
            			if("pdf".equals(file.getFileExt())) {
            				PDDocument pdf = PDDocument.load(imageFile);
            				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
            				ImageIO.write(imageObj, "png", outputfile);
            				pdf.close();
            				
            			}else {
            				// pdf??? ???????????? image??? ???????????? ??????
            				BufferedImage buImage = ImageIO.read(imageFile);
            				for(int y = 0; y < buImage.getHeight(); y++) {
            					for(int x = 0; x < buImage.getWidth(); x++) {
            						Color colour = new Color(buImage.getRGB(x, y));
            						int Y = (int) (0.2126 * colour.getRed() + 0.7152 * colour.getGreen() + 0.0722 * colour.getBlue());
            						buImage.setRGB(x, y, new Color(Y, Y, Y).getRGB());
            					}
            				}
            				ImageIO.write(buImage, "png", outputfile);
            			}
            			
            			if(outputfile.exists()) {
            				ocrText = tesseract.doOCR(outputfile);
            				String replaceText = "";
            				// ??????????????? ???????????? ??? ???????????? ??????
            				if(ocrText != null) {
            					replaceText = ocrText.replace(" ", "");
            				}else {
            					msgMap.put("fileType"+file.getFileType(), "??????????????? ??????");
            			    	responseMsg.setData(msgMap);
            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
            				}
            				
            				// ?????? ???????????? ???????????? ??????

            				if("12".equals(file.getFileType())){ 
            					// ??????
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	            			int st = replaceText.indexOf("(????????????:");
    	            			if(st > 0) {
        	    	                String patternType = "\\S{13}\\-[0-9]\\S{4}";
        	    	                Pattern pattern = Pattern.compile(patternType);
        	    	                Matcher matcher = pattern.matcher(replaceText);
        	    	                while(matcher.find()) {
        	    	                	resultEduNo =  matcher.group(0);
        	    	                }
                					if(eduNo.equals(resultEduNo)) {
                						msgMap.put("fileType"+file.getFileType(), "??????");
                					}else {
                						msgMap.put("fileType"+file.getFileType(), "?????????");
                					}
    	            			}
    	            			
    	            			String patternType = "\\d{10}";
    	    	                Pattern pattern = Pattern.compile(patternType);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultEduNo =  matcher.group(0);
    	    	                }
    	    	                if(StringUtils.isNotEmpty(resultEduNo)) {
    	    	                	if(eduNo.equals(resultEduNo)) {
    	    	                		msgMap.put("fileType"+file.getFileType(), "??????");
    	    	                	}else {
    	    	                		msgMap.put("fileType"+file.getFileType(), "?????????");
    	    	                	}
    	    	                }else {
                					int start = replaceText.indexOf("????????????:");
                					if(start > 0) {
                    					resultEduNo = replaceText.substring(start+5, start+23);
                    					if(eduNo.equals(resultEduNo)) {
                    						msgMap.put("fileType"+file.getFileType(), "??????");
                    					}else {
                    						msgMap.put("fileType"+file.getFileType(), "?????????");
                    					}
                					}
    	    	                }
            				}else if("13".equals(file.getFileType())){ 
            					// ??????????????? ????????? ??????
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	    	                String eduPattern = "\\d{10}";
    	    	                Pattern pattern = Pattern.compile(eduPattern);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultEduNo =  matcher.group(0);
    	    	                }
            					if(eduNo.equals(resultEduNo)) {
            						msgMap.put("fileType"+file.getFileType(), "??????");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "?????????");
            					}
            				}else {
            					// fileType??? ??????X
            					msgMap.put("fileType"+file.getFileType(), "fileType??????");
            				}
            			}else {
            				msgMap.put("fileType"+file.getFileType(), "??????????????? ??????");
            			}
        			}
        		}// for??? ??????
        	}else {
        		msgMap.put("error", "????????? ??????????????? ????????????.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
    	responseMsg.setData(msgMap);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//??????????????????????????????
	@GetMapping(value="/apply/prevRegCheckPopup")
	public ModelAndView prevRegCheck(ApplyDomain applyDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/prevRegCheckPopup");
		List<ApplyDomain> result = applyRepository.selectPrevRegCheckList(applyDomain);
		mav.addObject("result", result);
        return mav;
	}
	
	
	// ??????????????????
	@PostMapping(value="/apply/checkboxUpdatePlStat")
	public ResponseEntity<ResponseMsg> checkboxUpdatePlStat(ApplyDomain applyDomain) throws IOException{
		ResponseMsg responseMsg = applyService.checkboxUpdatePlStat(applyDomain);
		List<ApplyDomain> applyResult = (List<ApplyDomain>) responseMsg.getData();
		if(applyResult != null) { 
			ResponseMsg responseMsg2 = applyService.updateApplyListPlStat(applyResult);
			System.out.println("##### checkboxUpdatePlStat ");
			System.out.println(responseMsg2);
			List<EmailDomain> resultEmail = (List<EmailDomain>) responseMsg2.getData();
			if(resultEmail != null) {
				int emailResult = 0;
				if(resultEmail != null) {
					if(emailApply) {
						emailResult = applyService.applySendEmail(resultEmail);
						if(emailResult == -1) {
							responseMsg.setMessage("??????????????? ????????? ?????????????????????.\n???????????? ????????? ?????????.");
						}else {
							responseMsg.setMessage("?????????????????????.");
						}
					}else {
						responseMsg.setMessage("?????????????????????.");
					}
				}
			}else {
				return new ResponseEntity<ResponseMsg>(responseMsg2 ,HttpStatus.OK);
			}
		}else {
			return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
		}
		
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	

	
	//?????? ????????????
	@PostMapping(value="/apply/checkboxImproveUpdate")
	public ResponseEntity<ResponseMsg> checkboxImproveUpdate(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.checkboxImproveUpdate(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
}
