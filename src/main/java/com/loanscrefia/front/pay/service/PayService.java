package com.loanscrefia.front.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.repository.PayRepository;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Service
public class PayService {

	@Autowired private PayRepository payRepo;
	@Autowired private SearchService searchService;
	
	//은행연합회
	@Autowired private KfbApiService kfbApiService;
	
	//결제정보 저장
	@Transactional
	public boolean insertPayResult(PayDomain payDomain) {
		
		boolean result = true;
		
		//(1)결제 테이블에 정보 저장
		int payResultInsertResult = payRepo.insertPayResult(payDomain);
		
		if(payResultInsertResult > 0) {
			//(2)은행연합회 통신(본동록) : 성공 시 모집인 상태(pl_reg_stat) = 자격취득 / 실패 시 결제완료(이후 프로세스는 협회쪽에서 수동으로 진행)
			
			
			
			//(3)모집인 상태(pl_reg_stat) 자격취득으로 수정
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
