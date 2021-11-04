package com.loanscrefia.common.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.util.OutApiConnector;
import com.loanscrefia.util.OutApiParse;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Slf4j
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
	public ResponseMsg getHealthCheck(String apiDomain) throws IOException {
		
		
		String result = "fail";
		int TIMEOUT_VALUE = 3000;		// 3초
		
		HttpURLConnection conn = null;
		BufferedReader br = null;
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		JSONObject responseJson = new JSONObject();
		
		try {
			//URL 설정
			URL url 				= new URL(apiDomain+HealthCheckUrl);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			
			
			
			// 2021-10-05 api_log 생성
	        newLogParam.setToken("");
	        newLogParam.setUrl(this.getApiDomain()+AuthCodeUrl);
	        newLogParam.setSendData("getHealthCheck");
	        newLogParam.setSendUser(1);
            apiKey = this.insertNewKfbApiLog(newLogParam);
			
			//요청 결과
			responseCode = conn.getResponseCode();
			
			if(responseCode == 200) {
				result = "success";
				apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
				
				responseJson = new JSONObject(sb.toString());
	            log.error("########m 시작 8  ::  commonKfbApi()");
	            
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
	            }
	            
	            KfbApiDomain logParam = new KfbApiDomain();
	            //응답 이력 저장
	            logParam.setToken("서버상태확인");
	            logParam.setUrl("/loan/v1/health-check");
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            log.error("########m 시작 9  ::  commonKfbApi()");
	            this.insertKfbApiResLog(logParam);
				conn.disconnect();
				return new ResponseMsg(HttpStatus.OK, result, responseJson, "성공");
			}else {
				apiCheck = false;
				conn.disconnect();
				return new ResponseMsg(HttpStatus.OK, result, null, "실패");
			}
			
			
		} catch (Exception e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
			
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
		
		return new ResponseMsg(HttpStatus.OK, result, null, "실패");
	}
	
	//Authorize Code 발급
	public String getAuthCode() throws IOException {
		
		String authCode 		= "";
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+AuthCodeUrl);
			conn 	= (HttpURLConnection)url.openConnection();
	        
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
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
				br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb 	= new StringBuilder();
				String line 		= "";
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				br.close();
				
				JSONObject responseJson = new JSONObject(sb.toString());
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
				log.error("#########################################");
		        log.error("KfbApiService >> getAuthCode() > 통신오류");
		        log.error("#########################################");
		        
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
		} finally {
			
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
		
		return authCode;
	}
	
	//토큰 발급
	public ResponseMsg getAuthToken() throws IOException {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		try {
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+TokenUrl);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//authCode
			String authCode 		= this.getAuthCode();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
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
				br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb 	= new StringBuilder();
				String line 		= "";
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				responseJson = new JSONObject(sb.toString());
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
				log.error("#########################################");
		        log.error("KfbApiService >> getAuthToken() > 통신오류");
		        log.error("#########################################");
		        
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
		} finally {
			
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
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
	
	//API 로그 INSERT
	@Transactional
	public int insertNewKfbApiLog(KfbApiDomain kfbApiDomain) {
		return kfbApiRepo.insertNewKfbApiLog(kfbApiDomain);
	}
	
	//API 로그 UPDATE
	@Transactional
	public int updateNewKfbApiLog(KfbApiDomain kfbApiDomain) {
		return kfbApiRepo.updateNewKfbApiLog(kfbApiDomain);
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
	public ResponseMsg checkLoan(String authToken, JSONObject reqParam) throws IOException {

		//파라미터 설정
		String param = "?";
		String testName = URLEncoder.encode(reqParam.getString("name"),"UTF-8");
		param += "name="+testName;
		param += "&ssn="+reqParam.getString("ssn");
		param += "&ci="+reqParam.getString("ci");
		param += "&loan_type="+reqParam.getString("loan_type");
		
		
	    OutApiConnector.setApi api = new OutApiConnector.setApi("", "checkLoan");
        api.url(this.getApiDomain()+CheckLoanUrl+param);
        api.parameterJson(null);
        api.token(authToken);
        api.method("GET");

        log.info("## 호출 START");
        Response res = api.call();
        //System.out.println(res);

		String successCheck 	= "fail";
		String message 			= "";
        String resMsg = "res_msg :: null";
        String resCode = "000";
        
		JSONObject responseJson = new JSONObject();
		

		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;        
		// 2021-10-05 api_log 생성
        newLogParam.setToken(authToken);
        newLogParam.setUrl(this.getApiDomain()+CheckLoanUrl);
        newLogParam.setSendData(reqParam.toString());
        apiKey = this.insertNewKfbApiLog(newLogParam);
        int responseCode = OutApiParse.getCode(res);
        if (OutApiParse.getCode(res) == HttpURLConnection.HTTP_CREATED) {
        	newLogParam.setResYn("Y");
            log.info("## 호출 성공 ");
            String dlvRsvNo = OutApiParse.getData(res, "");
            log.info("## 호출 DATA = " + dlvRsvNo);
            responseJson = new JSONObject(dlvRsvNo);
            successCheck = "success";
            resCode = responseJson.getString("res_code");
            resMsg = responseJson.getString("res_msg");
        } else {
        	newLogParam.setResYn("N");
            log.info("## 호출 실패");
            JSONObject err = OutApiParse.getError(res);
            responseJson = err;
            log.info(String.valueOf(err));
        }
        

        newLogParam.setApiSeq(apiKey);
        newLogParam.setResConCode(responseCode);
        newLogParam.setResCode(resCode);
        newLogParam.setResMsg(resMsg);
        newLogParam.setResData(responseJson.toString());
        this.updateNewKfbApiLog(newLogParam);
        
        /*
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		int TIMEOUT_VALUE = 3000;		// 3초
		
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//파라미터 설정
			String param = "?";
			String testName = URLEncoder.encode(reqParam.getString("name"),"UTF-8");
			param += "name="+testName;
			param += "&ssn="+reqParam.getString("ssn");
			param += "&ci="+reqParam.getString("ci");
			param += "&loan_type="+reqParam.getString("loan_type");
			
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+CheckLoanUrl+param);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
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
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl(this.getApiDomain()+CheckLoanUrl);
	        newLogParam.setSendData(reqParam.toString());
            apiKey = this.insertNewKfbApiLog(newLogParam);
	    	
	        responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
		    	apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            responseJson = new JSONObject(sb.toString());
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
		            if(responseJson.getString("res_code").equals("200")) {
		            	//successCheck
		            	successCheck = "success";
		            }
	            }
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
	            }
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            conn.disconnect();
	        	
	        }else {
	        	apiCheck = false;
		        //message
		        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
		        resExpMsg = message;
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("checkLoan() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m MalformedURLException  ::  checkLoan()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m IOException  ::  checkLoan()");
	    	log.error(e.getMessage());
	    	log.error(e.toString());
	        e.printStackTrace();
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m JSONException  ::  checkLoan()");
	    	log.error(e.getMessage());
	        log.info("not JSON Format response");
	        e.printStackTrace();
	    } finally {
	    	
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
	    	
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    */
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	//확인용
	public ResponseMsg chkLoanIndv(String authToken, JSONObject reqParam, String method) throws IOException {
		//connect URL
		String connUrl = this.getApiDomain()+PreLoanUrl;
		
		//파라미터 설정
		if(method.equals("GET") || method.equals("DELETE")) {
			String param = "?pre_lc_num="+reqParam.getString("pre_lc_num");
			
			connUrl = connUrl + param;
		}
		
		OutApiConnector.setApi api = new OutApiConnector.setApi("", "preLoanIndv");
        api.url(connUrl);
        api.parameterJson(null);
        api.token(authToken);
        api.method(method);

        log.info("## 호출 START");
        Response res = api.call();
        //System.out.println(res);

		String successCheck 	= "fail";
		String message 			= "";
        String resMsg = "res_msg :: null";
        String resCode = "000";
        
		JSONObject responseJson = new JSONObject();
		

		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;        
		// 2021-10-05 api_log 생성
        newLogParam.setToken(authToken);
        newLogParam.setUrl(this.getApiDomain()+CheckLoanUrl);
        newLogParam.setSendData(reqParam.toString());
        apiKey = this.insertNewKfbApiLog(newLogParam);
        int responseCode = OutApiParse.getCode(res);
        if (OutApiParse.getCode(res) == HttpURLConnection.HTTP_CREATED
        		|| OutApiParse.getCode(res) == HttpURLConnection.HTTP_OK) {
        	newLogParam.setResYn("Y");
            log.info("## 호출 성공 ");
            String dlvRsvNo = OutApiParse.getData(res, "");
            log.info("## 호출 DATA = " + dlvRsvNo);
            responseJson = new JSONObject(dlvRsvNo);
            successCheck = "success";
            resCode = responseJson.getString("res_code");
            resMsg = responseJson.getString("res_msg");
        } else {
        	newLogParam.setResYn("N");
            log.info("## 호출 실패");
            JSONObject err = OutApiParse.getError(res);
            responseJson = err;
            log.info(String.valueOf(err));
        }
        

        newLogParam.setApiSeq(apiKey);
        newLogParam.setResConCode(responseCode);
        newLogParam.setResCode(resCode);
        newLogParam.setResMsg(resMsg);
        newLogParam.setResData(responseJson.toString());
        this.updateNewKfbApiLog(newLogParam);
        
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	//가등록 : POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public ResponseMsg preLoanIndv(String authToken, JSONObject reqParam, String method) throws IOException {
			
			String successCheck 	= "fail";
			String message 			= "";
			JSONObject responseJson = new JSONObject();
			int TIMEOUT_VALUE = 3000;		// 3초
			HttpURLConnection conn = null;
			BufferedReader br = null;
			
			
			int responseCode = 0;
			boolean apiCheck = false; 
	        String resCode = "000";
	        String resMsg = "res_msg :: null";
	        String resExpMsg = "";
			
			KfbApiDomain newLogParam = new KfbApiDomain();
			int apiKey = 0;
			
			if(StringUtils.isEmpty(authToken)) {
				return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
			}else {
				authToken = "Bearer " + authToken;
			}
			
			try {
				//connect URL
				String connUrl = this.getApiDomain()+PreLoanUrl;
				
				//파라미터 설정
				if(method.equals("GET") || method.equals("DELETE")) {
					String param = "?pre_lc_num="+reqParam.getString("pre_lc_num");
					
					connUrl = connUrl + param;
				}
				
				//URL 설정
				URL url 				= new URL(connUrl);
				conn 	= (HttpURLConnection)url.openConnection();
				
				//2021-09-30 timeout설정
				conn.setConnectTimeout(TIMEOUT_VALUE);
				conn.setReadTimeout(TIMEOUT_VALUE);
				
				// 2021-11-04 keep-alive관련 옵션 추가
				conn.setRequestProperty("Connection", "close");
				
				conn.setRequestMethod(method);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("Authorization", authToken);

				if(method.equals("POST")) {
					conn.setDoOutput(true);
					
					//요청 데이터 전송
					OutputStream os = conn.getOutputStream(); 
					os.write(reqParam.toString().getBytes("utf-8")); //*****
					os.flush();
					os.close();
				}
				
		        //요청 이력 저장
		        KfbApiDomain logParam = new KfbApiDomain();
		        logParam.setToken(authToken);
		        logParam.setUrl(this.getApiDomain()+PreLoanUrl);
		        logParam.setSendData(reqParam.toString());
		        this.insertKfbApiReqLog(logParam);
		        
		        
				// 2021-10-05 api_log 생성
		        newLogParam.setToken(authToken);
		        newLogParam.setUrl(this.getApiDomain()+PreLoanUrl);
		        newLogParam.setSendData(reqParam.toString());
	            apiKey = this.insertNewKfbApiLog(newLogParam);
		        
		        //요청 결과
		        responseCode = conn.getResponseCode();
		        
		        if(responseCode == 200) {
		        	apiCheck = true;
		        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        	StringBuilder sb 	= new StringBuilder();
		        	String line 		= "";
		        	
		            while((line = br.readLine()) != null) {
		            	sb.append(line);
		            }
		            
		            br.close();
		            
		            responseJson = new JSONObject(sb.toString());
		            
		            if(!responseJson.isNull("res_code")) {
		            	resCode = responseJson.getString("res_code");
			            if(responseJson.getString("res_code").equals("200")) {
			            	//successCheck
			            	successCheck = "success";
			            }
		            }
		            if(!responseJson.isNull("res_msg")) {
		            	resMsg = responseJson.getString("res_msg");
		            	message = responseJson.getString("res_msg");
		            }
		            
		            //응답 이력 저장
		            logParam.setResCode(resCode);
		            logParam.setResMsg(resMsg);
		            logParam.setResData(responseJson.toString());
		            this.insertKfbApiResLog(logParam);
		            
		            conn.disconnect();
		            
		        }else {
		        	apiCheck = false;
			        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
			        resExpMsg = message;
			        //응답 이력 저장
		            logParam.setResCode(Integer.toString(responseCode));
		            logParam.setResMsg("HTTP Method [" + method + "] :: preLoanIndv() 메소드 확인 필요");
		            logParam.setResData("empty");
		            this.insertKfbApiResLog(logParam);
		        }
		        
		        conn.disconnect();
		        
		    } catch (MalformedURLException e) {
				apiCheck = false;
				resExpMsg = e.getMessage();
		    	log.error("########m MalformedURLException  ::  preLoanIndv()");
		    	log.error(e.getMessage());
		        e.printStackTrace();
		    } catch (IOException e) {
				apiCheck = false;
				resExpMsg = e.getMessage();
		    	log.error("########m IOException  ::  preLoanIndv()");
		    	log.error(e.getMessage());
		        e.printStackTrace();
		    } catch (JSONException e) {
				apiCheck = false;
				resExpMsg = e.getMessage();
		    	log.error("########m JSONException  ::  preLoanIndv()");
		    	log.error(e.getMessage());
		        e.printStackTrace();
		    } finally {
		    	
	            // 2021-10-05 api_log 생성
	            newLogParam.setApiSeq(apiKey);
	            if(apiCheck == true) {
	            	newLogParam.setResYn("Y");
	            }else {
	            	newLogParam.setResYn("N");
	            	newLogParam.setResExpMsg(resExpMsg);
	            }
	            newLogParam.setResConCode(responseCode);
	            newLogParam.setResCode(resCode);
	            newLogParam.setResMsg(resMsg);
	            newLogParam.setResData(responseJson.toString());
	            this.updateNewKfbApiLog(newLogParam);
				
				// 2021-10-05 disconnect 강제 종료 실행
				if(conn != null) {
					conn.disconnect();
				}
				conn = null;
				
				// 2021-10-05 BufferedReader 강제 종료 실행
				if(br != null) {
					br.close();
				}
				br = null;
			}
		    
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	//본등록 : POST(본등록 처리),GET(조회),PUT(수정),DELETE(삭제)
	public ResponseMsg loanIndv(String authToken, JSONObject reqParam, String method) throws IOException {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		
		try {
			//connect URL
			String connUrl = this.getApiDomain()+LoanUrl;
			
			//파라미터 설정
			if(method.equals("GET") || method.equals("DELETE")) {
				String param = "?pre_lc_num="+reqParam.getString("pre_lc_num");
				
				connUrl = connUrl + param;
			}
			
			//URL 설정
			URL url 				= new URL(connUrl);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", authToken);
			
			if(method.equals("POST") || method.equals("PUT")) {
				conn.setDoOutput(true);
				
				//요청 데이터 전송
				OutputStream os = conn.getOutputStream(); 
				os.write(reqParam.toString().getBytes("utf-8")); //*****
				os.flush();
				os.close();
			}
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+LoanUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl(this.getApiDomain()+LoanUrl);
	        newLogParam.setSendData(reqParam.toString());
            apiKey = this.insertNewKfbApiLog(newLogParam);
	        
	        //요청 결과
	        responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            responseJson = new JSONObject(sb.toString());
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            
	            //결과
	            if(resCode.equals("200")) {
	            	//successCheck
	            	successCheck = "success";
	            }
	            
	            conn.disconnect();
	            
	        }else {
	        	apiCheck = false;		        
		        //message
		        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
		        resExpMsg = message;
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("HTTP Method [" + method + "] :: loanIndv() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m MalformedURLException  ::  loanIndv()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m IOException  ::  loanIndv()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m JSONException  ::  loanIndv()");
	    	log.error(e.getMessage());
	        log.info("not JSON Format response");
	        e.printStackTrace();
	    } finally {
	    	
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
            
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 법인
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//등록가능 여부 조회
	public ResponseMsg checkLoanCorp(String authToken, JSONObject reqParam) throws IOException {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//파라미터 설정
			String param = "?";
			param += "corp_num="+reqParam.getString("corp_num");
			param += "&corp_rep_ssn="+reqParam.getString("corp_rep_ssn");
			param += "&corp_rep_ci="+reqParam.getString("corp_rep_ci");
			param += "&loan_type="+reqParam.getString("loan_type");
			
			//URL 설정
			URL url 				= new URL(this.getApiDomain()+CheckLoanCorpUrl+param);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("Authorization", authToken);
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+CheckLoanCorpUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl(this.getApiDomain()+CheckLoanCorpUrl);
	        newLogParam.setSendData(reqParam.toString());
            apiKey = this.insertNewKfbApiLog(newLogParam);
	        
	        //요청 결과
	        responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            //응답 JSON
	            responseJson = new JSONObject(sb.toString());
	            
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
		            if(responseJson.getString("res_code").equals("200")) {
		            	successCheck = "success";
		            }
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
			        message = responseJson.getString("res_msg");
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            
	            conn.disconnect();
	        	
	        }else {
	        	apiCheck = false;
		        //message
		        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
		        resExpMsg = message;
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("checkLoanCorp() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	            conn.disconnect();
	        }
	        
	        conn.disconnect();
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m MalformedURLException  ::  checkLoanCorp()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m IOException  ::  checkLoanCorp()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m JSONException  ::  checkLoanCorp()");
	    	log.error(e.getMessage());
	        log.info("not JSON Format response");
	        e.printStackTrace();
	    } finally {
			
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
	    	
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	//가등록 : POST(가등록 처리),GET(가등록 조회),DELETE(가등록 취소)
	public ResponseMsg preLoanCorp(String authToken, JSONObject reqParam, String method) throws IOException {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//connect URL
			String connUrl = this.getApiDomain()+PreLoanCorpUrl;
			
			//파라미터 설정
			if(method.equals("GET") || method.equals("DELETE")) {
				String param = "?pre_corp_lc_num="+reqParam.getString("pre_corp_lc_num");
				
				connUrl = connUrl + param;
			}
			
			//URL 설정
			URL url 				= new URL(connUrl);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", authToken);

			if(method.equals("POST")) {
				conn.setDoOutput(true);
				
				//요청 데이터 전송
				OutputStream os = conn.getOutputStream(); 
				os.write(reqParam.toString().getBytes("utf-8")); //*****
				os.flush();
				os.close();
			}
			
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+PreLoanCorpUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl(this.getApiDomain()+PreLoanCorpUrl);
	        newLogParam.setSendData(reqParam.toString());
            apiKey = this.insertNewKfbApiLog(newLogParam);
	        
	        //요청 결과
	        responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            //응답 JSON
	            responseJson = new JSONObject(sb.toString());
	            
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
		            if(responseJson.getString("res_code").equals("200")) {
		            	successCheck = "success";
		            }
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
			        message = responseJson.getString("res_msg");
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            conn.disconnect();
	        }else {
				apiCheck = false;
				conn.disconnect();
		        //message
		        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
		        resExpMsg = message;
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("HTTP Method [" + method + "] :: preLoanCorp() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m MalformedURLException  ::  preLoanCorp()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m IOException  ::  preLoanCorp()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m JSONException  ::  preLoanCorp()");
	    	log.error(e.getMessage());
	        log.info("not JSON Format response");
	        e.printStackTrace();
	    } finally {
			
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
	    	
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	//본등록 : POST(본등록 처리),GET(조회),PUT(수정),DELETE(삭제)
	public ResponseMsg loanCorp(String authToken, JSONObject reqParam, String method) throws IOException {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//connect URL
			String connUrl = this.getApiDomain()+LoanCorpUrl;
			
			//파라미터 설정
			if(method.equals("GET") || method.equals("DELETE")) {
				String param = "?pre_corp_lc_num="+reqParam.getString("pre_corp_lc_num");
				connUrl = connUrl + param;
			}
			
			//URL 설정
			URL url 				= new URL(connUrl);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", authToken);
			
			if(method.equals("POST") || method.equals("PUT")) {
				conn.setDoOutput(true);
				
				//요청 데이터 전송
				OutputStream os = conn.getOutputStream(); 
				os.write(reqParam.toString().getBytes("utf-8")); //*****
				os.flush();
				os.close();
			}
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+LoanCorpUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl(this.getApiDomain()+LoanCorpUrl);
	        newLogParam.setSendData(reqParam.toString());
            apiKey = this.insertNewKfbApiLog(newLogParam);
	        
	        //요청 결과
	        responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            //응답 JSON
	            responseJson = new JSONObject(sb.toString());
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);

	            //결과
	            if(resCode.equals("200")) {
	            	successCheck = "success";
	            }
	            
	            conn.disconnect();
	            
	        }else {
				apiCheck = false;
				conn.disconnect();
		        
		        //message
		        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
		        resExpMsg = message;
		        
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("HTTP Method [" + method + "] :: loanCorp() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m MalformedURLException  ::  loanCorp()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m IOException  ::  loanCorp()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m JSONException  ::  loanCorp()");
	    	log.error(e.getMessage());
	        log.info("not JSON Format response");
	        e.printStackTrace();
	    } finally {
	    	
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
			
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 위반이력 : POST(등록),GET(조회),PUT(수정),DELETE(삭제)
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	public ResponseMsg violation(String authToken, JSONObject reqParam, String method) throws IOException {
		
		String successCheck 	= "fail";
		String message 			= "";
		JSONObject responseJson = new JSONObject();
		int TIMEOUT_VALUE = 3000;		// 3초
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API 토큰 오류 : 시스템관리자에게 문의해 주세요.");
		}else {
			authToken = "Bearer " + authToken;
		}
		
		try {
			//connect URL
			String connUrl 	= this.getApiDomain()+ViolationUrl;
			String param	= "";
			
			//파라미터 설정
			if(method.equals("GET")) {
				param = "?ssn="+reqParam.getString("ssn");
			}else if(method.equals("DELETE")) {
				param = "?vio_num="+reqParam.getString("vio_num");
			}
			connUrl = connUrl + param;
			
			//URL 설정
			URL url 				= new URL(connUrl);
			conn 	= (HttpURLConnection)url.openConnection();
			
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
			
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", authToken);
			
			if(method.equals("POST") || method.equals("PUT")) {
				conn.setDoOutput(true);
				
				//요청 데이터 전송
				OutputStream os = conn.getOutputStream(); 
				os.write(reqParam.toString().getBytes("utf-8")); //*****
				os.flush();
				os.close();
			}
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl(this.getApiDomain()+ViolationUrl);
	        logParam.setSendData(reqParam.toString());
	        this.insertKfbApiReqLog(logParam);
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl(this.getApiDomain()+ViolationUrl);
	        newLogParam.setSendData(reqParam.toString());
            apiKey = this.insertNewKfbApiLog(newLogParam);
	        
	        //요청 결과
	        responseCode = conn.getResponseCode();
	        
	        if(responseCode == 200) {
	        	apiCheck = true;
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        	StringBuilder sb 	= new StringBuilder();
	        	String line 		= "";
	        	
	            while((line = br.readLine()) != null) {
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            //응답 JSON
	            responseJson = new JSONObject(sb.toString());
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
		            if(responseJson.getString("res_code").equals("200")) {
		            	//successCheck
		            	successCheck = "success";
		            }
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
	            	message = responseJson.getString("res_msg");
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            this.insertKfbApiResLog(logParam);
	            conn.disconnect();
	        }else {
				apiCheck = false;
				conn.disconnect();
		        
		        //message
		        message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
		        resExpMsg = message;
		        //응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("HTTP Method [" + method + "] :: violoation() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	        }
	        
	        conn.disconnect();
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	        e.printStackTrace();
	    	log.error("########m MalformedURLException  ::  violation()");
	    	log.error(e.getMessage());
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	        e.printStackTrace();
	    	log.error("########m IOException  ::  violation()");
	    	log.error(e.getMessage());
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	        log.info("not JSON Format response");
	    	log.error("########m JSONException  ::  violation()");
	    	log.error(e.getMessage());
	        e.printStackTrace();
	    } finally {
	    	
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
			
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    
		return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동 > 샘플
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	public ResponseMsg commonKfbApi(String authToken, JSONObject reqParam, String apiNm, String methodType, String plClass, String preYn) throws IOException {
		
        String successCheck 	= "fail";
        String message 			= "";
        JSONObject responseJson = new JSONObject();
        int TIMEOUT_VALUE = 3000;		// 3초
        HttpURLConnection conn = null;
        BufferedReader br = null;
        
		int responseCode = 0;
		boolean apiCheck = false; 
        String resCode = "000";
        String resMsg = "res_msg :: null";
        String resExpMsg = "";
		
		KfbApiDomain newLogParam = new KfbApiDomain();
		int apiKey = 0;
        
        if(authToken == null && !"healthCheck".equals(authToken)) {
        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API오류 : 토큰을 확인해 주세요.\n시스템관리자에게 문의해 주세요.");
        }else if(apiNm == null) {
        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API오류 : API명을 확인해 주세요.\n시스템관리자에게 문의해 주세요.");
        }else if(methodType == null) {
        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API오류 : method형식을 확인해 주세요.\n시스템관리자에게 문의해 주세요.");
        }
        
        log.error("########m 시작 1111111  ::  commonKfbApi()");
        
	    try {
			String connUrl 	= apiNm;
			String param 	= "";
			
			//파라미터 설정
			if(methodType.equals("GET") || methodType.equals("DELETE")) {
				if("1".equals(plClass)) {
					if("Y".equals(preYn)) {
						param = "?pre_lc_num="+reqParam.getString("pre_lc_num");
					}else {
						param = "?lc_num="+reqParam.getString("lc_num");
					}
				}else {
					if("Y".equals(preYn)) {
						param = "?pre_corp_lc_num="+reqParam.getString("pre_corp_lc_num");
					}else {
						param = "?corp_lc_num="+reqParam.getString("corp_lc_num");
					}
				}
				connUrl = connUrl + param;
			}
	    	
			
			log.error("########m 시작 2  ::  commonKfbApi()");
			
	        //URL 설정
	        URL url 				= new URL(connUrl);
	        conn 	= (HttpURLConnection)url.openConnection();
	        
	        
	        log.error("########m 시작 3  ::  commonKfbApi()");
	        
			//2021-09-30 timeout설정
			conn.setConnectTimeout(TIMEOUT_VALUE);
			conn.setReadTimeout(TIMEOUT_VALUE);
			
			// 2021-11-04 keep-alive관련 옵션 추가
			conn.setRequestProperty("Connection", "close");
	        
	        conn.setRequestMethod(methodType);
			conn.setRequestProperty("Content-Type", "application/json"); //요청
			conn.setRequestProperty("Accept", "application/json"); //응답
			conn.setRequestProperty("Authorization", "Bearer "+authToken);
			
	        if(methodType.equals("POST") || methodType.equals("PUT")) {
	        	conn.setDoOutput(true);
		        if(reqParam != null) {
		        	
		        	log.error("########m 시작 3 - 1  ::  commonKfbApi()");
			        //요청 데이터 전송
					OutputStream os = null;
					os = conn.getOutputStream(); 
					os.write(reqParam.toString().getBytes("utf-8")); 
					os.flush();
					os.close();
		        }
	        }
	        log.error("########m 시작 4 ::  commonKfbApi()");
	        
	        //요청 이력 저장
	        KfbApiDomain logParam = new KfbApiDomain();
	        logParam.setToken(authToken);
	        logParam.setUrl("HTTP Method [" + methodType + "] ::" +connUrl);
	        if(reqParam != null) {
	        	logParam.setSendData(reqParam.toString());
	        }
	        this.insertKfbApiReqLog(logParam);
	        
	        
	        
			// 2021-10-05 api_log 생성
	        newLogParam.setToken(authToken);
	        newLogParam.setUrl("HTTP Method [" + methodType + "] ::" +connUrl);
	        if(reqParam != null) {
	        	newLogParam.setSendData(reqParam.toString());
	        }
            apiKey = this.insertNewKfbApiLog(newLogParam);
	        
	        
	        //요청 결과
	        responseCode = conn.getResponseCode();
	        log.error("########m 시작 6  ::  commonKfbApi()");
	        if(responseCode == 200) {
	        	apiCheck = true;
	        	log.error("########m 시작 7  ::  commonKfbApi()");
	        	br 	= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        	log.error("########m 시작 7-1  ::  commonKfbApi()");
	        	StringBuilder sb 	= new StringBuilder();
	        	
	        	String line 		= "";
	        	log.error("########m 시작 7-1 a  ::  " );
	        	
	            while((line = br.readLine()) != null) {
		        	log.error("########m 시작 7-2  ::  "+line);
	            	sb.append(line);
	            }
	            
	            br.close();
	            
	            log.error("########m 시작 8  ::  commonKfbApi()");
	            responseJson = new JSONObject(sb.toString());
	            log.error("########m 시작 8-1  ::  commonKfbApi() " + responseJson.toString());
	            if(!responseJson.isNull("res_code")) {
	            	resCode = responseJson.getString("res_code");
	            }
	            
	            if(!responseJson.isNull("res_msg")) {
	            	resMsg = responseJson.getString("res_msg");
	            }
	            
	            //응답 이력 저장
	            logParam.setResCode(resCode);
	            logParam.setResMsg(resMsg);
	            logParam.setResData(responseJson.toString());
	            
	            log.error("########m 시작 9  ::  commonKfbApi()");
	            
	            this.insertKfbApiResLog(logParam);
	            conn.disconnect();

	            //결과
	            if(resCode.equals("200")) {
	            	//successCheck
	            	successCheck = "success";
	            }
	            
	            log.error("########m 시작 10  ::  commonKfbApi()");
	            
	            return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, responseJson.getString("res_msg"));
	        }else {
				apiCheck = false;
				message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
				resExpMsg = "API통신오류 : 시스템관리자에게 문의해 주세요.";
	            log.error("########m error 1-1  ::  commonKfbApi()");
	        	// 통신오류 - 응답 이력 저장
	            logParam.setResCode(Integer.toString(responseCode));
	            logParam.setResMsg("HTTP Method [" + methodType + "] :: commonKfbApi() 메소드 확인 필요");
	            logParam.setResData("empty");
	            this.insertKfbApiResLog(logParam);
	            conn.disconnect();
	        	return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, "API통신오류 : 시스템관리자에게 문의해 주세요.");
	        }
	        
	    } catch (MalformedURLException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m MalformedURLException  ::  commonKfbApi()");
	    	log.error(e.getMessage());
	    	message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
	        e.printStackTrace();
	    } catch (IOException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m IOException   ::  commonKfbApi()");
	    	log.error(e.getMessage());
	    	log.error(e.toString());
	    	message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
	        e.printStackTrace();
	    } catch (JSONException e) {
			apiCheck = false;
			resExpMsg = e.getMessage();
	    	log.error("########m JSONException   ::  commonKfbApi()");
	    	log.error(e.getMessage());
	    	message = "API통신오류 : 시스템관리자에게 문의해 주세요.";
	        e.printStackTrace();
	    } finally {
	    	
            // 2021-10-05 api_log 생성
            newLogParam.setApiSeq(apiKey);
            if(apiCheck == true) {
            	newLogParam.setResYn("Y");
            }else {
            	newLogParam.setResYn("N");
            	newLogParam.setResExpMsg(resExpMsg);
            }
            newLogParam.setResConCode(responseCode);
            newLogParam.setResCode(resCode);
            newLogParam.setResMsg(resMsg);
            newLogParam.setResData(responseJson.toString());
            this.updateNewKfbApiLog(newLogParam);
			
			// 2021-10-05 disconnect 강제 종료 실행
			if(conn != null) {
				conn.disconnect();
			}
			conn = null;
			
			// 2021-10-05 BufferedReader 강제 종료 실행
			if(br != null) {
				br.close();
			}
			br = null;
		}
	    return new ResponseMsg(HttpStatus.OK, successCheck, responseJson, message);
	}
}
