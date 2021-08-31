package com.loanscrefia.front.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.domain.SearchResultDomain;
import com.loanscrefia.front.search.repository.SearchRepository;
import com.loanscrefia.member.user.domain.UserDomain;

import sinsiway.CryptoUtil;

@Service
public class SearchService {

	@Autowired private SearchRepository searchRepo;
	
	@Autowired 
	private KfbApiService kfbApiService;
	
	@Autowired
	private KfbApiRepository kfbApiRepository;
	
	//모집인 조회 : 개인(결제)
	public ResponseMsg selectPayIndvUserInfo(SearchDomain searchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다[0].");
		
		//검색어
		String plMZIdFront 	= searchDomain.getPlMZIdFront();
		String gender 		= searchDomain.getGender();
		
		//조회 결과
		searchDomain.setPlCellphone(searchDomain.getPlCellphone().replaceAll("-", ""));
		
		// 내부 테이블 조회 - 결제 대상자 조회
		SearchDomain result = searchRepo.selectPayIndvUserInfo(searchDomain);
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		
		// 내부 테이블 조회 - 자격취득된 건 조회
		SearchDomain payResult = searchRepo.selectPayResultIndvUserInfo(searchDomain);
		if(payResult != null) {
			// 이미 자격취득한 건 외에 다른건들도 자격취득 및 기등록여부(수수료기납부여부) 변경
			List<SearchDomain> payResultList = searchRepo.selectPayResultIndvUserList(searchDomain);
			if(payResultList.size() > 0) {
				for(SearchDomain tmp : payResultList) {
					// hist 쌓기
					SearchDomain updateDomain = new SearchDomain();
					updateDomain.setMasterSeq(tmp.getMasterSeq());
					searchRepo.insertSearchUserHistory(updateDomain);
					searchRepo.updatePreRegYn(updateDomain);
				}
			}
			
			// hist, step 이력 쌓기
			searchDomain.setMasterSeq(0);
			searchDomain.setPlClass("1");
			searchRepo.insertSearchUserStepHistory(searchDomain);
			searchRepo.updatePayResultStat(searchDomain);
			
			return new ResponseMsg(HttpStatus.OK, "fail", "이미 자격취득(결제완료)한 대상입니다. 등록번호를 확인해 주세요.");
		}
		

		// 2. 은행연합회 API 기등록여부(수수료납부여부 확인)
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		indvParam.put("pre_lc_num", result.getPreLcNum());
		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "GET", "1", "Y");
		if("success".equals(responseMsg.getCode())) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			// 기등록여부 Y / N으로 분기처리
			if(!responseJson.isNull("fee_yn")) {
				if("Y".equals(responseJson.getString("fee_yn"))) {
					
					// 내부 테이블 조회 - 승인전 조회 데이터 기등록여부값만 변경
					List<SearchDomain> payResultList = searchRepo.selectPayResultIndvUserList(searchDomain);
					if(payResultList.size() > 0) {
						for(SearchDomain tmp : payResultList) {
							// hist 쌓기
							SearchDomain updateDomain = new SearchDomain();
							updateDomain.setMasterSeq(tmp.getMasterSeq());
							searchRepo.insertSearchUserHistory(updateDomain);
							searchRepo.updatePreRegYn(updateDomain);
						}
					}
					
					// hist, step 이력 쌓기
					searchDomain.setMasterSeq(0);
					searchDomain.setPlClass("1");
					searchRepo.insertSearchUserStepHistory(searchDomain);
					searchRepo.updatePayResultStat(searchDomain);
				}
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다[20].");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다[30].");
		}

		//데이터 복호화해서 검색어와 비교
		String rPlMZId 		= CryptoUtil.decrypt(result.getPlMZId());
		String rPlMZIdFront = rPlMZId.substring(0, 6);
		String rPlMZIdEnd	= rPlMZId.substring(6);
		String rGender 		= "";
		
		if(rPlMZIdEnd.startsWith("1") || rPlMZIdEnd.startsWith("3") || rPlMZIdEnd.startsWith("5") || rPlMZIdEnd.startsWith("7")) {
			rGender = "M";
		}else {
			rGender = "F";
		}
		
		if(plMZIdFront.equals(rPlMZIdFront) && gender.equals(rGender)) {
			return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다[40].");
	}
	
	//모집인 조회 : 법인(결제)
	public ResponseMsg selectPayCorpUserInfo(SearchDomain searchDomain) {
		
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다[0].");
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(searchDomain.getPlMerchantNo())) {
			searchDomain.setPlMerchantNo(CryptoUtil.encrypt(searchDomain.getPlMerchantNo()));
		}
		
		// 내부 테이블 조회 - 결제 대상자 조회
		SearchDomain result = searchRepo.selectPayCorpUserInfo(searchDomain); 
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		
		// 내부 테이블 조회 - 자격취득된 건 조회
		SearchDomain payResult = searchRepo.selectPayResultCorpUserInfo(searchDomain); 
		if(payResult != null) {
			// 내부 테이블 조회 - 승인전 조회 데이터 기등록여부값만 변경
			List<SearchDomain> payResultList = searchRepo.selectPayCorpUserList(searchDomain);
			if(payResultList.size() > 0) {
				for(SearchDomain tmp : payResultList) {
					// hist 쌓기
					SearchDomain updateDomain = new SearchDomain();
					updateDomain.setMasterSeq(tmp.getMasterSeq());
					searchRepo.insertSearchUserHistory(updateDomain);
					searchRepo.updatePreRegYn(updateDomain);
				}
			}
			
			// hist, step 이력 쌓기
			searchDomain.setMasterSeq(0);
			searchDomain.setPlClass("2");
			searchRepo.insertSearchUserStepHistory(searchDomain);
			searchRepo.updatePayResultStat(searchDomain);
			return new ResponseMsg(HttpStatus.OK, "fail", "이미 자격취득(결제완료)한 대상입니다. 등록번호를 확인해 주세요.");
		}
		
		// 2. 은행연합회 API 기등록여부(수수료납부여부 확인)
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		indvParam.put("pre_corp_lc_num", result.getPreLcNum());
		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.PreLoanCorpUrl, "GET", "2", "Y");
		if("success".equals(responseMsg.getCode())) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			// 기등록여부 Y / N으로 분기처리
			if(!responseJson.isNull("fee_yn")) {
				if("Y".equals(responseJson.getString("fee_yn"))) {
					
					// 내부 테이블 조회 - 승인전 조회 데이터 기등록여부값만 변경
					List<SearchDomain> payResultList = searchRepo.selectPayCorpUserList(searchDomain);
					if(payResultList.size() > 0) {
						for(SearchDomain tmp : payResultList) {
							// hist 쌓기
							SearchDomain updateDomain = new SearchDomain();
							updateDomain.setMasterSeq(tmp.getMasterSeq());
							searchRepo.insertSearchUserHistory(updateDomain);
							searchRepo.updatePreRegYn(updateDomain);
						}
					}
					
					// hist, step 이력 쌓기
					searchDomain.setMasterSeq(0);
					searchDomain.setPlClass("2");
					searchRepo.insertSearchUserStepHistory(searchDomain);
					searchRepo.updatePayResultStat(searchDomain);
				}
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다[20].");
			}
		}else {
			// 이전 검증에서 통과 후				
			// 서버통신오류
		}
		
		return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
	}

	//모집인 조회 : 개인
	public ResponseMsg selectIndvUserInfo(SearchDomain searchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		// 2021-07-10 은행연합회 API 통신 - 개인조회
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		indvParam.put("lc_num", searchDomain.getPlRegistNo());
		// 은행연합회 API 개인조회 시작
		
		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "GET", "1", "N");
		if("success".equals(responseMsg.getCode())) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			if(!responseJson.isNull("name")) {
				kfbApiDomain.setResData(searchDomain.getPlRegistNo());
				return new ResponseMsg(HttpStatus.OK, null, kfbApiDomain, "success");
			}else {
				return new ResponseMsg(HttpStatus.OK, null, responseMsg, "fail");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, null, responseMsg, "fail");
		}


	}
	
	//모집인 조회 : 법인
	public ResponseMsg selectCorpUserInfo(SearchDomain searchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		// 2021-07-10 은행연합회 API 통신 - 법인조회
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		indvParam.put("corp_lc_num", searchDomain.getPlRegistNo());
		// 은행연합회 API 개인조회 시작
		
		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "GET", "2", "N");
		if("success".equals(responseMsg.getCode())) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			if(!responseJson.isNull("corp_num")) {
				kfbApiDomain.setResData(searchDomain.getPlRegistNo());
				return new ResponseMsg(HttpStatus.OK, null, kfbApiDomain, "success");
			}else {
				return new ResponseMsg(HttpStatus.OK, null, responseMsg, "fail");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, null, responseMsg, "fail");
		}
	}
	
	//모집인 조회 결과
	public Map<String,Object> userSearchResult(SearchDomain searchDomain) {
		
		Map<String, Object> result 				= new HashMap<String, Object>();
		
		//모집인 상세
		searchDomain.setPlRegStat("3"); //모집인 상태가 자격취득인 것
		SearchDomain searchUserInfo 			= this.selectSearchUserInfo(searchDomain);
		
		//모집인 이력 : 데이터 논의중
		
		
		//모집인 위반이력
		List<SearchDomain> violationInfoList 	= searchRepo.selectSearchUserViolationInfoList(searchDomain);
		
		//전달
		result.put("searchUserInfo", searchUserInfo);
		//result.put("searchUserHistInfo", searchUserHistInfo);
		result.put("violationInfoList", violationInfoList);
		
		return result;
	}
	
	//모집인 상세
	public SearchDomain selectSearchUserInfo(SearchDomain searchDomain) {
		
		SearchDomain result = searchRepo.selectSearchUserInfo(searchDomain);
		
		if(StringUtils.isNotEmpty(result.getPlMZId())) {
			//주민번호
			String plMZId 			= CryptoUtil.decrypt(result.getPlMZId());
			String plMZIdFront		= plMZId.substring(0, 6);
			String plMZIdEnd		= plMZId.substring(6);
			plMZId 					= plMZIdFront + "-" + plMZIdEnd;
			result.setPlMZId(plMZId);
			
			//생년월일
			result.setPlMZIdFront(plMZIdFront);
			
			//성별
			if(plMZIdEnd.startsWith("1") || plMZIdEnd.startsWith("3") || plMZIdEnd.startsWith("5") || plMZIdEnd.startsWith("7")) {
				result.setGender("M");
			}else {
				result.setGender("F");
			}
		}
		if(StringUtils.isNotEmpty(result.getPlMerchantNo())) {
			//법인번호
			String plMerchantNo 	= CryptoUtil.decrypt(result.getPlMerchantNo());
			plMerchantNo 			= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			result.setPlMerchantNo(plMerchantNo);
		}
		
		return result;
	}
	
	//모집인 상태 변경
	@Transactional
	public int updatePlRegStat(SearchDomain searchDomain) {
		//기본이력저장
		searchRepo.insertSearchUserHistory(searchDomain);
		
		int updateResult = searchRepo.updatePlRegStat(searchDomain);
		
		if(updateResult > 0) {
			this.insertSearchUserStepHistory(searchDomain);
		}
		
		return updateResult;
	}
	
	//모집인 정보 단계별(STATUS) 이력 저장
	@Transactional
	public int insertSearchUserStepHistory(SearchDomain searchDomain) {
		return searchRepo.insertSearchUserStepHistory(searchDomain);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
