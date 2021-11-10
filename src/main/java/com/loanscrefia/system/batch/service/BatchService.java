package com.loanscrefia.system.batch.service;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.NewApplyDomain;
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
	
	
	
	
	
	
	
	
	// 2021-11-09 스케줄명으로 조회
	public List<BatchDomain> selectBatchList(BatchDomain param) {
		return batchRepository.selectBatchList(param);
	}
	
	
	
	// 2021-11-10 배치 시작 이력 저장
	public int insertScheduleHist(BatchDomain param) {
		return batchRepository.insertScheduleHist(param);
	}
	
	// 2021-11-10 배치 종료 이력 저장
	public void updateScheduleHist(BatchDomain param) {
		batchRepository.updateScheduleHist(param);
	}
	
	
	// 2021-11-09 가등록 - 개인
	@Transactional
	public int preloanReg(BatchDomain req) {
		ApiDomain param = new ApiDomain();
		JSONObject jsonParam = new JSONObject(req.getParam());
		param.setParamJson(jsonParam);
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = Integer.parseInt(jsonParam.getString("master_seq").toString());
		}
		int cnt = 0;
		try {
			ResponseMsg result = apiService.excuteApi(param);
			if("success".equals(result.getCode())) {
				String preLcNum ="";
				JSONObject responseJson = new JSONObject(result.getData().toString());
				if(!responseJson.isNull("pre_lc_num")) {
					preLcNum = responseJson.getString("pre_lc_num");
				}
				
				// 가등록번호 update
				NewApplyDomain newApplyDomain = new NewApplyDomain();
				newApplyDomain.setPreLcNum(preLcNum);
				newApplyDomain.setMasterSeq(masterSeq);
				batchRepository.updatePreLcNum(newApplyDomain);
				req.setStatus("2");
				cnt = 1;
				
			}else {
				req.setStatus("3");
				req.setError(result.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			req.setError(e.getMessage());
		} finally {
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	// 2021-11-10 가등록 - 법인
	@Transactional
	public int preloanCorpReg(BatchDomain req) {
		ApiDomain param = new ApiDomain();
		JSONObject jsonParam = new JSONObject(req.getParam());
		param.setParamJson(jsonParam);
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = Integer.parseInt(jsonParam.getString("master_seq").toString());
		}
		int cnt = 0;
		try {
			ResponseMsg result = apiService.excuteApi(param);
			if("success".equals(result.getCode())) {
				String preLcNum ="";
				JSONObject responseJson = new JSONObject(result.getData().toString());
				if(!responseJson.isNull("pre_corp_lc_num")) {
					preLcNum = responseJson.getString("pre_corp_lc_num");
				}
				
				// 가등록번호 update
				NewApplyDomain newApplyDomain = new NewApplyDomain();
				newApplyDomain.setPreLcNum(preLcNum);
				newApplyDomain.setMasterSeq(masterSeq);
				batchRepository.updatePreLcNum(newApplyDomain);
				req.setStatus("2");
				cnt = 1;
			}else {
				req.setStatus("3");
				req.setError(result.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			req.setError(e.getMessage());
		} finally {
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
}
