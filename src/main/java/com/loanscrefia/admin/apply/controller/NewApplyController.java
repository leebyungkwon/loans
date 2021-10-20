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
import com.loanscrefia.admin.apply.domain.ApplyExcelDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.apply.repository.NewApplyRepository;
import com.loanscrefia.admin.apply.service.NewApplyService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.sms.domain.SmsDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilExcel;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import sinsiway.CryptoUtil;

@Controller
@RequestMapping(value="/admin")
public class NewApplyController {
	
	@Autowired 
	private NewApplyService applyService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired 
	private NewApplyRepository applyRepository;
	
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
	
	//SMS 적용여부
	@Value("${sms.apply}")
	public boolean smsApply;


	// 2021-10-13 모집인 등록 승인처리 페이지
	@GetMapping(value="/newApply/newApplyPage")
	public ModelAndView newApplyPage(String historyback) {
		ModelAndView mv =  new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyList");
		mv.addObject("historyback", historyback);
		return mv;
	}
	
	// 2021-10-13 모집인 등록 승인처리 리스트
	@PostMapping(value="/newApply/newApplyList")
	public ResponseEntity<ResponseMsg> newApplyList(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.selectNewApplyList(newApplyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-13 모집인 등록 승인처리 - 엑셀 다운로드
	@PostMapping("/newApply/newApplyListExcelDown")
	public void newApplyListExcelDown(NewApplyDomain newApplyDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		// 2021-07-27 페이징 false
		newApplyDomain.setIsPaging("false");
 		List<NewApplyDomain> excelDownList = applyService.selectNewApplyList(newApplyDomain);
 		new UtilExcel().downLoad(excelDownList, ApplyExcelDomain.class, response.getOutputStream());
	}
	
	
	
	// 2021-10-13 모집인 등록 승인처리 - 상세 페이지 : 개인
	@PostMapping(value="/newApply/newApplyIndvDetail")
    public ModelAndView newApplyIndvDetail(NewApplyDomain newApplyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyIndvDetail");
    	Map<String, Object> result 	= applyService.getNewApplyIndvDetail(newApplyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-13 모집인 등록 승인처리 - 상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/newApply/newApplyCorpDetail")
    public ModelAndView newRecruitCorpDetail(NewApplyDomain newApplyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyCorpDetail");
    	Map<String, Object> result 	= applyService.getNewApplyCorpDetail(newApplyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-13 모집인 등록 승인처리 - 상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/newApply/newApplyCorpImwonDetail")
    public ModelAndView newRecruitCorpImwonDetail(ApplyImwonDomain applyImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyCorpImwonDetail");
    	Map<String, Object> result 	= applyService.getNewApplyCorpImwonDetail(applyImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-13 모집인 등록 승인처리 - 상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/newApply/newApplyCorpExpertDetail")
    public ModelAndView newRecruitCorpExpertDetail(ApplyExpertDomain applyExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyCorpExpertDetail");
    	Map<String, Object> result 	= applyService.getNewApplyCorpExpertDetail(applyExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-13 모집인 등록 승인처리 - 상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/newApply/newApplyCorpItDetail")
    public ModelAndView newRecruitCorpItDetail(ApplyItDomain applyItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyCorpItDetail");
    	Map<String, Object> result 	= applyService.getNewApplyCorpItDetail(applyItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-13 모집인 등록 승인처리 - 상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/newApply/newApplyCorpEtcDetail")
    public ModelAndView newRecruitCorpEtcDetail(NewApplyDomain newApplyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewApplyPage+"/newApplyCorpEtcDetail");
    	Map<String, Object> result 	= applyService.getNewApplyCorpEtcDetail(newApplyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 2021-10-13 모집인 등록 승인처리 - 상태변경처리
	@PostMapping(value="/newApply/updateNewPlStat")
	public ResponseEntity<ResponseMsg> updateNewRecruitPlStat(NewApplyDomain newApplyDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.updateNewApplyPlStat(newApplyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 2021-10-13 모집인 등록 승인처리 - 첨부서류체크 등록
	@PostMapping(value="/newApply/insertNewApplyCheck")
	public ResponseEntity<ResponseMsg> insertNewApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.insertNewApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-13 모집인 등록 승인처리 - 첨부서류체크 삭제
	@PostMapping(value="/newApply/deleteNewApplyCheck")
	public ResponseEntity<ResponseMsg> deleteNewApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.deleteNewApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-13 모집인 등록 승인처리 - 실무자확인
	@PostMapping(value="/newApply/applyNewCheck")
	public ResponseEntity<ResponseMsg> applyNewCheck(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyNewCheck(newApplyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-13 모집인 등록 승인처리 - 관리자확인
	@PostMapping(value="/newApply/applyNewAdminCheck")
	public ResponseEntity<ResponseMsg> applyAdminNewCheck(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyNewAdminCheck(newApplyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 2021-10-13 모집인 등록 승인처리 - 승인일 홀딩
	@PostMapping(value="/newApply/newAppDateHold")
	public ResponseEntity<ResponseMsg> newAppDateHold(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.newAppDateHold(newApplyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 2021-10-13 모집인 등록 승인처리 - 개인 OCR
	@PostMapping("/newApply/newIndvOcr")
	public ResponseEntity<ResponseMsg> newIndvOcr(NewApplyDomain newApplyDomain) throws IOException { 
		//상세
		NewApplyDomain applyInfo = applyRepository.getNewApplyDetail(newApplyDomain);
		
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
	
	// 2021-10-13 모집인 등록 승인처리 - 법인 등록정보 OCR
	@PostMapping("/newApply/newCorpOcr")
	public ResponseEntity<ResponseMsg> newCorpOcr(NewApplyDomain newApplyDomain) throws IOException { 
		//상세
		NewApplyDomain applyInfo = applyRepository.getNewApplyDetail(newApplyDomain);
		
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
	
	
	
	// 2021-10-13 모집인 등록 승인처리 - 법인 임원정보 OCR
	@PostMapping("/newApply/newCorpImwonOcr")
	public ResponseEntity<ResponseMsg> newCorpImwonOcr(ApplyImwonDomain applyImwonDomain) throws IOException { 
		//상세
		ApplyImwonDomain applyInfo = applyRepository.getNewApplyImwonDetail(applyImwonDomain);
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
	
	// 2021-10-13 모집인 등록 승인처리 - 기등록여부체크리스트
	@GetMapping(value="/newApply/prevNewRegCheckPopup")
	public ModelAndView prevNewRegCheckPopup(NewApplyDomain newApplyDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/prevRegCheckPopup");
		List<NewApplyDomain> result = applyRepository.selectNewPrevRegCheckList(newApplyDomain);
		mav.addObject("result", result);
        return mav;
	}
	
	
	// 2021-10-13 모집인 등록 승인처리 - 상태변경처리
	@PostMapping(value="/newApply/checkboxNewUpdatePlStat")
	public ResponseEntity<ResponseMsg> checkboxNewUpdatePlStat(NewApplyDomain newApplyDomain) throws IOException{
		ResponseMsg responseMsg = applyService.checkboxNewUpdatePlStat(newApplyDomain);
		List<NewApplyDomain> applyResult = (List<NewApplyDomain>) responseMsg.getData();
		if(applyResult != null) { 
			ResponseMsg responseMsg2 = applyService.updateNewApplyListPlStat(applyResult);
			
			//List<EmailDomain> resultEmail = (List<EmailDomain>) responseMsg2.getData();
			
			// 2021-10-20 EMAIL 발송에서 SMS발송으로 전환
			List<SmsDomain> resultSms = (List<SmsDomain>) responseMsg2.getData();
			
			if(resultSms != null) {
				int smsResult = 0;
				if(resultSms != null) {
					if(smsApply) {
						smsResult = applyService.applyNewSendSms(resultSms);
						if(smsResult == -1) {
							responseMsg.setMessage("문자발송시 오류가 발생하였습니다.\n관리자에 문의해 주세요.");
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
	

	
	// 2021-10-13 모집인 등록 승인처리 - 선택 보완요청
	@PostMapping(value="/newApply/checkboxNewImproveUpdate")
	public ResponseEntity<ResponseMsg> checkboxNewImproveUpdate(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.checkboxNewImproveUpdate(newApplyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
}
