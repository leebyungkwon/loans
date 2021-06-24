package com.loanscrefia.common.common.repository;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.common.domain.KfbApiDomain;

@Mapper
public interface KfbApiRepository {

	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//요청 이력 등록
	int insertKfbApiReqLog(KfbApiDomain kfbApiDomain);
	
	//응답 이력 등록
	int insertKfbApiResLog(KfbApiDomain kfbApiDomain);
	
	//은행연합회 api_key_update 배치
	void updateKfbApiKey(String key);
	
	//은행연합회 api_key 조회
	String selectKfbApiKey();
	
	
}
