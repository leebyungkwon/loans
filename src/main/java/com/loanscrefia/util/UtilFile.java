package com.loanscrefia.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.repository.CommonRepository;

@Component
public class UtilFile {
	public UtilFile() {
	}

	@Autowired
	private CommonRepository commonRepository;
	@Autowired
	ResourceLoader resourceLoader;

	@Value("${upload.filePath}")
	public String filePath;

	private final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

	private FileDomain fileDomain;
	private MultipartFile[] files;

	private Boolean save = true;
	private String path;
	private String uploadPath;
	private String ext;
	private Boolean zip = false;

	public UtilFile setFiles(MultipartFile[] files) {
		this.files = files;
		return this;
	}

	public UtilFile setEntity(FileDomain entity) {
		this.fileDomain = entity;
		return this;
	}

	public UtilFile setPath(String path) {
		this.path = path;
		return this;
	}

	public UtilFile setExt(String ext) {
		this.ext = ext;
		return this;
	}

	public UtilFile setSave(Boolean save) {
		this.save = save;
		return this;
	}

	private final String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	//단일 첨부파일 업로드
	public Map<String, Object> upload() {
		Map<String, Object> result 	= new HashMap<String, Object>();
		String msg 					= "success";
		Boolean success 			= false;
		List<FileDomain> fileList 	= new ArrayList<>();

		if (this.files[0].getSize() < 1) {
			success = true;
			msg = "첨부파일이 잘못 되었습니다.[0001]";
			result.put("message", msg);
			result.put("success", success);
			result.put("data", Collections.emptyList());
			return result;
		}
		
		this.uploadPath = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\" + this.filePath,this.path/* , this.today */).toString();

		File dir = new File(this.uploadPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		int count 			= 0;
		int fileLen 		= files.length;
		Integer fileGrpSeq 	= null;
		
		if(fileLen > 1){
			if(this.fileDomain.getFileGrpSeq() == null) {
				fileGrpSeq = selectFileGrpSeq(this.fileDomain);
			}else {
				fileGrpSeq = this.fileDomain.getFileGrpSeq();
			}
		}

		for (MultipartFile file : files) {
			try {
				if(!file.isEmpty()) {
					String fileName 		= file.getOriginalFilename();
					final String extension 	= fileName.substring(fileName.lastIndexOf(".") + 1);

					if (!UtilString.isStr(this.ext.toLowerCase())) {
						if ("excel".equals(this.ext)) {
							for (Excel excel : Excel.values()) {
								if (excel.toString().equals(extension)) {
									success = true;
									break;
								}
							}
						}
						if ("image".equals(this.ext.toLowerCase())) {
							for (Image img : Image.values()) {
								if (img.toString().equals(extension)) {
									success = true;
									break;
								}
							}
						}
						if ("doc".equals(this.ext.toLowerCase())) {
							for (Doc doc : Doc.values()) {
								if (doc.toString().equals(extension)) {
									success = true;
									break;
								}
							}
						}
					}
					if (!success) {
						msg = "잘못된 확장자의 첨부파일을 등록 하였습니다.[0002]";
						result.put("message", msg);
						result.put("success", success);
						result.put("data", Collections.emptyList());
						return result;
					}
					
					final String saveName 	= getRandomString();
					final String orgName 	= fileName.replace("." + extension, "");

					File target = new File(uploadPath, saveName + "." + extension);
					file.transferTo(target);

					FileDomain attach = new FileDomain();

					attach.setFileGrpSeq(fileGrpSeq);
					attach.setFileExt(extension);
					attach.setFileOrgNm(orgName);
					attach.setFilePath(this.path);
					attach.setFileSaveNm(saveName);
					attach.setFileFullPath(this.uploadPath);
					//attach.setSize((int) target.length());
					if(fileLen > 1 && this.fileDomain.getFileTypeList() != null) {
						attach.setFileType(this.fileDomain.getFileTypeList().get(count));
					}

					if (this.save)
						this.save(attach);
					fileList.add(attach);

					if ("zip".equals(extension))
						this.zip = true;
					if (this.zip)
						fileList = this.unZip(attach, file, fileList);
					success = true;
					count++;
					// if(this.zip) target.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result.put("message", msg);
		result.put("success", success);
		result.put("data", fileList);
		return result;
	}
	
	//멀티 첨부파일 업로드 - 하나를 업로드해도 무조건 파일 그룹 시퀀스 따야함
	public Map<String, Object> multiUpload() {
		Map<String, Object> result 	= new HashMap<String, Object>();
		String msg 					= "success";
		Boolean success 			= false;
		List<FileDomain> fileList 	= new ArrayList<>();

		if (this.files[0].getSize() < 1) {
			success = true;
			msg = "첨부파일이 잘못 되었습니다.[0001]";
			result.put("message", msg);
			result.put("success", success);
			result.put("data", Collections.emptyList());
			return result;
		}
		
		this.uploadPath = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\" + this.filePath,this.path/* , this.today */).toString();

		File dir = new File(this.uploadPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		int count 			= 0;
		Integer fileGrpSeq 	= null;
		
		if(this.fileDomain.getFileGrpSeq() == null) {
			fileGrpSeq = selectFileGrpSeq(this.fileDomain);
		}else {
			fileGrpSeq = this.fileDomain.getFileGrpSeq();
		}

		for (MultipartFile file : files) {
			try {
				if(!file.isEmpty()) {
					String fileName 		= file.getOriginalFilename();
					final String extension 	= fileName.substring(fileName.lastIndexOf(".") + 1);

					if (!UtilString.isStr(this.ext.toLowerCase())) {
						if ("excel".equals(this.ext)) {
							for (Excel excel : Excel.values()) {
								if (excel.toString().equals(extension)) {
									success = true;
									break;
								}
							}
						}
						if ("image".equals(this.ext.toLowerCase())) {
							for (Image img : Image.values()) {
								if (img.toString().equals(extension)) {
									success = true;
									break;
								}
							}
						}
						if ("doc".equals(this.ext.toLowerCase())) {
							for (Doc doc : Doc.values()) {
								if (doc.toString().equals(extension)) {
									success = true;
									break;
								}
							}
						}
					}
					if (!success) {
						msg = "잘못된 확장자의 첨부파일을 등록 하였습니다.[0002]";
						result.put("message", msg);
						result.put("success", success);
						result.put("data", Collections.emptyList());
						return result;
					}
					
					final String saveName 	= getRandomString();
					final String orgName 	= fileName.replace("." + extension, "");

					File target = new File(uploadPath, saveName + "." + extension);
					file.transferTo(target);

					FileDomain attach = new FileDomain();

					attach.setFileGrpSeq(fileGrpSeq);
					attach.setFileExt(extension);
					attach.setFileOrgNm(orgName);
					attach.setFilePath(this.path);
					attach.setFileSaveNm(saveName);
					attach.setFileFullPath(this.uploadPath);
					//attach.setSize((int) target.length());
					if(this.fileDomain.getFileTypeList() != null) {
						attach.setFileType(this.fileDomain.getFileTypeList().get(count));
					}

					if (this.save)
						this.save(attach);
					fileList.add(attach);

					if ("zip".equals(extension))
						this.zip = true;
					if (this.zip)
						fileList = this.unZip(attach, file, fileList);
					success = true;
					count++;
					// if(this.zip) target.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result.put("message", msg);
		result.put("success", success);
		result.put("data", fileList);
		return result;
	}

	@Transactional
	private void save(FileDomain fileDomain) {
		commonRepository.insertFile(fileDomain);
	}

	@Transactional(readOnly=true)
	private Integer selectFileGrpSeq(FileDomain fileDomain) {
		return commonRepository.selectFileGrpSeq(fileDomain);
	}

	/*
	@Transactional
	private FileDomain select(FileDomain attach) {
		FileDomain fileDomain = (FileDomain) commonRepository.selectFile(attach);
		return fileDomain;
	}

	public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		FileDomain f = this.select(this.fileDomain);
		File file = new File(f.getFilePath(), f.getFileSaveNm() + "." + f.getFileExt());
		fileDomain = (FileDomain) commonRepository.selectFile(fileDomain);

		String name = fileDomain.getFileOrgNm() + "." + fileDomain.getFileExt();
		String filename = new String(name.getBytes("euc-kr"), "utf-8");

		String mimeType = URLConnection.guessContentTypeFromName(filename); // --- 파일의 mime타입을 확인합니다.
		if (mimeType == null) { // --- 마임타입이 없을 경우 application/octet-stream으로 설정합니다.
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType); // --- reponse에 mimetype을 설정합니다.
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\""); // ---
																								// Content-Disposition를
																								// attachment로 설정하여 다운로드
																								// 받을 파일임을 브라우저에게 알려줍니다.
		response.setContentLength((int) file.length()); // --- response content length를 설정합니다.
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file)); // --- inputstream 객체를 얻습니다.
		FileCopyUtils.copy(inputStream, response.getOutputStream()); // --- inputstream으로 파일을 읽고 outputsream으로 파일을 씁니다.

	}
	*/

	private List<FileDomain> unZip(FileDomain attach, MultipartFile file, List<FileDomain> fileList)
			throws IOException {
		String zipFile = Paths.get(attach.getFilePath(), attach.getFileSaveNm() + "." + attach.getFileExt()).toString();
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry ze = zis.getNextEntry();
		while (ze != null) {

			String fileName = ze.getName();
			final String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			final String saveName = getRandomString();
			final String orgName = fileName.replace("." + extension, "");
			File target = new File(this.uploadPath + File.separator + saveName + "." + extension);
			target.getParentFile().mkdirs();

			FileOutputStream fos = new FileOutputStream(target);
			int len;
			byte buffer[] = new byte[1024];
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();

			FileDomain unzipFile = new FileDomain();
			unzipFile.setFileOrgNm(orgName);
			unzipFile.setFileSaveNm(saveName);
			unzipFile.setFilePath(uploadPath);
			unzipFile.setFileExt(extension);
			if (this.save)
				this.save(unzipFile);
			fileList.add(unzipFile);

			ze = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();

		return fileList;
	}

	public enum All {

	}

	public enum Excel {
		xls, xlsx
	}

	public enum Image {
		png, jpg, jpeg, pdf
	}

	public enum Doc {
		pdf, hwp, xls, xlsx, txt
	}

	public enum Zip {
		zip
	}
}
