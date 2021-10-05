package com.loanscrefia.front.search.service;

import java.io.IOException;
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

import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.search.domain.SearchDomain;
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
	public ResponseMsg selectPayIndvUserInfo(SearchDomain searchDomain) throws IOException {
		
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
		
		if(!"1".equals(result.getPlClass())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "데이터 오류가 발생하였습니다.");
		}
		
		// 은행연합회 API 기등록여부(수수료납부여부 확인)
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		indvParam.put("pre_lc_num", result.getPreLcNum());
		ResponseMsg responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "GET", "1", "Y");
		if("success".equals(responseMsg.getCode())) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			// 기등록여부 Y / N으로 분기처리
			if(!responseJson.isNull("fee_yn")) {
				if("Y".equals(responseJson.getString("fee_yn"))) {
					// 내부 테이블 조회 - 승인전 조회 데이터 기등록여부값만 변경
					List<SearchDomain> payResultList = searchRepo.selectPayResultIndvUserList(searchDomain);
					if(payResultList.size() > 0) {
						for(SearchDomain tmp : payResultList) {
							SearchDomain updateDomain = new SearchDomain();
							updateDomain.setMasterSeq(tmp.getMasterSeq());
							updateDomain.setPlClass("1");
							searchRepo.insertSearchUserHistory(updateDomain);
							searchRepo.updatePreRegYn(updateDomain);
						}
					}
					
					// 내부 테이블 조회 - 승인완료 데이터 본등록 API
					List<SearchDomain> payApiResultList = searchRepo.selectApiResultIndvUserList(searchDomain);
					if(payApiResultList.size() > 0) {
						for(SearchDomain apiTmp : payApiResultList) {
							// 금융상품 3, 6번 제외
							String prdCheck = apiTmp.getPlProduct();
							String lcNum = "";
							String conNum = "";
							if("01".equals(prdCheck) || "05".equals(prdCheck)) {
								JSONObject jsonParam = new JSONObject();
								String plClass = apiTmp.getPlClass();
								jsonParam.put("pre_lc_num", apiTmp.getPreLcNum());
								ResponseMsg apiResponseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", plClass, "Y");
								if("success".equals(apiResponseMsg.getCode())) {
									JSONObject apiResponseJson = new JSONObject(apiResponseMsg.getData().toString());
									lcNum = apiResponseJson.getString("lc_num");
									JSONObject jsonObj = new JSONObject();
									JSONArray conArr = apiResponseJson.getJSONArray("con_arr");
									// 계약금융기관코드(저장되어있는 데이터 비교)
									String comCode = Integer.toString(apiTmp.getComCode());
									for(int i=0; i<conArr.length(); i++){
										jsonObj = conArr.getJSONObject(i);
										String loanType = jsonObj.getString("loan_type");
										String finCode = jsonObj.getString("fin_code");
										// 등록시 계약금융기관코드 및 대출모집인 유형코드(상품코드)가 동일한 정보만 저장(계약일, 대출모집인휴대폰번호 등등 추가가능)
										if(loanType.equals(prdCheck) && finCode.equals(comCode)) {
											conNum = jsonObj.getString("con_num");
											break;
										}
									}
									
									UserDomain updateApiDomain = new UserDomain();
									updateApiDomain.setMasterSeq(apiTmp.getMasterSeq());
									updateApiDomain.setPlRegistNo(lcNum);
									updateApiDomain.setConNum(conNum);
									updateApiDomain.setPlRegStat("3");
									
									kfbApiRepository.updateKfbApiByUserInfo(updateApiDomain);
									
									// STEP 이력저장
									SearchDomain sDomain = new SearchDomain();
									sDomain.setMasterSeq(0);
									sDomain.setPlClass("1");
									sDomain.setPlCellphone(searchDomain.getPlCellphone());
									sDomain.setPlMName(searchDomain.getPlMName());
									searchRepo.insertSearchUserStepHistory(sDomain);
									
								}
							}
						}
						return new ResponseMsg(HttpStatus.OK, "fail", "이미 결제한 정보가 존재합니다.\n자격취득여부를 확인해 주세요.");
					}
				}
			}
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
		return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
	}
	
	//모집인 조회 : 법인(결제)
	public ResponseMsg selectPayCorpUserInfo(SearchDomain searchDomain) throws IOException {
		//검색어 암호화
		if(StringUtils.isNotEmpty(searchDomain.getPlMerchantNo())) {
			searchDomain.setPlMerchantNo(CryptoUtil.encrypt(searchDomain.getPlMerchantNo()));
		}
		// 내부 테이블 조회 - 결제 대상자 조회
		SearchDomain result = searchRepo.selectPayCorpUserInfo(searchDomain); 
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		
		if(!"2".equals(result.getPlClass())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "데이터 오류가 발생하였습니다.");
		}		
		
		// 은행연합회 API 기등록여부(수수료납부여부 확인)
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject corpParam = new JSONObject();
		corpParam.put("pre_corp_lc_num", result.getPreLcNum());
		ResponseMsg responseMsg = kfbApiService.commonKfbApi(apiKey, corpParam, KfbApiService.ApiDomain+KfbApiService.PreLoanCorpUrl, "GET", "2", "Y");
		
		if("success".equals(responseMsg.getCode())) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			// 기등록여부 Y / N으로 분기처리
			if(!responseJson.isNull("fee_yn")) {
				if("Y".equals(responseJson.getString("fee_yn"))) {
					// 내부 테이블 조회 - 승인전 조회 데이터 기등록여부값만 변경
					List<SearchDomain> payResultList = searchRepo.selectPayCorpUserList(searchDomain);
					if(payResultList.size() > 0) {
						for(SearchDomain tmp : payResultList) {
							SearchDomain updateDomain = new SearchDomain();
							updateDomain.setMasterSeq(tmp.getMasterSeq());
							updateDomain.setPlClass("2");
							searchRepo.insertSearchUserHistory(updateDomain);
							searchRepo.updatePreRegYn(updateDomain);
						}
					}
					
					// 내부 테이블 조회 - 승인완료 데이터 본등록 API
					List<SearchDomain> payApiResultList = searchRepo.selectApiResultCorpList(searchDomain);
					if(payApiResultList.size() > 0) {
						for(SearchDomain apiTmp : payApiResultList) {
							
							// 금융상품 3, 6번 제외
							String prdCheck = apiTmp.getPlProduct();
							String lcNum = "";
							String conNum = "";
							if("01".equals(prdCheck) || "05".equals(prdCheck)) {
								JSONObject jsonParam = new JSONObject();
								String plClass = apiTmp.getPlClass();
								jsonParam.put("pre_corp_lc_num", apiTmp.getPreLcNum());
								ResponseMsg apiResponseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", plClass, "Y");
								if("success".equals(apiResponseMsg.getCode())) {
									JSONObject apiResponseJson = new JSONObject(apiResponseMsg.getData().toString());
									lcNum = apiResponseJson.getString("corp_lc_num");
									JSONObject jsonObj = new JSONObject();
									JSONArray conArr = apiResponseJson.getJSONArray("con_arr");
									// 계약금융기관코드(저장되어있는 데이터 비교)
									String comCode = Integer.toString(apiTmp.getComCode());
									for(int i=0; i<conArr.length(); i++){
										jsonObj = conArr.getJSONObject(i);
										String loanType = jsonObj.getString("loan_type");
										String finCode = jsonObj.getString("fin_code");
										// 등록시 계약금융기관코드 및 대출모집인 유형코드(상품코드)가 동일한 정보만 저장(계약일, 대출모집인휴대폰번호 등등 추가가능)
										if(loanType.equals(prdCheck) && finCode.equals(comCode)) {
											conNum = jsonObj.getString("con_num");
											break;
										}
									}
									
									UserDomain updateApiDomain = new UserDomain();
									updateApiDomain.setMasterSeq(apiTmp.getMasterSeq());
									updateApiDomain.setPlRegistNo(lcNum);
									updateApiDomain.setConNum(conNum);
									updateApiDomain.setPlRegStat("3");
									kfbApiRepository.updateKfbApiByUserInfo(updateApiDomain);
									
									// STEP 이력저장
									SearchDomain sDomain = new SearchDomain();
									sDomain.setMasterSeq(0);
									sDomain.setPlClass("2");
									sDomain.setPlMerchantNo(searchDomain.getPlMerchantNo());
									sDomain.setPlCeoName(searchDomain.getPlCeoName());
									searchRepo.insertSearchUserStepHistory(sDomain);
								}
							}
						}
						return new ResponseMsg(HttpStatus.OK, "fail", "이미 결제한 정보가 존재합니다.\n자격취득여부를 확인해 주세요.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
	}

	//모집인 조회 : 개인
	public ResponseMsg selectIndvUserInfo(SearchDomain searchDomain) throws IOException {
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
	public ResponseMsg selectCorpUserInfo(SearchDomain searchDomain) throws IOException {
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
	
	
	
	
	
	

	
	//2021-09-03 모집인결제조회 : 개인
	public ResponseMsg getPayResultIndvSearch(SearchDomain searchDomain) {
		
		//검색어
		String plMZIdFront 	= searchDomain.getPlMZIdFront();
		String gender 		= searchDomain.getGender();
		
		//조회 결과
		searchDomain.setPlCellphone(searchDomain.getPlCellphone().replaceAll("-", ""));
		
		SearchDomain result = searchRepo.getPayResultIndvSearch(searchDomain);
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
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
	
	
	//2021-09-03 모집인결제조회 : 법인
	public ResponseMsg getPayResultCorpSearch(SearchDomain searchDomain) {
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(searchDomain.getPlMerchantNo())) {
			searchDomain.setPlMerchantNo(CryptoUtil.encrypt(searchDomain.getPlMerchantNo()));
		}
		
		SearchDomain result = searchRepo.getPayResultCorpSearch(searchDomain); 
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		
		return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
	}

	
	//모집인 상세
	public SearchDomain getPayResultSearchResult(SearchDomain searchDomain) {
		SearchDomain result = searchRepo.getPayResultSearchResult(searchDomain);
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
	
	//결제완료 후 승인상태가 승인완료전인 리스트 - 개인
	@Transactional
	public List<SearchDomain> selectPrevIndvPaySearchResult(SearchDomain searchDomain) {
		return searchRepo.selectPrevIndvPaySearchResult(searchDomain);
	}
	
	//결제완료 후 승인상태가 승인완료전인 리스트 - 법인
	@Transactional
	public List<SearchDomain> selectPrevCorpPaySearchResult(SearchDomain searchDomain) {
		return searchRepo.selectPrevCorpPaySearchResult(searchDomain);
	}
	
	
	
	//결제완료 후 승인상태가 승인완료인 리스트 - 개인
	@Transactional
	public List<SearchDomain> selectIndvPaySearchResult(SearchDomain searchDomain) {
		return searchRepo.selectIndvPaySearchResult(searchDomain);
	}
	
	//결제완료 후 승인상태가 승인완료인 리스트 - 법인
	@Transactional
	public List<SearchDomain> selectCorpPaySearchResult(SearchDomain searchDomain) {
		return searchRepo.selectCorpPaySearchResult(searchDomain);
	}
	
	
}
