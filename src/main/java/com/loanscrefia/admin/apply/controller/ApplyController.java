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
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.repository.ApplyRepository;
import com.loanscrefia.admin.apply.service.ApplyService;
import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.service.UserService;
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
	
	@Value("${upload.filePath}")
	public String filePath;

	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/apply/applyPage")
	public String applyPage() {
		return CosntPage.BoApplyPage+"/applyList";
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
	public ResponseEntity<ResponseMsg> updateRecruitPlStat(ApplyDomain applyDomain){
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
	
	
	// 개인 OCR
	@PostMapping("/apply/indvOcr")
	public ResponseEntity<ResponseMsg> indvOcr(ApplyDomain applyDomain) throws IOException { 
		//상세
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
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
        				
        				/*
        				// 암호화 해제
        				String oFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "." + file.getFileExt();
        				String chFile = this.filePath.toString()+ "/" +file.getFilePath()+"/"+file.getFileSaveNm() + "_dnc." + file.getFileExt();
        				CryptoUtil.decryptFile(oFile, chFile);
        				File imageFile = new File(realfilePath, file.getFileSaveNm() + "_dnc." + file.getFileExt());
        				*/
        				
        				File imageFile = new File(realfilePath, file.getFileSaveNm() + "." + file.getFileExt());
        				
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
            					// 경력이고 협회인증서인 경우
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
    	            			}else {
    	            				int start = replaceText.indexOf("수료번호:");
    	            				if(start <= 0) {
    	            					msgMap.put("fileType"+file.getFileType(), "오류");
    	            			    	responseMsg.setData(msgMap);
    	            					return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
    	            				}
    	            				
                					resultEduNo = replaceText.substring(start+5, start+23);
                					if(eduNo.equals(resultEduNo)) {
                						msgMap.put("fileType"+file.getFileType(), "일치");
                					}else {
                						msgMap.put("fileType"+file.getFileType(), "불일치");
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
            				}else if("7".equals(file.getFileType())){ 
            					// 결격사유
            				    int lineCnt = 0;
            				    int fromIndex = -1;
            				    while ((fromIndex = replaceText.indexOf("불충족", fromIndex + 1)) >= 0) {
            				      lineCnt++;
            				    }

            					if(lineCnt < 2) {
            						msgMap.put("fileType"+file.getFileType(), "결격사유 충족");
            					}else {
            						msgMap.put("fileType"+file.getFileType(), "결격사유 불충족");
            					}
            					
            				}else if("13".equals(file.getFileType())){ 
            					// 후견부존재증명서
            				    int lineCnt = 0;
            				    int fromIndex = -1;
            				    
            				    
            				    while ((fromIndex = replaceText.indexOf("불충족", fromIndex + 1)) >= 0) {
            				      lineCnt++;
            				    }

            					if(lineCnt < 2) {
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
	
	// 법인 ocr
	@PostMapping("/apply/corpOcr")
	public Map<String, Object> corpOcr(ApplyDomain applyDomain) throws IOException { 
		//상세
		ApplyDomain applyInfo = applyRepository.getApplyDetail(applyDomain);
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileGrpSeq(applyInfo.getFileSeq());
		List<FileDomain> files = commonService.selectFileList(fileDomain);
		Tesseract tesseract = new Tesseract();
    	tesseract.setLanguage("kor");										
        tesseract.setDatapath("C:\\tessdata");
        Map<String, Object> msgMap = new HashMap<String, Object>();
        try {
        	if(files.size() > 0) {
        		for(FileDomain file : files) {
        			File imageFile = new File(file.getFilePath(), file.getFileSaveNm() + "." + file.getFileExt());
        			// 흑색변환 처리시 필요한 랜덤파일명
        			String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        			File outputfile = new File(file.getFilePath(), randomFileNm + ".png");
        			String ocrText = "";
        			
        			// PDF인 경우 JPG 흑백으로 변경작업 필요(속도이슈)
        			if("pdf".equals(file.getFileExt())) {
        				PDDocument pdf = PDDocument.load(imageFile);
        				PDFRenderer pdfRenderer = new PDFRenderer(pdf);
        				BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
        				ImageIO.write(imageObj, "png", outputfile);
        				
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
        				// 문자추출시 띄어쓰기 및 공백제거 실행
        				String replaceText = ocrText.replace(" ", "");
        				
        				// 파일 타입별로 추출영역 생성
        				if("2".equals(file.getFileType())) {
	            			// 등록하고자 하는 회원의 주민번호를 비교한다.
	            			String jumin = applyInfo.getPlMZId();
	            			String resultJumin = "";
	    	                String patternType = "\\d{6}\\-[1-4]\\d{2}";
	    	                Pattern pattern = Pattern.compile(patternType);
	    	                Matcher matcher = pattern.matcher(ocrText);
	    	                while(matcher.find()) {
	    	                	resultJumin =  matcher.group(0);
	    	                }
        					if(jumin.equals(resultJumin)) {
        						msgMap.put("fileType"+file.getFileType(), "일치");
        					}else {
        						msgMap.put("fileType"+file.getFileType(), "불일치");
        					}
        					
        				}else if("3".equals(file.getFileType())){
        					int startIndex = replaceText.indexOf("주민등록번호");
        					String zIdResult = replaceText.substring(startIndex+7, startIndex+21);
        					if(applyInfo.getPlMZId().equals(zIdResult)) {
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
        			
        		}// for문 종료
        		
        	}else {
        		msgMap.put("error", "조회된 첨부파일이 없습니다.");
        	}
        }catch (TesseractException e) {
            e.printStackTrace();
        }
		return msgMap;
	}
}
