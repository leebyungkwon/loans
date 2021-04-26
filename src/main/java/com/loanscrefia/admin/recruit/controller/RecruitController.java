package com.loanscrefia.admin.recruit.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.service.RecruitService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.templete.service.TempleteService;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Controller
@RequestMapping(value="/admin/recruit")
public class RecruitController {
	
	@Autowired
	private RecruitService recruitService;
	
	@Autowired
	private TempleteService templeteService;
	
	
	// 모집인 조회 페이지
	@GetMapping(value="/recruitPage")
	public String recruitPage() {
		return CosntPage.BoRecruitPage+"/recruitList";
	}

	// 모집인 조회
	@PostMapping(value="/recruitList")
	public ResponseEntity<ResponseMsg> recruitList(RecruitDomain recruitDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(recruitService.selectRecruitList(recruitDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 모집인 등록 팝업
	@GetMapping(value="/p/recruitReg")
	public ModelAndView recruitReg() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoRecruitPage+"/p/recruitReg");
        return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 전체검증
	@PostMapping("/ocrTest")
	public ResponseEntity<ResponseMsg> ocrTest(@ModelAttribute RecruitDomain recruitDomain) throws IOException { 
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		Map<String, Object> ret = new HashMap<>();
		// 전체검증 시작
		// 1. 모집인 등록번호로 데이터 조회
		// 2. 첨부파일 데이터 조회
		// 3. FILE class로 파일 추출 후 검증시작
		// 4. 각각의 맞는 docType에 성공여부 return
		
		long sampleRecruitNo = 1;
		recruitDomain.setRecruitNo(sampleRecruitNo);
		
		RecruitDomain resultRecruit = recruitService.getRecruit(recruitDomain); 
		FileDomain fileDomain = new FileDomain();
		fileDomain.setFileId(resultRecruit.getAtchNo());
		// 첨부파일번호가 하나인 경우
		FileDomain resultFile = templeteService.getFile(fileDomain);
		// 첨부파일번호가 여러개인 경우
		List<FileDomain> resultFileList = templeteService.getFileList(fileDomain);

		for(FileDomain tmp : resultFileList) {
			Tesseract tesseract = new Tesseract();
	    	tesseract.setLanguage("kor");										
	        tesseract.setDatapath("C:\\tessdata");
	        
			try {
				File imageFile = new File(tmp.getFilePath(), tmp.getFileSaveNm() + "." + tmp.getFileExt());
				
	            String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
	            File outputfile = new File(tmp.getFilePath(), randomFileNm + ".png");
	            String ocrText = "";
	            // PDF인 경우 JPG 흑백으로 변경작업 필요(속도이슈)
	            if("pdf".equals(tmp.getFileExt())) {
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
	    			
	    			// 문서번호별로 추출해야하는 로직이 변경되야합니다.
	    			if("01".equals(tmp.getFileDocType())){
	    				
		                int startIndex = replaceText.indexOf("주민등록번호");
		                if(startIndex == -1) {
		                	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "추출된 주민등록번호 없습니다.");
		                	ret.put(tmp.getFileDocType(), false);
		                	break;
		                }
		                
		                
		                String ocrResult = replaceText.substring(startIndex+7, startIndex+21);
		                System.out.println("추출된 주민등록번호 == " + ocrResult);
		                
		                if(resultRecruit.getUserJumin() == ocrResult) {
		                	ret.put(tmp.getFileDocType(), true);
		                }else {
		                	ret.put(tmp.getFileDocType(), false);
		                }
		                
	    			}
	    			
	            }else {
	            	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "파일추출에 실패했습니다.");
	            }
			}catch(TesseractException e) {
	            e.printStackTrace();
	        }
		}
		
		// 검증한 결과값 전달
		responseMsg.setData(ret);
		

		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	
	
}
