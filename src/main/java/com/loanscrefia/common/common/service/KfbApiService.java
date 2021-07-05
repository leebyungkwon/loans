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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	//API 서버 도메인(application.yml)
	@Value("${kfbApi.url}")
	public static String ApiDomain;
	
	@Value("${kfbApi.url}")
	private void setApiDomain(String url) {
		ApiDomain = url;
	}
	public String getApiDomain() {
		return ApiDomain;
	}
	
	public static String ClientId 			= "crefia"; 					//요청자 아이디
	public static String ClientSecret		= "781r671t905ehq46"; 			//요청자 비밀번호
	
	//Authorize Code 발급
	public static String AuthCodeUrl		= "/oauth/2.0/authorize";		//POST
	
	//개인
	public static String CheckLoanUrl 		= "/loan/v1/check-loan-consultants"; 		//GET(등록가능 여부 조회)
	public static String PreLoanUrl			= "/loan/v1/pre-loan-consultants"; 			//POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public static String LoanUrl 			= "/loan/v1/loan-consultants"; 				//POST(본등록 처리),GET(조회),PUT(수정),DELETE(등록 말소)
	
	//법인
	public static String CheckLoanCorpUrl 	= "/loan/v1/check-loan-corp-consultants"; 	//GET(등록가능 여부 조회)
	public static String PreLoanCorpUrl		= "/loan/v1/pre-loan-corp-consultants"; 	//POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public static String LoanCorpUrl 		= "/loan/v1/loan-corp-consultants"; 		//POST(본등록 처리),GET(조회),PUT(수정),DELETE(등록 말소)
	
	//위반이력
	public static String ViolationUrl		= "/loan/v1/violation-consultants"; 		//POST(등록),GET(조회),PUT(수정),DELETE(삭제)
	
	//네트워크 및 서버상태확인
	public static String HealthCheckUrl		= "/loan/v1/health-check";	//POST
	
	//토큰 발급
	public static String TokenUrl			= "/oauth/2.0/token";		//POST
	
	//금융기관 조회
	public static String FinUrl				= "/loan/v1/fin-info";		//GET
	
	//주민등록번호 변경
	public static String modUrl				= "/loan/v1/mod-ssn";		//PUT
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 공통
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//네트워크 및 서버상태확인
	public ResponseMsg getHealthCheck(String apiDomain) {
		
		String result = "fail";
		
		try {
			//URL 설정
			URL url 				= new URL(apiDomain+HealthCheckUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			
			//요청 결과
			int responseCode = conn.getResponseCode();
			
			if(responseCode == 200) {
				result = "success";
				
				BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line 		= "";
				StringBuilder sb 	= new StringBuilder();
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				JSONObject responseJson = new JSONObject(sb.toString());

				return new ResponseMsg(HttpStatus.OK, result, responseJson, "성공");
			}
			
			conn.disconnect();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseMsg(HttpStatus.OK, result, null, "실패");
	}
	
	//Authorize Code 발급
	public String getAuthCode() {
		
		String authCode 		= "";
		
		try {
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+AuthCodeUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
	        
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("X-Kfb-Client-Id", ClientId);
			conn.setRequestProperty("X-Kfb-User-Secret", ClientSecret);
			conn.setDoOutput(false);
			
			//요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken("");
	        logParam.setUrl(this.getApiDomain()+AuthCodeUrl);
	        logParam.setSendData("");
	        this.insertKfbApiReqLog(logParam);
			
			//요청 결과
			int responseCode = conn.getResponseCode();
			
			if(responseCode == 200) {
				BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb 	= new StringBuilder();
				String line 		= "";
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				JSONObject responseJson = new JSONObject(sb.toString());
				
				System.out.println("#########################################");
				System.out.println("#########################################");
		        System.out.println("KfbApiService >> getAuthCode() > responseJson :: 결과값 " + responseJson);
		        System.out.println("#########################################");
		        System.out.println("#########################################");
				
				if(responseJson.getString("res_code").equals("200")) {
					authCode = responseJson.getString("authorize_code");
				}
				
				//응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
		        
			}else {
				//통신오류
				System.out.println("#########################################");
				System.out.println("#########################################");
		        System.out.println("KfbApiService >> getAuthCode() > 통신오류");
		        System.out.println("#########################################");
		        System.out.println("#########################################");
		        
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("getAuthCode() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
			}
			
			conn.disconnect();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return authCode;
	}
	
	//토큰 발급
	public ResponseMsg getAuthToken() {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		
		try {
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+TokenUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			//authCode
			String authCode 		= this.getAuthCode();
	        
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("X-Kfb-Client-Id", ClientId);
			conn.setRequestProperty("X-Kfb-User-Secret", ClientSecret);
			conn.setRequestProperty("Authorize_code", authCode);
			conn.setDoOutput(false);
			
			//요청 결과
			int responseCode = conn.getResponseCode();
			
			//요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken("");
	        logParam.setUrl(this.getApiDomain()+TokenUrl);
	        logParam.setSendData("Authorize_code :: " + authCode);
	        this.insertKfbApiReqLog(logParam);
			
			if(responseCode == 200) {
				BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb 	= new StringBuilder();
				String line 		= "";
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				responseJson = new JSONObject(sb.toString());
				
				System.out.println("#########################################");
				System.out.println("#########################################");
		        System.out.println("KfbApiService >> getAuthToken() > responseJson :: 결과값 " + responseJson);
		        System.out.println("#########################################");
		        System.out.println("#########################################");
				
				if(responseJson.getString("res_code").equals("200")) {
					//successCheck
					successCheck = "success";
					
					//토큰 DB 저장
					KfbApiDomain kfbApiDomain = new KfbApiDomain();
			        kfbApiDomain.setToken(responseJson.getString("authorization"));
			        kfbApiRepo.insertKfbApiKey(kfbApiDomain);
				}
				
				//응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
		        
			}else{
				System.out.println("#########################################");
				System.out.println("#########################################");
		        System.out.println("KfbApiService >> getAuthToken() > 통신오류");
		        System.out.println("#########################################");
		        System.out.println("#########################################");
		        
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("getAuthToken() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
			}
			
			conn.disconnect();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
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
	public String selectKfbApiKey(KfbApiDomain kfbApiDomain) {
		return kfbApiRepo.selectKfbApiKey(kfbApiDomain);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 개인
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//등록가능 여부 조회
	public ResponseMsg checkLoan(String authToken, JSONObject reqParam) {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//파라미터 설정
			String param = "?";
			param += "name="+reqParam.getString("name");
			param += "&ssn="+reqParam.getString("ssn");
			param += "&ci="+reqParam.getString("ci");
			param += "&loan_type="+reqParam.getString("loan_type");
			
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+CheckLoanUrl+param);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("Authorization", authToken);
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+CheckLoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            //응답 JSON
	            responseJson = new JSONObject(sb.toString());
	            
	            //message
		        message = responseJson.getString("res_msg");
	            
	            System.out.println("#########################################");
	            System.out.println("KfbApiService >> checkLoan() > responseJson :: " + responseJson);
	            System.out.println("#########################################");
	            
	            //결과
	            if(responseJson.getString("res_code").equals("200")) {
	            	//successCheck
	            	successCheck = "success";
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(message);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	        	
	        }else {
	        	System.out.println("#########################################");
		        System.out.println("#########################################");
		        System.out.println("KfbApiService >> checkLoan() > 통신오류");
		        System.out.println("#########################################");
		        System.out.println("#########################################");
		        
		        //message
		        message = "checkLoan() 메소드 확인 필요";
		        
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg(message);
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
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
	
	//가등록 : POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public ResponseMsg preLoanIndv(String authToken, JSONObject reqParam, String method) {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+PreLoanUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", authToken);
			
			if(!method.equals("GET")) {
				conn.setDoOutput(true);
				
				//요청 데이터 전송
		        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		        bw.write(reqParam.toString());
		        bw.flush();
		        bw.close();
			}
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+PreLoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            //응답 JSON
	            responseJson = new JSONObject(sb.toString());
	            
	            //message
		        message = responseJson.getString("res_msg");
	            
	            System.out.println("#########################################");
	            System.out.println("KfbApiService >> preLoanIndv() > responseJson :: " + responseJson);
	            System.out.println("#########################################");
	            
	            //결과
	            if(responseJson.getString("res_code").equals("200")) {
	            	//successCheck
	            	successCheck = "success";
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(message);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            
	        }else {
	        	System.out.println("#########################################");
		        System.out.println("#########################################");
		        System.out.println("KfbApiService >> preLoanIndv() > 통신오류");
		        System.out.println("#########################################");
		        System.out.println("#########################################");
		        
		        //message
		        message = "preLoanIndv() 메소드 확인 필요";
		        
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg(message);
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
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
	
	//본등록 : POST(본등록 처리),GET(조회),PUT(수정),DELETE(삭제)
	public ResponseMsg loanIndv(String authToken, JsonObject reqParam, String method) {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		
		try {
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+LoanUrl);
			HttpURLConnection conn 	= (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", authToken);
			
			if(method.equals("POST")) {
				conn.setDoOutput(true);
			}else {
				conn.setDoOutput(false);
			}
			
	        //요청 데이터 전송
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        bw.write(reqParam.toString());
	        bw.flush();
	        bw.close();
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+LoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        
	        System.out.println("#########################################");
	        System.out.println("#########################################");
	        System.out.println("KfbApiService >> loanIndv() > responseCode :: " + responseCode);
	        System.out.println("#########################################");
	        System.out.println("#########################################");
	        
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
			}else if(responseCode == 420) {
				System.out.println("420 : 필수 파라미터 미입력");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "420 : 필수 파라미터 미입력\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 421) {
				System.out.println("421 : 파라미터 형식 오류");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "421 : 파라미터 형식 오류\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 422) {
				System.out.println("422 : 타 협회 가등록 진행 중");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "422 : 타 협회 가등록 진행 중\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 423) {
				System.out.println("423 : 대출모집인 유형 중복");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "423 : 대출모집인 유형 중복\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 424) {
				System.out.println("424 : 해당 데이터 없음");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "424 : 해당 데이터 없음\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 425) {
				System.out.println("425 : 본 등록 완료 된 가등록");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "425 : 본 등록 완료 된 가등록\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 426) {
				System.out.println("426 : 취소 된 가등록");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "426 : 취소 된 가등록\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 427) {
				System.out.println("427 : 가등록 기간 만료");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "427 : 가등록 기간 만료\n시스템관리자에게 문의해 주세요.");
			}else if(responseCode == 428) {
				System.out.println("428 : 업권이 일치하지 않음");
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "428 : 업권이 일치하지 않음\n시스템관리자에게 문의해 주세요.");
			}else {
				//성공
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            responseJson = new JSONObject(sb.toString());
	            
	            System.out.println("#########################################");
		        System.out.println("#########################################");
		        System.out.println("KfbApiService >> loanIndv() > responseJson :: " + responseJson);
		        System.out.println("#########################################");
		        System.out.println("#########################################");
	            
	            //응답 이력 저장
	            logParam.setResCode(responseJson.getString("res_code"));
	            logParam.setResMsg(responseJson.getString("res_msg"));
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
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
        
        if(authToken == null && !"healthCheck".equals(authToken)) {
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
			conn.setRequestProperty("Authorization", "Bearer "+authToken);
			
	        if(!methodType.equals("GET")) {
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
	        if(reqParam != null) {
	        	logParam.setSendData(reqParam.toString());
	        }
	        this.insertKfbApiReqLog(logParam);
	        
	        //요청 결과
	        int responseCode = conn.getResponseCode();
	        if(responseCode == 200) {
	        	
	        	BufferedReader br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
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
	            br.close();
	            
	            return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, responseJson.getString("res_msg"));
	            
	        }else {
	        	// 통신오류 - 응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("API통신오류발생");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        	
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API통신오류 : 시스템관리자에게 문의해 주세요.");
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
