package com.loanscrefia.system.templete.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.board.domain.TempleteDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.templete.service.TempleteService;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;

import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Log4j2
@RestController
@RequestMapping(value="/system/templete")
public class TempleteController {
	
	@Autowired private TempleteService templeteService;

	@Autowired UtilFile utilFile;
	
	@GetMapping(value="/templetePage")
	public ModelAndView templetePage() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/templetePage");
        return mv;
	}
	
	@GetMapping(value="/templete2")
	public ModelAndView templete2() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/templete2");
        return mv;
	}
	
	@GetMapping(value="/templete")
	public ModelAndView templete() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/templete");
        return mv;
	}
	
	@PostMapping(value="/list")
	public ResponseEntity<ResponseMsg> list(HttpServletRequest request, TempleteDomain board){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
    	responseMsg.setData(templeteService.selectTemplete(board));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	@GetMapping(value="/templeteSave")
	public ModelAndView templeteSavePage(TempleteDomain board) {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/templeteSave");
    	String type = "NOTICE";
    	if(null != board.getBoardNo()) {
    		TempleteDomain b = templeteService.findById(board);
    		mv.addObject("board", b);
    		type = b.getBoardType();
    	}
		mv.addObject("type", type);
        return mv;
	}
	
	@GetMapping("/p/templeteSave")
    public ModelAndView templeteSavePopup() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/p/popTempleteSave");
        return mv;
    }

	@GetMapping("/p/templeteFileSave")
    public ModelAndView templeteFileSave() {
    	ModelAndView mv = new ModelAndView(CosntPage.BoTempletePage+"/p/popTempleteFileSave");
        return mv;
    }
	
	@PostMapping("/templeteSave")
	public ResponseEntity<ResponseMsg> templeteSave(@Valid TempleteDomain board) {
		ResponseMsg responseMsg = templeteService.templeteSave(board);
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}

	
	@PostMapping("/templeteFileSave")
	public ResponseEntity<ResponseMsg> templeteFileSave(TempleteDomain board, @RequestParam("files") MultipartFile[] files, HttpServletRequest request) 
			throws IllegalStateException, IOException {
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if(files.length > 0) {
			//List<FileEntity> f = uFile.uploadFiles(files, "test");
			FileDomain entity = new FileDomain();
			Map<String, Object> result = utilFile.setPath("test")
							.setFiles(files)
							.upload();
        }
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	@PostMapping("/excel")
	public ResponseEntity<ResponseMsg> readExcel(@RequestParam("files") MultipartFile[] files, TempleteDomain board) throws IOException { 

		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );

		Map<String, Object> ret = utilFile.setPath("test")
				.setFiles(files)
				.setExt("excel")
				.upload();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(); 
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			for(FileDomain exc : file) {
				String path = Paths.get(exc.getFilePath(), exc.getFileSaveNm()+"."+exc.getFileExt()).toString();
				//result = new UtilExcel().upload(path,TempleteDomain.class);
			}
			if(file.size() > 0) {
				// ????????????
				board.setAttchNo1(file.get(0).getFileSeq());
				responseMsg = templeteService.templeteSave(board);
			}
		}else {
			responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002", ret.get("message"));
		}
	
    	responseMsg.setData(result);
    	
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	@PostMapping("/img")
	public ResponseEntity<ResponseMsg> img(@RequestParam("files") MultipartFile[] files, TempleteDomain board) throws IOException { 

		ResponseMsg responseMsg = null;

		Map<String, Object> ret = utilFile.setPath("test")
							.setFiles(files)
							.setExt("image")
							.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				board.setAttchNo1(file.get(0).getFileSeq());
				//board.setAttchNo1(file.get(0).getFileGrpId());  --> ?????? ????????? ?????? file_grp_id ??????
				responseMsg = templeteService.templeteSave(board);
			}
		}else {
			responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002",ret.get("message").toString());
		}
		System.out.println(responseMsg);
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	@PostMapping("/zip")
	public ResponseEntity<ResponseMsg> zip(@RequestParam("files") MultipartFile[] files, TempleteDomain board) throws IOException { 

		ResponseMsg responseMsg = null;

		Map<String, Object> ret = utilFile.setPath("zip")
				.setFiles(files)
				.setExt("zip")
				.upload();
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				board.setAttchNo2(file.get(0).getFileSeq());
				responseMsg = templeteService.templeteSave(board);
			}
		}else {
			responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002",ret.get("message").toString());
		}
		System.out.println(responseMsg);
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	
	@PostMapping("/excelDown")
	public void writeExcel(TempleteDomain board, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
 		List<TempleteDomain> b = templeteService.selectTemplete(board);
 		new UtilExcel().downLoad(b, TempleteDomain.class, response.getOutputStream());
	}
	
	
	
	
	
	
	@PostMapping("/docCheck")
	public ResponseEntity<ResponseMsg> docCheck(@RequestParam("files") MultipartFile[] files, @RequestParam("docType") String docType) throws IOException { 
		Map<String, Object> ret = utilFile.setPath("ocrfile")
				.setFiles(files)
				.setExt("image")
				.upload();
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if((boolean) ret.get("success")) {
			FileDomain attach = (FileDomain) ret.get("ocrCheck");
	        Tesseract tesseract = new Tesseract();
	        
	        try {
	        	File imageFile = new File(attach.getFilePath(), attach.getFileSaveNm() + "." + attach.getFileExt());
	        	tesseract.setLanguage("kor");										
	            tesseract.setDatapath("C:\\tessdata");
	            // ???????????? ????????? ????????? ???????????????
	            String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
	            File outputfile = new File(attach.getFilePath(), randomFileNm + ".png");
	            String ocrText = "";
	            
	            // PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
	            if("pdf".equals(attach.getFileExt())) {
		            PDDocument pdf = PDDocument.load(imageFile);
		            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
		            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
		            ImageIO.write(imageObj, "png", outputfile);
		            
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
	            	if(!docType.isEmpty() && !ocrText.isEmpty()) {
	            		if("01".equals(docType)) {
	            			// ??????????????? ?????? ????????? ??????????????? ????????????.
	            			String jumin = "880131-115";
	            			String resultJumin = "";
	    	                String patternType = "\\d{6}\\-[1-4]\\d{2}";		                // ?????????????????? ?????????
	    	                Pattern pattern = Pattern.compile(patternType);
	    	                Matcher matcher = pattern.matcher(ocrText);
	    	                while(matcher.find()) {
	    	                	resultJumin =  matcher.group(0);
	    	                }
	    	                if(jumin.equals(resultJumin)) {
	    	                	// ????????? ?????????
	    	                	responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "01");
	    	                }else {
	    	                	responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "02");
	    	                }
	    	                System.out.println("????????? ???????????? == " + resultJumin);
	            		}else if("02".equals(docType)) {
	            			
	            			// ??????????????? ???????????? ??? ???????????? ??????
	            			String replaceText = ocrText.replace(" ", "");
	            			
	            			System.out.println("????????? ????????? ?????????.");
	            			System.out.println("############################");
	            			System.out.println("###############"+replaceText+"#############");
	            			System.out.println("############################");
	            			
	                        int startIndex = replaceText.indexOf("??????????????????");
	                        if(startIndex == -1) {
	                        	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "????????? ????????????????????? ????????????.");
	                        	return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	                        }
	                        
	                        String corpResult = replaceText.substring(startIndex+7, startIndex+21);
	                        System.out.println("????????? ????????????????????? == " + corpResult);
	                        responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "01");
	            		    
	            		}else {
	            			responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "????????????(docType) ????????? ?????????????????????.<br/> ???????????? ?????? ?????????.");
	            		}
	            	}else {
	            		responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "OCR????????? ??????????????????.");
	            	}
	            	
	            }else {
	            	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "??????????????? ??????????????????.");
	            }
	        }
	        catch (TesseractException e) {
	            e.printStackTrace();
	        }
		}
		
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	@PostMapping("/juminFiles")
	public ResponseEntity<ResponseMsg> juminCheck(@RequestParam("juminFiles") MultipartFile[] files) throws IOException { 
		
		Map<String, Object> ret = utilFile.setPath("ocrfile")
				.setFiles(files)
				.setExt("image")
				.upload();
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if((boolean) ret.get("success")) {
			FileDomain attach = (FileDomain) ret.get("ocrCheck");
	        Tesseract tesseract = new Tesseract();
	        
	        try {
	        	File imageFile = new File(attach.getFilePath(), attach.getFileSaveNm() + "." + attach.getFileExt());
	        	tesseract.setLanguage("kor");										
	            tesseract.setDatapath("C:\\tessdata");
	            // ???????????? ????????? ????????? ???????????????
	            String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
	            File outputfile = new File(attach.getFilePath(), randomFileNm + ".png");
	            String ocrText = "";
	            
	            // PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
	            if("pdf".equals(attach.getFileExt())) {
		            PDDocument pdf = PDDocument.load(imageFile);
		            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
		            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
		            ImageIO.write(imageObj, "png", outputfile);
		            
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
        			// ??????????????? ?????? ????????? ??????????????? ????????????.
        			String jumin = "880131-115";
        			String resultJumin = "";
	                String patternType = "\\d{6}\\-[1-4]\\d{2}";		                // ?????????????????? ?????????
	                Pattern pattern = Pattern.compile(patternType);
	                Matcher matcher = pattern.matcher(ocrText);
	                while(matcher.find()) {
	                	resultJumin =  matcher.group(0);
	                }
	                if(jumin.equals(resultJumin)) {
	                	// ????????? ?????????
	                	responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "01");
	                }else {
	                	responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "02");
	                }
	                responseMsg.setData(resultJumin);
	                System.out.println("????????? ???????????? == " + resultJumin);
	            }else {
	            	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "??????????????? ??????????????????.");
	            }
	        }
	        catch (TesseractException e) {
	            e.printStackTrace();
	        }
		}
		
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	
	
	@PostMapping("/verificationUpload")
	public ResponseEntity<ResponseMsg> verificationUpload(@RequestParam("brFiles") MultipartFile[] files) throws IOException { 
		
		Map<String, Object> ret = utilFile.setPath("ocrfile")
				.setFiles(files)
				.setExt("image")
				.upload();
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if((boolean) ret.get("success")) {
			FileDomain attach = (FileDomain) ret.get("ocrCheck");
			responseMsg.setData(attach);
		}
		
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	@PostMapping("/menualCheck")
	public ResponseEntity<ResponseMsg> menualCheck(@ModelAttribute FileDomain fileDomain) throws IOException { 
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if(fileDomain.getFileSeq() <= 0) {
			return new ResponseEntity<>(responseMsg, HttpStatus.OK);
		}
		
		FileDomain result = templeteService.getFile(fileDomain);
		Tesseract tesseract = new Tesseract();
        try {
        	File imageFile = new File(result.getFilePath(), result.getFileSaveNm() + "." + result.getFileExt());
        	tesseract.setLanguage("kor");										
            tesseract.setDatapath("C:\\tessdata");
            // ???????????? ????????? ????????? ???????????????
            String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            File outputfile = new File(result.getFilePath(), randomFileNm + ".png");
            String ocrText = "";
            
            // PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
            if("pdf".equals(result.getFileExt())) {
	            PDDocument pdf = PDDocument.load(imageFile);
	            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
	            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
	            ImageIO.write(imageObj, "png", outputfile);
	            
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
    			// ??????????????? ???????????? ??? ???????????? ??????
    			String replaceText = ocrText.replace(" ", "");
    			
    			System.out.println("????????? ????????? ?????????.");
    			System.out.println("############################");
    			System.out.println("###############"+replaceText+"#############");
    			System.out.println("############################");
    			
                int startIndex = replaceText.indexOf("??????????????????");
                if(startIndex == -1) {
                	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "????????? ????????????????????? ????????????.");
                	return new ResponseEntity<>(responseMsg, HttpStatus.OK);
                }
                
                String corpResult = replaceText.substring(startIndex+7, startIndex+21);
                System.out.println("????????? ????????????????????? == " + corpResult);
                responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "01");
                responseMsg.setData(corpResult);
                
            }else {
            	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "??????????????? ??????????????????.");
            }
        }
        catch (TesseractException e) {
            e.printStackTrace();
        }
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/cfFiles")
	public ResponseEntity<ResponseMsg> cfFilesCheck(@RequestParam("cfFiles") MultipartFile[] files) throws IOException { 
		
		Map<String, Object> ret = utilFile.setPath("ocrfile").setFiles(files).setExt("image").upload();
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if((boolean) ret.get("success")) {
			FileDomain attach = (FileDomain) ret.get("ocrCheck");
	        Tesseract tesseract = new Tesseract();
	        try {
	        	File imageFile = new File(attach.getFilePath(), attach.getFileSaveNm() + "." + attach.getFileExt());
	        	tesseract.setLanguage("kor");										
	            tesseract.setDatapath("C:\\tessdata");
	            // ???????????? ????????? ????????? ???????????????
	            String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
	            File outputfile = new File(attach.getFilePath(), randomFileNm + ".png");
	            String ocrText = "";
	            
	            // PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
	            if("pdf".equals(attach.getFileExt())) {
		            PDDocument pdf = PDDocument.load(imageFile);
		            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
		            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
		            ImageIO.write(imageObj, "png", outputfile);
		            
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
	            	String replaceText = ocrText.replace(" ", "");
	            	
	            	System.out.println("##??????##" + replaceText);
	            	
	            	String cfNo = "";
	            	String cfCurNm = "";
	            	
	            	// ????????? ???????????? ??????
	                String patternType = "\\d{10}";
	                Pattern pattern = Pattern.compile(patternType);
	                Matcher matcher = pattern.matcher(ocrText);
	                while(matcher.find()) {
	                	cfNo =  matcher.group(0);
	                }
	                
	                // ????????? ????????? ??????
	                int startIndex = replaceText.indexOf("?????????");
	                int endIndex = replaceText.indexOf("????????????");
	                if(startIndex == -1) {
	                	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "????????? ????????? ????????????.");
	                	return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	                }
	            	
	                cfCurNm = replaceText.substring(startIndex+4, endIndex);
	                
	                Map<String, Object> result = new HashMap<String, Object>();
	                result.put("cfNo", cfNo);
	                result.put("cfCurNm", cfCurNm);
	                
	            	responseMsg = new ResponseMsg(HttpStatus.OK, null, result, "01");
	            	
	            	
	            	
	            }else {
	            	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "??????????????? ??????????????????.");
	            }
	        }
	        catch (TesseractException e) {
	            e.printStackTrace();
	        }
		}
		
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	@PostMapping("/ocrTest")
	public ResponseEntity<ResponseMsg> ocrTest(@ModelAttribute FileDomain fileDomain) throws IOException { 
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		if(fileDomain.getFileSeq() <= 0) {
			return new ResponseEntity<>(responseMsg, HttpStatus.OK);
		}
		
		FileDomain result = templeteService.getFile(fileDomain);
		Tesseract tesseract = new Tesseract();
        try {
        	File imageFile = new File(result.getFilePath(), result.getFileSaveNm() + "." + result.getFileExt());
        	tesseract.setLanguage("kor");										
            tesseract.setDatapath("C:\\tessdata");
            // ???????????? ????????? ????????? ???????????????
            String randomFileNm = UUID.randomUUID().toString().replaceAll("-", "");
            File outputfile = new File(result.getFilePath(), randomFileNm + ".png");
            String ocrText = "";
            
            // PDF??? ?????? JPG ???????????? ???????????? ??????(????????????)
            if("pdf".equals(result.getFileExt())) {
	            PDDocument pdf = PDDocument.load(imageFile);
	            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
	            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.GRAY);
	            ImageIO.write(imageObj, "png", outputfile);
	            
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
    			// ??????????????? ???????????? ??? ???????????? ??????
    			String replaceText = ocrText.replace(" ", "");
    			
    			System.out.println("????????? ????????? ?????????.");
    			System.out.println("############################");
    			System.out.println("###############"+replaceText+"#############");
    			System.out.println("############################");
    			
                int startIndex = replaceText.indexOf("??????????????????");
                if(startIndex == -1) {
                	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "????????? ????????????????????? ????????????.");
                	return new ResponseEntity<>(responseMsg, HttpStatus.OK);
                }
                
                String corpResult = replaceText.substring(startIndex+7, startIndex+21);
                System.out.println("????????? ????????????????????? == " + corpResult);
                responseMsg = new ResponseMsg(HttpStatus.OK, null, null, "01");
                responseMsg.setData(corpResult);
                
            }else {
            	responseMsg = new ResponseMsg(HttpStatus.BAD_REQUEST, null, null, "??????????????? ??????????????????.");
            }
        }
        catch (TesseractException e) {
            e.printStackTrace();
        }
		return new ResponseEntity<>(responseMsg, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
}
