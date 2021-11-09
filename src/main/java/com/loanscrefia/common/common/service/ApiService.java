package com.loanscrefia.common.common.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.util.OutApiConnector;
import com.loanscrefia.util.OutApiParse;

import okhttp3.Response;

@Service
public class ApiService {
	
	public static String ClientId 			= "crefia"; 					//요청자 아이디
	public static String ClientSecret		= "781r671t905ehq46"; 			//요청자 비밀번호
	
	@Autowired private KfbApiRepository kfbApiRepo;
	
	@Transactional
	public ResponseMsg excuteApi(ApiDomain apiInfo)  {

		KfbApiDomain logParam = new KfbApiDomain();
        String token = kfbApiRepo.selectKfbApiKey(logParam);
        
		logParam.setToken(token);
		logParam.setUrl(apiInfo.getUrl());

        if("GET".equals(apiInfo.getMethod()))   logParam.setSendData(apiInfo.getParam());
        else                         			logParam.setSendData(apiInfo.getParamJson().toString());
		
		logParam.setSendUser(1);
        int apiKey = kfbApiRepo.insertNewKfbApiLog(logParam);
        logParam.setApiSeq(apiKey);
        
		OutApiConnector.setApi api = new OutApiConnector.setApi("", apiInfo.getApiName());
        api.url(apiInfo.getUrl());
        api.token(token);
        api.method(apiInfo.getMethod());
        api.clientId(ClientId);
        api.clientSecret(ClientSecret);

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
				data = new JSONObject(res.body());	
				
				if(data.getString("res_code").equals("200")) successCheck = "success";
				
	            resCode = data.getString("res_code");
	            resMsg = data.getString("res_msg");
			}else {
				data = new JSONObject(res.body());	
	            resCode = data.getString("res_code");
	            resMsg = data.getString("res_msg");
			}
			 
		} catch (JSONException e) {
			resExpMsg = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			resExpMsg = e.getMessage();
			e.printStackTrace();
		} finally {

			logParam.setResConCode(responseCode);
			logParam.setResCode(resCode);
			logParam.setResMsg(resMsg);
			logParam.setResExpMsg(resExpMsg);
			logParam.setResData(data.toString());
			kfbApiRepo.updateNewKfbApiLog(logParam);
		}
        
		return new ResponseMsg(HttpStatus.OK, successCheck, data, resMsg);
			
	}
		
}
