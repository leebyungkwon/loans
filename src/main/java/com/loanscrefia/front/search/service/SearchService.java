package com.loanscrefia.front.search.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.repository.SearchRepository;
import com.loanscrefia.member.user.domain.UserDomain;

import sinsiway.CryptoUtil;

@Service
public class SearchService {

	@Autowired private SearchRepository searchRepo;
	
	//모집인 조회 : 개인(결제)
	@Transactional(readOnly = true)
	public ResponseMsg selectPayIndvUserInfo(SearchDomain searchDomain) {
		
		//검색어
		String plMZIdFront 	= searchDomain.getPlMZIdFront();
		String gender 		= searchDomain.getGender();
		
		//조회 결과
		searchDomain.setPlCellphone(searchDomain.getPlCellphone().replaceAll("-", ""));
		SearchDomain result = searchRepo.selectPayIndvUserInfo(searchDomain);
		
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		
		//데이터 복호화해서 검색어와 비교
		String rPlMZId 		= CryptoUtil.decrypt(result.getPlMZId());
		String rPlMZIdFront = rPlMZId.substring(0, 6);
		String rPlMZIdEnd	= rPlMZId.substring(6);
		String rGender 		= "";
		
		if(rPlMZIdEnd.startsWith("1") || rPlMZIdEnd.startsWith("3")) {
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
	@Transactional(readOnly = true)
	public ResponseMsg selectPayCorpUserInfo(SearchDomain searchDomain) {
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(searchDomain.getPlMerchantNo())) {
			searchDomain.setPlMerchantNo(CryptoUtil.encrypt(searchDomain.getPlMerchantNo().replaceAll("-", "")));
		}
		
		//조회 결과
		SearchDomain result = searchRepo.selectPayCorpUserInfo(searchDomain); 
		
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
	}

	//모집인 조회 : 개인
	@Transactional(readOnly = true)
	public ResponseMsg selectIndvUserInfo(SearchDomain searchDomain) {
		
		//조회 결과
		searchDomain.setPlCellphone(searchDomain.getPlCellphone().replaceAll("-", ""));
		SearchDomain result = searchRepo.selectIndvUserInfo(searchDomain);
		
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
	}
	
	//모집인 조회 : 법인
	@Transactional(readOnly = true)
	public ResponseMsg selectCorpUserInfo(SearchDomain searchDomain) {
		
		//조회 결과
		SearchDomain result = searchRepo.selectCorpUserInfo(searchDomain);
		
		if(result == null) {
			return new ResponseMsg(HttpStatus.OK, "fail", "조회된 결과가 없습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, null, result.getMasterSeq(), "");
	}
	
	//모집인 조회 결과
	@Transactional(readOnly=true)
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
	@Transactional(readOnly=true)
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
			if(plMZId.substring(7).startsWith("1") || plMZId.substring(7).startsWith("3")) {
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
