package com.loanscrefia.admin.corp.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.repository.CorpRepository;
import com.loanscrefia.config.message.ResponseMsg;

import sinsiway.CryptoUtil;

@Service
public class CorpService {

	@Autowired private CorpRepository corpRepo;
	
	//법인 리스트
	@Transactional(readOnly=true)
	public List<CorpDomain> selectCorpList(CorpDomain corpDomain) {
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(corpDomain.getPlMerchantNo())) {
			corpDomain.setPlMerchantNo(CryptoUtil.encrypt(corpDomain.getPlMerchantNo().replaceAll("-", "")));
		}
		
		//리스트
		List<CorpDomain> corp = corpRepo.selectCorpList(corpDomain);
		
		//리스트 데이터 복호화
		for(int i=0; i<corp.size(); i++) {
			String plMerchantNo = CryptoUtil.decrypt(corp.get(i).getPlMerchantNo());
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
			corp.get(i).setPlMerchantNo(plMerchantNo);
		}
		return corp;
	}
	
	//법인 저장
	@Transactional
	public ResponseMsg saveCorpInfo(CorpDomain corpDomain) {
		
		int result = 0;
		
		if(corpDomain.getCorpSeq() == null) {
			//저장
			result = corpRepo.insertCorpInfo(corpDomain);
		}else {
			//수정
			result = corpRepo.updateCorpInfo(corpDomain);
		}
		
		//결과
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//법인 저장 : 엑셀 업로드
	@Transactional
	public void insertCorpInfoByExcel(CorpDomain corpDomain) {
		
		List<Map<String, Object>> excelParam 	= corpDomain.getExcelParam();
		
		for(int i = 0;i < excelParam.size();i++) {
			CorpDomain chkParam = new CorpDomain();
			String itemA 		= (String)excelParam.get(i).get("A"); //법인명
			String itemC 		= (String)excelParam.get(i).get("C"); //법인번호(암호화된 상태)

			//중복체크
			chkParam.setPlMerchantNo(itemC);
			int chkResult = corpRepo.selectCorpInfoCnt(chkParam);
			
			//결과
			if(chkResult == 0) {
				chkParam.setPlMerchantName(itemA);
				chkParam.setPathTyp("1");
				corpRepo.insertCorpInfo(chkParam);
		 	}
		}
	}
	
	//법인 상세
	@Transactional(readOnly=true)
	public CorpDomain getCorpInfo(CorpDomain corpDomain) {

		CorpDomain result = corpRepo.getCorpInfo(corpDomain);
		String dnc = CryptoUtil.decrypt(result.getPlMerchantNo());
		result.setPlMerchantNo(dnc);
		
		return corpRepo.getCorpInfo(corpDomain);
	}

	// 법인등록번호 중복체크
	@Transactional(readOnly = true)
	public int plMerchantNoCheck(CorpDomain corpDomain) {
		return corpRepo.plMerchantNoCheck(corpDomain);
	}
	
}
