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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.config.message.ResponseMsg;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class KfbApiService {
	
	@Autowired private KfbApiRepository kfbApiRepo;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 필요정보
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//API 서버 도메인							- 개발 : "172.16.123.10:43003"
	public static String ApiDomain 			= "http://localhost:8080";
	
	//개인
	public static String CheckLoanUrl 		= ApiDomain+"/loan/v1/check-loan-consultants"; 		//GET(등록가능 여부 조회)
	public static String PreLoanUrl			= ApiDomain+"/loan/v1/pre-loan-consultants"; 		//POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public static String LoanUrl 			= ApiDomain+"/loan/v1/loan-consultants"; 			//POST(본등록 처리),GET(조회),PUT(수정),DELETE(삭제)
	
	//법인
	public static String CheckLoanCorpUrl 	= ApiDomain+"/loan/v1/check-loan-corp-consultants"; //GET(등록가능 여부 조회)
	public static String PreLoanCorpUrl		= ApiDomain+"/loan/v1/pre-loan-corp-consultants"; 	//POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public static String LoanCorpUrl 		= ApiDomain+"/loan/v1/loan-corp-consultants"; 		//POST(본등록 처리),GET(조회),PUT(수정),DELETE(삭제)
	
	//위반이력
	public static String ViolationUrl		= ApiDomain+"/loan/v1/violation-consultants"; 		//POST(등록),GET(조회),PUT(수정),DELETE(삭제)
	
	//토큰 발급
	public static String TokenUrl			= ApiDomain+"/oauth/2.0/token";		//POST
	public static String ClientId 			= "ClientId"; 						//요청자 아이디
	public static String ClientPw 			= "ClientPw"; 						//요청자 비밀번호
	
	//금융기관 조회
	public static String FinUrl				= ApiDomain+"/loan/v1/fin-info";	//GET
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 공통
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//토큰 발급
	public String getAuthToken() {
		
		String authToken 		= "";
		
		try {
			//URL 설정
			URL url 				= new URL(TokenUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
			conn.setRequestMethod("POST");
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
	
	//요청 이력 등록
	@Transactional
	public void insertKfbApiReqLog(KfbApiDomain kfbApiDomain) {
		kfbApiRepo.insertKfbApiReqLog(kfbApiDomain);
	}
	
	//응답 이력 등록
	@Transactional
	public void insertKfbApiResLog(KfbApiDomain kfbApiDomain) {
		kfbApiRepo.insertKfbApiResLog(kfbApiDomain);
	}
	
	//은행연합회 토큰 조회
	@Transactional(readOnly=true)
	public String selectKfbApiKey() {
		return kfbApiRepo.selectKfbApiKey();
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 개인
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//등록가능 여부 조회
	public ResponseMsg checkLoan(String authToken, JsonObject reqParam) {
		
	    try {
	        //URL 설정
	        URL url 				= new URL(CheckLoanUrl);
	        HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", authToken);
	        //conn.setDoOutput(true); //POST일때만?
	        
	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.write(reqParam.toString());
	        bw.flush();
	        bw.close();
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(CheckLoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
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
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            
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
	public boolean indvTempRegCancel(String authToken, JsonObject reqParam) {
		
		boolean tempRegCheck = false;
	    try {
	        //URL 설정
	        URL url 				= new URL(PreLoanUrl);
	        HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
	        conn.setRequestMethod("DELETE");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", authToken);
	        
	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.write(reqParam.toString());
	        bw.flush();
	        bw.close();
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(PreLoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
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
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            
	            log.info("KfbApiService >> checkLoan() > responseJson :: " + responseJson);
	            log.info("KfbApiService >> checkLoan() > reg_yn :: " + responseJson.getString("reg_yn"));
	            
	            tempRegCheck = true;
	        }
	        
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    
	    return tempRegCheck;
	}
	
	
	
	//등록(본등록)
	public boolean indvReg(String authToken, JsonObject reqParam) {
		boolean indvReg = false;
	    try {
	        //URL 설정
	        URL url 				= new URL(PreLoanUrl);
	        HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
	        conn.setRequestMethod("DELETE");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", authToken);
	        
	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.write(reqParam.toString());
	        bw.flush();
	        bw.close();
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(PreLoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
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
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            
	            log.info("KfbApiService >> checkLoan() > responseJson :: " + responseJson);
	            log.info("KfbApiService >> checkLoan() > reg_yn :: " + responseJson.getString("reg_yn"));
	            indvReg = true;
	        }
	        
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    
	    return indvReg;
	}
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
	
	
	
	
	
	
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 샘플
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	public ResponseMsg commonKfbApi(String authToken, JsonObject reqParam, String apiNm, String methodType) {
		
        Map<String, Object> msgMap = new HashMap<String, Object>();
        String successCheck = "fail";
	    try {
	        //URL 설정
	        URL url 				= new URL(apiNm);
	        HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
	        conn.setRequestMethod(methodType);
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", authToken);
	        
	        /*
	        if(methodType.equals("POST")) {
	        	conn.setDoOutput(true); //POST일때만?	        	
	        }
	        */

	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.write(reqParam.toString());
	        bw.flush();
	        bw.close();
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(apiNm);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        log.info("KfbApiService >> checkLoan() > responseCode :: " + responseCode);
	        
	        
	        // responseCode값 확인 - JSONObject로 변환한 데이터를 넘겨서 각 service에서 처리 (ApplyService - updateApplyPlStat 샘플)
	        if(responseCode == 200) {
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	String line 		= "";
	        	StringBuilder sb 	= new StringBuilder();
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            JSONObject responseJson = new JSONObject(sb.toString());
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            log.info("KfbApiService >> checkLoan() > responseJson :: " + responseJson);
	            
	            successCheck = "success";
	            msgMap.put("resultData", responseJson);
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return new ResponseMsg(HttpStatus.OK, successCheck, msgMap);
	}
}
