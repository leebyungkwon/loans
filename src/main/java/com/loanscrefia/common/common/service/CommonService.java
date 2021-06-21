package com.loanscrefia.common.common.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.common.common.domain.BankApiDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.common.domain.VersionDomain;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.common.common.repository.VersionRepository;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Service
public class CommonService {

	@Autowired private VersionRepository verRepo;
	@Autowired private CommonRepository commonRepository;
	
	@Value("${upload.filePath}")
	public String uploadPath;
	
	@CacheEvict(value = "static", allEntries=true)
	public VersionDomain verSave(VersionDomain versionDomain) {
		return verRepo.save(versionDomain);
	}
	
	@Cacheable(value = "static")
	public Optional<VersionDomain> getVer(VersionDomain versionDomain) {
		return verRepo.findById(versionDomain.getVerId());
	}
	
	//첨부파일 리스트
	@Transactional(readOnly=true)
	public List<FileDomain> selectFileList(FileDomain fileDomain){
		return commonRepository.selectFileList(fileDomain);
	}
	
	//첨부파일 리스트(그룹 시퀀스 사용)
	@Transactional(readOnly=true)
	public List<FileDomain> selectFileListByGrpSeq(FileDomain fileDomain){
		return commonRepository.selectFileListByGrpSeq(fileDomain);
	}
	
	//첨부파일 단건 조회
	@Transactional(readOnly=true)
	public FileDomain getFile(FileDomain fileDomain) {
		return commonRepository.getFile(fileDomain);
	}
	
	//첨부파일 삭제
	@Transactional
	public int deleteFile(FileDomain fileDomain) {
		return commonRepository.deleteFile(fileDomain);
	}
	
	//첨부파일 real 삭제
	@Transactional
	public int realDeleteFile(FileDomain fileDomain) {
		
		FileDomain fileInfo = this.getFile(fileDomain);
		
		String oFile = this.uploadPath.toString() + "/" + fileInfo.getFilePath() + "/" + fileInfo.getFileSaveNm() + "." + fileInfo.getFileExt();
		String dFile = this.uploadPath.toString() + "/" + fileInfo.getFilePath() + "/" + fileInfo.getFileSaveNm() + "_dnc." + fileInfo.getFileExt();
		
		File delFile1 = new File(oFile);
		File delFile2 = new File(dFile);
		
		if(delFile1.exists()) {
			delFile1.delete();
		}
		if(delFile2.exists()) {
			delFile2.delete();
		}
		
		return commonRepository.realDeleteFile(fileDomain);
	}
	
	//첨부파일 real 삭제(그룹 시퀀스 사용)
	@Transactional
	public int realDeleteFileByGrpSeq(FileDomain fileDomain) {
		
		List<FileDomain> list = this.selectFileListByGrpSeq(fileDomain);
		
		if(list.size() > 0) {
			for(int i = 0;i < list.size();i++) {
				String oFile = this.uploadPath.toString() + "/" + list.get(i).getFilePath() + "/" + list.get(i).getFileSaveNm() + "." + list.get(i).getFileExt();
				String dFile = this.uploadPath.toString() + "/" + list.get(i).getFilePath() + "/" + list.get(i).getFileSaveNm() + "_dnc." + list.get(i).getFileExt();
				
				File delFile1 = new File(oFile);
				File delFile2 = new File(dFile);
				
				if(delFile1.exists()) {
					delFile1.delete();
				}
				if(delFile2.exists()) {
					delFile2.delete();
				}
			}
		}
		
		return commonRepository.realDeleteFileByGrpSeq(fileDomain);
	}

	//회원사 리스트
	@Transactional(readOnly=true)
	public List<CodeDtlDomain> selectCompanyCodeList(CodeDtlDomain codeDtlDomain){
		
		//세션 체크
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		if(loginInfo != null) {
			codeDtlDomain.setCreYn(loginInfo.getCreYn());
			codeDtlDomain.setCreGrp(loginInfo.getCreGrp());
		}
		
		List<CodeDtlDomain> comList = commonRepository.selectCompanyCodeList(codeDtlDomain);
		
		return comList;
	}
	
	//로그인 회원상세조회
	@Transactional(readOnly=true)
	public MemberDomain getMemberDetail(MemberDomain memberDomain) {
		return commonRepository.getMemberDetail(memberDomain);
	}
	
	//회원사 회원상세조회
	@Transactional(readOnly=true)
	public MemberDomain getCompanyMemberDetail(MemberDomain memberDomain) {
		return commonRepository.getCompanyMemberDetail(memberDomain);
	}
	
	//결제정보 조회
	@Transactional(readOnly=true)
	public PayResultDomain getPayResultDetail(PayResultDomain payResultDomain) {
		return commonRepository.getPayResultDetail(payResultDomain);
	}
	
	
	
	//은행연합회 - 개인 등록가능 여부 조회
	@Transactional
	public ResponseMsg checkLoan(BankApiDomain bankApiDomain){
		HttpURLConnection conn = null;
	    JSONObject responseJson = null;
	    
	    try {
	        //URL 설정
	        URL url = new URL("http://localhost:8080/test/api/action");
	        conn = (HttpURLConnection) url.openConnection();
	        
	        // type의 경우 POST, GET, PUT, DELETE 가능
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Transfer-Encoding", "chunked");
	        conn.setRequestProperty("Connection", "keep-alive");
	        conn.setDoOutput(true);
	        
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        // JSON 형식의 데이터 셋팅
	        JsonObject commands = new JsonObject();
	        JsonArray jsonArray = new JsonArray();
	        
	        commands.addProperty("key", 1);
	        commands.addProperty("age", 20);
	        commands.addProperty("userNm", "홍길동");

	        // 자기 자신을 넣는것 확인 
	        commands.add("userInfo", commands);
	         // JSON 형식의 데이터 셋팅 끝
	        
	        // 데이터를 STRING으로 변경
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        String jsonOutput = gson.toJson(commands);
	             
	        bw.write(commands.toString());
	        bw.flush();
	        bw.close();
	        
	        // 보내고 결과값 받기
	        int responseCode = conn.getResponseCode();
	        if (responseCode == 200) {
	        	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            responseJson = new JSONObject(sb.toString());
	            
	            // 응답 데이터
	            System.out.println("responseJson :: " + responseJson);
	        } 
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    
	    // return  영역
        Map<String, Object> msgMap = new HashMap<String, Object>();
		return new ResponseMsg(HttpStatus.OK, "fail", msgMap, "실패");
	}
	
	
}