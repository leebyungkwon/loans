package com.loanscrefia.common.common.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.config.message.ResponseMsg;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class KfbApiService {
	
	public static String ApiDomain 	= "http://localhost:8080"; //API 서버 도메인
	public static String ClientId 	= "ClientId"; //요청자 아이디
	public static String ClientPw 	= "ClientPw"; //요청자 비밀번호
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 공통
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//토큰 발급
	public String getAuthToken() {
		
		String authToken 		= "";
		
		try {
			//URL 설정
			URL url 				= new URL(ApiDomain+"/oauth/2.0/token");
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
			conn.setRequestMethod("POST"); //POST, GET, PUT, DELETE 가능
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("X-Kfb-Client-Id", ClientId);
			conn.setRequestProperty("X-Kfb-User-Secret", ClientPw);
			conn.setDoOutput(true); //POST일때만
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        log.info("KfbApiService >> getAuthToken() > responseCode :: " + responseCode);
	        
	        if(responseCode == 200) {
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	String line 		= "";
	        	StringBuilder sb 	= new StringBuilder();
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            JSONObject responseJson = new JSONObject(sb.toString());
	            //authToken 				= responseJson.getString("authorization");
	            
	            log.info("KfbApiService >> getAuthToken() > responseJson :: " + responseJson);
	            log.info("KfbApiService >> getAuthToken() > authToken :: " + responseJson.getString("authorization"));
	        }
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return authToken;
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 개인
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//등록가능 여부 조회
	@Transactional
	public ResponseMsg checkLoan(String authToken, JsonObject reqParam) {
		
	    try {
	        //URL 설정
	        URL url 				= new URL(ApiDomain+"/loan/v1/check-loan-consultants");
	        HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
	        conn.setRequestMethod("GET"); //POST, GET, PUT, DELETE 가능
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", authToken);
	        //conn.setDoOutput(true); //POST일때만?
	        
	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.write(reqParam.toString());
	        bw.flush();
	        bw.close();
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        log.info("KfbApiService >> checkLoan() > responseCode :: " + responseCode);
	        
	        if(responseCode == 200) {
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	String line 		= "";
	        	StringBuilder sb 	= new StringBuilder();
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            JSONObject responseJson = new JSONObject(sb.toString());
	            
	            log.info("KfbApiService >> checkLoan() > responseJson :: " + responseJson);
	            log.info("KfbApiService >> checkLoan() > reg_yn :: " + responseJson.getString("reg_yn"));
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
	
	//가등록
	
	//가등록 취소
	
	//등록(본등록)
	
	//수정
	
	//삭제
	
	
	
	
	
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 법인
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 위반이력
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	
	
	
	
	
	
}
