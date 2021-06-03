package com.loanscrefia.admin.corp.service;

import java.util.List;
import java.util.Map;

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
		
		List<CorpDomain> corp = corpRepo.selectCorpList(corpDomain);
		for(int i=0; i<corp.size(); i++) {
			String dnc = CryptoUtil.decrypt(corp.get(i).getPlMerchantNo());
			corp.get(i).setPlMerchantNo(dnc);
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
		
		List<Map<String, Object>> excelParam = corpDomain.getExcelParam();
		
		for(int i = 0;i < excelParam.size();i++) {
			CorpDomain chkParam = new CorpDomain();
			chkParam.setPlMerchantName((String)excelParam.get(i).get("A"));
			chkParam.setPlMerchantNo((String)excelParam.get(i).get("C"));
			
			//중복체크
			int chkResult = corpRepo.selectCorpInfoCnt(chkParam);
			
			//결과
			if(chkResult == 0) {
				
				// 2021-05-31 법인번호 암호화 진행예정
				// insert 및 update쿼리 -> REPLACE함수 제거 -> java에서 replace 제거 후 진행
				String encMerchantNo = CryptoUtil.encrypt(corpDomain.getPlMerchantNo()); // 암호화
				chkParam.setPlMerchantNo(encMerchantNo);
				
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
	public int plMerchantNoCheck(CorpDomain corpDomain) {
		return corpRepo.plMerchantNoCheck(corpDomain);
	}
	
}
