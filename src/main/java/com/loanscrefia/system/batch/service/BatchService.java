package com.loanscrefia.system.batch.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.recruit.domain.NewRecruitDomain;
import com.loanscrefia.admin.recruit.repository.NewRecruitRepository;
import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.service.ApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.domain.BatchReqDomain;
import com.loanscrefia.system.batch.repository.BatchRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BatchService{
	
	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private ApiService apiService;
	@Autowired
	private NewRecruitRepository recruitRepository;

	@Transactional
	public int recruitReg(BatchDomain req) {
		ApiDomain param = new ApiDomain();
		int cnt = 0;
		//schedule 시작 이력저장
		try {
			//필요정보 호출하여 param 만들기
			
			
			//api 호출
			ResponseMsg result = apiService.excuteApi(param);
			
			
			//데이터 등록
			NewRecruitDomain recruitDomain = new NewRecruitDomain();
			recruitRepository.insertNewMasterStep(recruitDomain);
			if("success".equals(result.getCode())) cnt = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			//schedule 완료 이력저장
			return cnt;
		}
	}

	public List<BatchDomain> selectReqBatchList(BatchDomain param) {
		return batchRepository.selectReqBatchList(param);
	}
}
