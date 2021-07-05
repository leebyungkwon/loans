package com.loanscrefia.common.common.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.member.user.domain.UserDomain;

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
	
	//은행연합회 토큰 저장
	void insertKfbApiKey(KfbApiDomain kfbApiDomain);
	
	//은행연합회 토큰 조회
	String selectKfbApiKey();
	
	//회원정보 수정 - 은행연합회에서 전달받은 등록번호 update
	int updateKfbApiByUserInfo(UserDomain userDomain);
	
	
	
	// API리스트 조회
	List<KfbApiDomain> selectApiList(KfbApiDomain kfbApiDomain);
	
	
}
