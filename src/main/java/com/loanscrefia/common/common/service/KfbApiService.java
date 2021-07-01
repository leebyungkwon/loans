package com.loanscrefia.common.common.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	
	//API 서버 도메인							- 개발 : "172.16.123.10:43003" // "http://localhost:8080";
	public static String ApiDomain 			= "http://172.16.123.10:43003";
	public static String ClientId 			= "crefia"; 					//요청자 아이디
	public static String ClientSecret		= "781r671t905ehq46"; 			//요청자 비밀번호
	
	//Authorize Code 발급
	public static String AuthCodeUrl		= ApiDomain+"/oauth/2.0/authorize";	//POST
	
	//개인
	public static String CheckLoanUrl 		= ApiDomain+"/loan/v1/check-loan-consultants"; 		//GET(등록가능 여부 조회)
	public static String PreLoanUrl			= ApiDomain+"/loan/v1/pre-loan-consultants"; 		//POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public static String LoanUrl 			= ApiDomain+"/loan/v1/loan-consultants"; 			//POST(본등록 처리),GET(조회),PUT(수정),DELETE(등록 말소)
	
	//법인
	public static String CheckLoanCorpUrl 	= ApiDomain+"/loan/v1/check-loan-corp-consultants"; //GET(등록가능 여부 조회)
	public static String PreLoanCorpUrl		= ApiDomain+"/loan/v1/pre-loan-corp-consultants"; 	//POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public static String LoanCorpUrl 		= ApiDomain+"/loan/v1/loan-corp-consultants"; 		//POST(본등록 처리),GET(조회),PUT(수정),DELETE(등록 말소)
	
	//위반이력
	public static String ViolationUrl		= ApiDomain+"/loan/v1/violation-consultants"; 		//POST(등록),GET(조회),PUT(수정),DELETE(삭제)
	
	//네트워크 및 서버상태확인
	public static String HealthCheckUrl		= ApiDomain+"/loan/v1/health-check";//POST
	
	//토큰 발급
	public static String TokenUrl			= ApiDomain+"/oauth/2.0/token";		//POST
	
	//금융기관 조회
	public static String FinUrl				= ApiDomain+"/loan/v1/fin-info";	//GET
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 공통
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//네트워크 및 서버상태확인
	public String getHealthCheck() {
		
		String result = "fail";
		
		try {
			//URL 설정
			URL url 				= new URL(CheckLoanUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.connect();
			
			//요청 결과
			int responseCode = conn.getResponseCode();
			log.info("KfbApiService >> getAuthCode() > responseCode :: " + responseCode);
			
			if(responseCode == 200) {
				result = "success";
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
	}
	
	//Authorize Code 발급
	public String getAuthCode() {
		
		String authCode 		= "";
		
		try {
			
			/*
			//URL 설정
			URL url 				= new URL(AuthCodeUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("X-Kfb-Client-Id", ClientId);
			conn.setRequestProperty("X-Kfb-User-Secret", ClientSecret);
			conn.setDoOutput(true); //POST일때만

	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.flush();
	        bw.close();
	        
			*/
			
			Map<String, Object> jsonParamMap = new HashMap<String, Object>();

			// Connect
			URL url = new URL(PreLoanUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			 
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("X-Kfb-Client-Id", ClientId);
			conn.setRequestProperty("X-Kfb-User-Secret", ClientSecret);
			conn.setRequestMethod("POST");
			
			System.out.println("###########################");
			System.out.println("########11111111##################");
			System.out.println("############"+conn+"###############");
			System.out.println("###########################");
			System.out.println("############"+conn.getRequestMethod()+"###############");
			
			
			
			conn.connect();
			
			
			// write
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			OutputStream os = conn.getOutputStream();
			String stringParam = new GsonBuilder().create().toJson( jsonParamMap, Map.class );
			bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			bw.write(stringParam);
			bw.close();
			os.close();
			
			
			
			//요청 결과
			int responseCode = conn.getResponseCode();
			
			System.out.println("#############################");
			System.out.println("#############################");
			System.out.println("#######"+responseCode+"#######################");
			System.out.println("#############################");
			System.out.println("#############################");
			
			
			log.info("KfbApiService >> getAuthCode() > responseCode :: " + responseCode);
			
			if(responseCode == 200) {
				BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line 		= "";
				StringBuilder sb 	= new StringBuilder();
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				JSONObject responseJson = new JSONObject(sb.toString());
				//authCode 				= responseJson.getString("authorize_code");
				
				log.info("KfbApiService >> getAuthCode() > responseJson :: " + responseJson);
				log.info("KfbApiService >> getAuthCode() > authCode :: " + responseJson.getString("authorize_code"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return authCode;
	}
	
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
			conn.setRequestProperty("X-Kfb-User-Secret", ClientSecret);
			//conn.setRequestProperty("Authorize_code", this.getAuthCode());
			conn.setRequestProperty("Authorize_code", "3");
			conn.setDoOutput(true); //POST일때만
			//conn.setDoInput(true);
			
			
			System.out.println("###########################");
			System.out.println("########11111111##################");
			System.out.println("############"+conn+"###############");
			System.out.println("###########################");
			System.out.println("############"+conn.getRequestMethod()+"###############");
			
			
			
			conn.connect();
			
			
			
			//요청 결과
			int responseCode = conn.getResponseCode();
			
			System.out.println("#############################");
			System.out.println("#############################");
			System.out.println("#######"+responseCode+"#######################");
			System.out.println("#############################");
			System.out.println("#############################");
			
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
		
		Map<String, Object> resultMap 	= new HashMap<String, Object>();
		String resultCode 				= "fail";
		
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
	            
	            //결과
	            resultCode = "success";
	            resultMap.put("responseJson", responseJson);
	            
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
	    
		return new ResponseMsg(HttpStatus.OK, resultCode, resultMap);
	}
	
	//가등록 : POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public ResponseMsg preLoanIndv(String authToken, JsonObject reqParam, String method) {
		
		Map<String, Object> resultMap 	= new HashMap<String, Object>();
		String resultCode 				= "fail";
		
		try {
			//URL 설정
			URL url 				= new URL(PreLoanUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod(method);
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
	        logParam.setUrl(PreLoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        log.info("KfbApiService >> preLoanIndv() > responseCode :: " + responseCode);
	        
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
	            
	            //결과
	            resultCode = "success";
	            resultMap.put("responseJson", responseJson);
	            
	            log.info("KfbApiService >> preLoanIndv() > responseJson :: " + responseJson);
	        } 
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    
		return new ResponseMsg(HttpStatus.OK, resultCode, resultMap);
	}
	
	//본등록 : POST(본등록 처리),GET(조회),PUT(수정),DELETE(삭제)
	public ResponseMsg loanIndv(String authToken, JsonObject reqParam, String method) {
		
		Map<String, Object> resultMap 	= new HashMap<String, Object>();
		String resultCode 				= "fail";
		
		try {
			//URL 설정
			URL url 				= new URL(LoanUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod(method);
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
	        logParam.setUrl(LoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        log.info("KfbApiService >> loanIndv() > responseCode :: " + responseCode);
	        
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
	            
	            //결과
	            resultCode = "success";
	            resultMap.put("responseJson", responseJson);
	            
	            log.info("KfbApiService >> loanIndv() > responseJson :: " + responseJson);
	        } 
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    
		return new ResponseMsg(HttpStatus.OK, resultCode, resultMap);
	}
	
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
		
        String successCheck = "fail";
        String message = "";
        JSONObject responseJson = new JSONObject();
        
        if(authToken == null) {
        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API오류 : 토큰을 확인해 주세요.\n시스템관리자에게 문의해 주세요.");
        }else if(apiNm == null) {
        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API오류 : API명을 확인해 주세요.\n시스템관리자에게 문의해 주세요.");
        }else if(methodType == null) {
        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API오류 : method형식을 확인해 주세요.\n시스템관리자에게 문의해 주세요.");
        }
        
	    try {
	        //URL 설정
	        URL url 				= new URL(apiNm);
	        HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
	        conn.setRequestMethod(methodType);
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("X-Kfb-Client-Id", ClientId);
			conn.setRequestProperty("X-Kfb-User-Secret", ClientSecret);
			conn.setRequestProperty("Authorize_code", "3");
			
	        if(methodType.equals("POST")) {
	        	conn.setDoOutput(true);
	        }
	        
	        if(reqParam != null) {
		        //요청 데이터 전송
		        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		        bw.write(reqParam.toString());
		        bw.flush();
		        bw.close();
	        }
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(apiNm);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        if(responseCode == 401) {
	        	System.out.println("401 : 인증오류");
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "401 : 인증오류\n시스템관리자에게 문의해 주세요.");
	        }else if(responseCode == 403) {
	        	System.out.println("403 : 접근권한이 없는 리소스 요청");
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "403 : 접근권한이 없는 리소스 요청\n시스템관리자에게 문의해 주세요.");
	        }else if(responseCode == 404) {
	        	System.out.println("404 : 해당 URI 없음");
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "404 : 해당 URI 없음\n시스템관리자에게 문의해 주세요.");
	        }else if(responseCode == 405) {
	        	System.out.println("405 : 지원하지 않는 메소드 호출");
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "405 : 지원하지 않는 메소드 호출\n시스템관리자에게 문의해 주세요.");
	        }else if(responseCode == 406) {
	        	System.out.println("406 : JSON 형식의 요청이 아닐 경우");
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "406 : JSON 형식 요청 확인\n시스템관리자에게 문의해 주세요.");
	        }else {
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            responseJson = new JSONObject(sb.toString());
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            successCheck = "success";
	        }
	        
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        System.out.println("not JSON Format response");
	        e.printStackTrace();
	    }
	    return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
}
