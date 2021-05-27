package com.loanscrefia.front.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.repository.PayRepository;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Service
public class PayService {

	@Autowired private PayRepository payRepo;
	@Autowired private SearchService searchService;
	
	//결제정보 저장
	@Transactional
	public boolean insertPayResult(PayDomain payDomain) {
		
		boolean result = true;
		
		//(1)결제 테이블에 정보 저장
		int payResultInsertResult = payRepo.insertPayResult(payDomain);
		
		if(payResultInsertResult > 0) {
			//(2)모집인 상태(pl_reg_stat) 자격취득으로 수정
			SearchDomain param = new SearchDomain();
			param.setMasterSeq(payDomain.getMasterSeq());
			int updateResult = searchService.updatePlRegStat(param);
			
			if(updateResult == 0) {
				result = false;
			}else {
				//(2)모집인 단계 이력 저장
				searchService.insertSearchUserStepHistory(param);
			}
		}else {
			result = false;
		}
		return result;
	}
}
