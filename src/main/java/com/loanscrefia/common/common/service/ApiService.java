package com.loanscrefia.common.common.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.service.BatchService;
import com.loanscrefia.util.OutApiConnector;
import com.loanscrefia.util.OutApiParse;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Slf4j
@Service
public class ApiService {
	
	public static String ClientId 			= "crefia"; 					//요청자 아이디
	public static String ClientSecret		= "781r671t905ehq46"; 			//요청자 비밀번호
	
	@Autowired private KfbApiRepository kfbApiRepo;

	
	@Autowired private KfbApiService kfbApiService;
	@Autowired private BatchService batchService;

	//API 서버 도메인(application.yml)
	@Value("${kfbApi.url}")
	public String apiURL;
	
	public ResponseMsg excuteApi(ApiDomain apiInfo)  {

		KfbApiDomain logParam = new KfbApiDomain();
        String token = "Bearer " + kfbApiRepo.selectNewKfbApiKey(logParam);
        logParam.setApiName(apiInfo.getApiName());
		logParam.setToken(token);
		logParam.setUrl(apiInfo.getUrl());

        if("GET".equals(apiInfo.getMethod()) || "DELETE".equals(apiInfo.getMethod()))   logParam.setSendData(apiInfo.getParam());
        else                         													logParam.setSendData(apiInfo.getParamJson().toString());
        
		logParam.setSendUser(1);
        int apiKey = kfbApiRepo.insertApiLog(logParam);
        logParam.setApiSeq(apiKey);
        
		OutApiConnector.setApi api = new OutApiConnector.setApi("", apiInfo.getApiName());
        api.url(apiURL+apiInfo.getUrl());
        api.token(token);
        api.method(apiInfo.getMethod()); 
        api.parameter("?"+apiInfo.getParam());
        api.parameterJson(apiInfo.getParamJson());
        api.clientId(ClientId);
        api.clientSecret(ClientSecret);
        api.outLog(true);
        
        int responseCode = 0;
        String resMsg = "res_msg :: null";
        String resCode = "000";
        String resExpMsg = null;
        String successCheck = "fail";
        JSONObject data =  null;
        try {
			Response res = api.call();
			responseCode = OutApiParse.getCode(res);
			if(responseCode == 200 || responseCode == 201) {
				String resData = OutApiParse.getData(res, "");
		        data =  new JSONObject(resData);
				
				if(data.getString("res_code").equals("200")) successCheck = "success";
				
	            resCode = data.getString("res_code");
	            resMsg = data.getString("res_msg");
				logParam.setResData(data.toString());
			}else {
				String resData = OutApiParse.getData(res, "");
		        data =  new JSONObject(resData);	
				
	            resCode = data.getString("res_code");
	            resMsg = data.getString("res_msg");
				logParam.setResData(data.toString());
				
				//배치등록*
				if("401".equals(resCode)) {
			    	log.info("================ Re apiAuthToken() START ================");
			    	// 금일 토큰 생성 확인
			    	kfbApiService.getAuthToken();
			    	log.info("================ Re apiAuthToken() END ================");
				}
			}
			 
		} catch (JSONException e) {
			resExpMsg = responseCode + " : "+ e.getMessage();
			resMsg = responseCode + " : "+ e.getMessage();
			log.error(e.getMessage());
		} catch (IOException e) {
			resExpMsg = responseCode + " : "+ e.getMessage();
			resMsg = responseCode + " : "+ e.getMessage();
			log.error(e.getMessage());
		} finally {

			logParam.setResConCode(responseCode);
			logParam.setResCode(resCode);
			logParam.setResMsg(resMsg);
			logParam.setResExpMsg(resExpMsg);
			kfbApiRepo.updateApiLog(logParam);
		}
        
		return new ResponseMsg(HttpStatus.OK, successCheck, data, resMsg);
			
	}
		
}
