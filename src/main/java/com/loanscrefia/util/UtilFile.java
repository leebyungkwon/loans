package com.loanscrefia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.repository.CommonRepository;



@Component
public class UtilFile {
	public UtilFile() {
	}

	@Autowired
	private CommonRepository commonRepository;

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

	public Map<String, Object> upload() {
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "success";
		Boolean success = false;
		List<FileDomain> fileList = new ArrayList<>();

		if (this.files[0].getSize() < 1) {
			success = true;
			msg = "첨부파일이 잘못 되었습니다.[0001]";
			result.put("message", msg);
			result.put("success", success);
			result.put("data", Collections.emptyList());
			return result;
		}

		this.uploadPath = Paths.get(this.filePath, this.path, this.today).toString();

		File dir = new File(this.uploadPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		for (MultipartFile file : files) {
			try {
				String fileName = file.getOriginalFilename();
				final String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

				if (!UtilString.isStr(this.ext)) {
					if ("excel".equals(this.ext)) {
						for (Excel excel : Excel.values()) {
							if (excel.toString().equals(extension)) {
								success = true;
								break;
							}
						}
					}
					if ("image".equals(this.ext)) {
						for (Image img : Image.values()) {
							if (img.toString().equals(extension)) {
								success = true;
								break;
							}
						}
					}

					if ("zip".equals(this.ext)) {
						for (Zip zip : Zip.values()) {
							if (zip.toString().equals(extension)) {
								success = true;
								break;
							}
						}
					}

				}

				
				 if(!success) { 
					 msg = "잘못된 확장자의 첨부파일을 등록 하였습니다.[0002]"; 
					 result.put("message",msg); 
					 result.put("success", success);
					 result.put("data",Collections.emptyList()); return result; 
				}
				
				final String saveName = getRandomString();
				final String orgName = fileName.replace("." + extension, "");

				File target = new File(uploadPath, saveName + "." + extension);
				file.transferTo(target);

				/* ���� ���� ���� */
				FileDomain attach = new FileDomain();
				attach.setFileOrgNm(orgName);
				attach.setFileSaveNm(saveName);
				attach.setFilePath(uploadPath);
				attach.setFileExt(extension);
				if (this.save)
					this.save(attach);
				fileList.add(attach);

				if ("zip".equals(extension))
					this.zip = true;
				if (this.zip)
					fileList = this.unZip(attach, file, fileList);
				success = true;
				// if(this.zip) target.delete();

				result.put("ocrCheck", attach);

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
	private void save(FileDomain attach) {
		commonRepository.insertFile(attach);
	}

	private List<FileDomain> unZip(FileDomain attach, MultipartFile file, List<FileDomain> fileList)
			throws IOException {
		String zipFile = Paths.get(attach.getFilePath(), attach.getFileSaveNm() + "." + attach.getFileExt()).toString();
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile), Charset.forName("EUC-KR"));
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

	public enum Excel {
		xls, xlsx
	}

	public enum Image {
		png, jpg, jpeg, pdf
	}

	public enum Doc {
		pdf, hwp
	}

	public enum Zip {
		zip
	}
}
