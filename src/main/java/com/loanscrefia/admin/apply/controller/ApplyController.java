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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
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
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//이메일 적용여부
	@Value("${email.apply}")
	public boolean emailApply;

	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/apply/applyPage")
	public ModelAndView applyPage(String historyback) {
		ModelAndView mv =  new ModelAndView(CosntPage.BoApplyPage+"/applyList");
		mv.addObject("historyback", historyback);
		return mv;
	}
	
	//리스트
	@PostMapping(value="/apply/applyList")
	public ResponseEntity<ResponseMsg> selectApplyList(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.selectApplyList(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 다운로드
	@PostMapping("/apply/applyListExcelDown")
	public void applyListExcelDown(ApplyDomain applyDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		// 2021-07-27 페이징 false
		applyDomain.setIsPaging("false");
 		List<ApplyDomain> excelDownList = applyService.selectApplyList(applyDomain);
 		new UtilExcel().downLoad(excelDownList, ApplyDomain.class, response.getOutputStream());
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/apply/applyIndvDetail")
    public ModelAndView applyIndvDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyIndvDetail");
    	Map<String, Object> result 	= applyService.getApplyIndvDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/apply/applyCorpDetail")
    public ModelAndView recruitCorpDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/apply/applyCorpImwonDetail")
    public ModelAndView recruitCorpImwonDetail(ApplyImwonDomain applyImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpImwonDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpImwonDetail(applyImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/apply/applyCorpExpertDetail")
    public ModelAndView recruitCorpExpertDetail(ApplyExpertDomain applyExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpExpertDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpExpertDetail(applyExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/apply/applyCorpItDetail")
    public ModelAndView recruitCorpItDetail(ApplyItDomain applyItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpItDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpItDetail(applyItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/apply/applyCorpEtcDetail")
    public ModelAndView recruitCorpEtcDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpEtcDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpEtcDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 상태변경처리
	@PostMapping(value="/apply/updatePlStat")
	public ResponseEntity<ResponseMsg> updateRecruitPlStat(ApplyDomain applyDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.updateApplyPlStat(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 첨부서류체크 등록
	@PostMapping(value="/apply/insertApplyCheck")
	public ResponseEntity<ResponseMsg> insertApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.insertApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 첨부서류체크 삭제
	@PostMapping(value="/apply/deleteApplyCheck")
	public ResponseEntity<ResponseMsg> deleteApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.deleteApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 실무자확인
	@PostMapping(value="/apply/applyCheck")
	public ResponseEntity<ResponseMsg> applyCheck(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyCheck(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 관리자확인
	@PostMapping(value="/apply/applyAdminCheck")
	public ResponseEntity<ResponseMsg> applyAdminCheck(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyAdminCheck(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 승인일 홀딩
	@PostMapping(value="/apply/appDateHold")
	public ResponseEntity<ResponseMsg> appDateHold(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.appDateHold(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 개인 OCR
	@PostMapping("/apply/indvOcr")
	public ResponseEntity<ResponseMsg> indvOcr(ApplyDomain applyDomain) throws IOException { 
		//상세
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
		
		// 주민번호 암호화 해제		
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
        				
        				// 암호화 해제
        				File imageFile = null;
        				
        				if(cryptoApply) {
        					String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
            				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
            				CryptoUtil.decryptFile(oFile, chFile);
            				imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				}else {
        					imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				}
        				
            			// 흑색변환 처리시 필요한 랜덤파일명
            			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            			File outputfile = new File(realfilePath, randomFileNm + ".png");
            			String ocrText = "";
            			
            			// PDF인 경우 JPG 흑백으로 변경작업 필요(속도이슈)
            			if("pdf".equals(file.getFileExt())) {
            				PDDocument pdf = PDDocument.load(imageFile);
            				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
            				ImageIO.write(imageObj, "png", outputfile);
            				pdf.close();
            				
            			}else {
            				// pdf가 아닌경우 image는 흑백으로 변경
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
            				// 문자추출시 띄어쓰기 및 공백제거 실행
            				if(ocrText != null) {
            					replaceText = ocrText.replace(" ", "");
            				}else {
            					msgMap.put("fileType"+file.getFileType(), "파일추출에 실패");
            			    	responseMsg.setData(msgMap);
            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
            				}
            				
            				// 파일 타입별로 추출영역 생성
            				if("2".equals(file.getFileType())) {
    	            			// 등록하고자 하는 회원의 주민번호를 비교한다.
    	            			String jumin = applyInfo.getPlMZId();
    	            			String resultJumin = "";
    	    	                String patternType = "\\d{6}\\-[1-4]\\d{6}";
    	    	                Pattern pattern = Pattern.compile(patternType);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultJumin =  matcher.group(0);
    	    	                }
            					if(jumin.equals(resultJumin)) {
            						msgMap.put("fileType"+file.getFileType(), "일치");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "불일치");
            					}
            					
            				}else if("3".equals(file.getFileType())){
            					// 경력
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	            			int st = replaceText.indexOf("(수료번호:");
    	            			if(st > 0) {
        	    	                String patternType = "\\S{13}\\-[0-9]\\S{4}";
        	    	                Pattern pattern = Pattern.compile(patternType);
        	    	                Matcher matcher = pattern.matcher(replaceText);
        	    	                while(matcher.find()) {
        	    	                	resultEduNo =  matcher.group(0);
        	    	                }
                					if(eduNo.equals(resultEduNo)) {
                						msgMap.put("fileType"+file.getFileType(), "일치");
                					}else {
                						msgMap.put("fileType"+file.getFileType(), "불일치");
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
    	    	                		msgMap.put("fileType"+file.getFileType(), "일치");
    	    	                	}else {
    	    	                		msgMap.put("fileType"+file.getFileType(), "불일치");
    	    	                	}
    	    	                }else {
                					int start = replaceText.indexOf("수료번호:");
                					if(start > 0) {
                    					resultEduNo = replaceText.substring(start+5, start+23);
                    					if(eduNo.equals(resultEduNo)) {
                    						msgMap.put("fileType"+file.getFileType(), "일치");
                    					}else {
                    						msgMap.put("fileType"+file.getFileType(), "불일치");
                    					}
                					}
    	    	                }
            					
            				}else if("4".equals(file.getFileType())){ 
            					// 신규일경우 인증서 추출
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	    	                String eduPattern = "\\d{10}";
    	    	                Pattern pattern = Pattern.compile(eduPattern);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultEduNo =  matcher.group(0);
    	    	                }
            					if(eduNo.equals(resultEduNo)) {
            						msgMap.put("fileType"+file.getFileType(), "일치");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "불일치");
            					}
            				}else {
            					// fileType에 포함X
            					msgMap.put("fileType"+file.getFileType(), "fileType오류");
            				}
            			}else {
            				msgMap.put("fileType"+file.getFileType(), "파일추출에 실패");
            			}
        			}
        		}// for문 종료
        	}else {
        		msgMap.put("error", "조회된 첨부파일이 없습니다.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
    	responseMsg.setData(msgMap);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 법인 등록정보 OCR
	@PostMapping("/apply/corpOcr")
	public ResponseEntity<ResponseMsg> corpOcr(ApplyDomain applyDomain) throws IOException { 
		//상세
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
		
		// 법인번호 암호화 해제		
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
        				
        				// 암호화 해제
        				File imageFile = null;
        				
        				if(cryptoApply) {
        					String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
            				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
            				CryptoUtil.decryptFile(oFile, chFile);
            				imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				}else {
        					imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				}
        				
            			// 흑색변환 처리시 필요한 랜덤파일명
            			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            			File outputfile = new File(realfilePath, randomFileNm + ".png");
            			String ocrText = "";
            			
            			// PDF인 경우 JPG 흑백으로 변경작업 필요(속도이슈)
            			if("pdf".equals(file.getFileExt())) {
            				PDDocument pdf = PDDocument.load(imageFile);
            				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
            				ImageIO.write(imageObj, "png", outputfile);
            				pdf.close();
            				
            			}else {
            				// pdf가 아닌경우 image는 흑백으로 변경
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
            				// 문자추출시 띄어쓰기 및 공백제거 실행
            				if(ocrText != null) {
            					replaceText = ocrText.replace(" ", "");
            				}else {
            					msgMap.put("fileType"+file.getFileType(), "파일추출에 실패");
            			    	responseMsg.setData(msgMap);
            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
            				}
            				
            				// 파일 타입별로 추출영역 생성
            				if("2".equals(file.getFileType())) {
    	            			// 법인등기부등본
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
            						msgMap.put("fileType"+file.getFileType()+"_12", "일치");
            					}else {
            						msgMap.put("fileType"+file.getFileType()+"_12", "불일치");
            					}
            					
            					// 설립년월일
            					String foundDate = applyInfo.getOcrCorpFoundDate();
            					String ocrFoundDate = "";
	            				int start = replaceText.indexOf("회사성립연월일");
	            				if(start <= 0) {
	            					msgMap.put("fileType"+file.getFileType()+"_13", "Read실패");
	            			    	responseMsg.setData(msgMap);
	            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	            				}
	            				ocrFoundDate = replaceText.substring(start+7, start+28);
            					if(foundDate.equals(ocrFoundDate)) {
            						msgMap.put("fileType"+file.getFileType()+"_13", "일치");
            					}else {
            						msgMap.put("fileType"+file.getFileType()+"_13", "불일치");
            					}
            					
            				}else {
            					// fileType에 포함X
            					msgMap.put("fileType"+file.getFileType(), "fileType오류");
            				}
            			}else {
            				msgMap.put("fileType"+file.getFileType(), "파일추출에 실패");
            			}
        			}
        		}// for문 종료
        	}else {
        		msgMap.put("error", "조회된 첨부파일이 없습니다.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
    	responseMsg.setData(msgMap);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	// 법인 임원정보 OCR
	@PostMapping("/apply/corpImwonOcr")
	public ResponseEntity<ResponseMsg> corpImwonOcr(ApplyImwonDomain applyImwonDomain) throws IOException { 
		//상세
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
        				
        				// 암호화 해제
        				File imageFile = null;
        				
        				if(cryptoApply) {
        					String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
            				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
            				CryptoUtil.decryptFile(oFile, chFile);
            				imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				}else {
        					imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				}
        				
            			// 흑색변환 처리시 필요한 랜덤파일명
            			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            			File outputfile = new File(realfilePath, randomFileNm + ".png");
            			String ocrText = "";
            			
            			// PDF인 경우 JPG 흑백으로 변경작업 필요(속도이슈)
            			if("pdf".equals(file.getFileExt())) {
            				PDDocument pdf = PDDocument.load(imageFile);
            				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
            				ImageIO.write(imageObj, "png", outputfile);
            				pdf.close();
            				
            			}else {
            				// pdf가 아닌경우 image는 흑백으로 변경
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
            				// 문자추출시 띄어쓰기 및 공백제거 실행
            				if(ocrText != null) {
            					replaceText = ocrText.replace(" ", "");
            				}else {
            					msgMap.put("fileType"+file.getFileType(), "파일추출에 실패");
            			    	responseMsg.setData(msgMap);
            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
            				}
            				
            				// 파일 타입별로 추출영역 생성

            				if("12".equals(file.getFileType())){ 
            					// 경력
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	            			int st = replaceText.indexOf("(수료번호:");
    	            			if(st > 0) {
        	    	                String patternType = "\\S{13}\\-[0-9]\\S{4}";
        	    	                Pattern pattern = Pattern.compile(patternType);
        	    	                Matcher matcher = pattern.matcher(replaceText);
        	    	                while(matcher.find()) {
        	    	                	resultEduNo =  matcher.group(0);
        	    	                }
                					if(eduNo.equals(resultEduNo)) {
                						msgMap.put("fileType"+file.getFileType(), "일치");
                					}else {
                						msgMap.put("fileType"+file.getFileType(), "불일치");
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
    	    	                		msgMap.put("fileType"+file.getFileType(), "일치");
    	    	                	}else {
    	    	                		msgMap.put("fileType"+file.getFileType(), "불일치");
    	    	                	}
    	    	                }else {
                					int start = replaceText.indexOf("수료번호:");
                					if(start > 0) {
                    					resultEduNo = replaceText.substring(start+5, start+23);
                    					if(eduNo.equals(resultEduNo)) {
                    						msgMap.put("fileType"+file.getFileType(), "일치");
                    					}else {
                    						msgMap.put("fileType"+file.getFileType(), "불일치");
                    					}
                					}
    	    	                }
            				}else if("13".equals(file.getFileType())){ 
            					// 신규일경우 인증서 추출
            					String eduNo = applyInfo.getPlEduNo();
    	            			String resultEduNo = "";
    	    	                String eduPattern = "\\d{10}";
    	    	                Pattern pattern = Pattern.compile(eduPattern);
    	    	                Matcher matcher = pattern.matcher(replaceText);
    	    	                while(matcher.find()) {
    	    	                	resultEduNo =  matcher.group(0);
    	    	                }
            					if(eduNo.equals(resultEduNo)) {
            						msgMap.put("fileType"+file.getFileType(), "일치");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "불일치");
            					}
            				}else {
            					// fileType에 포함X
            					msgMap.put("fileType"+file.getFileType(), "fileType오류");
            				}
            			}else {
            				msgMap.put("fileType"+file.getFileType(), "파일추출에 실패");
            			}
        			}
        		}// for문 종료
        	}else {
        		msgMap.put("error", "조회된 첨부파일이 없습니다.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
    	responseMsg.setData(msgMap);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//기등록여부체크리스트
	@GetMapping(value="/apply/prevRegCheckPopup")
	public ModelAndView prevRegCheck(ApplyDomain applyDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/prevRegCheckPopup");
		List<ApplyDomain> result = applyRepository.selectPrevRegCheckList(applyDomain);
		mav.addObject("result", result);
        return mav;
	}
	
	
	// 상태변경처리
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
							responseMsg.setMessage("메일발송시 오류가 발생하였습니다.\n관리자에 문의해 주세요.");
						}else {
							responseMsg.setMessage("완료되었습니다.");
						}
					}else {
						responseMsg.setMessage("완료되었습니다.");
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
}
