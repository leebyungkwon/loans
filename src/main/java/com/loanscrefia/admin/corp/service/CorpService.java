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
			String plMerchantNo = CryptoUtil.decrypt(corp.get(i).getPlMerchantNo());
			plMerchantNo = plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
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
		List<CorpDomain> corpList 				= this.selectCorpList(corpDomain);
		
		if(corpList.size() > 0) {
			for(int i = 0;i < excelParam.size();i++) {
				String plMerchantNo = CryptoUtil.decrypt((String)excelParam.get(i).get("C"));
				
				for(int j = 0;j < corpList.size();j++) {
					//중복제거
					if(plMerchantNo.equals(corpList.get(j).getPlMerchantNo())) {
						excelParam.remove(i);
					}
				}
			}
		}
		//등록
		if(excelParam.size() > 0) {
			CorpDomain param = new CorpDomain();
			param.setExcelParam(excelParam);
			corpRepo.insertCorpInfo(param);
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
