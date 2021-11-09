package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;
import org.json.JSONObject;

import lombok.Data;

@Data
@Alias("api")
public class ApiDomain extends BaseDomain {
	
	private String apiName; 
	private String method;
	private String url;
	private String param;
	private JSONObject paramJson;
	
	
}
