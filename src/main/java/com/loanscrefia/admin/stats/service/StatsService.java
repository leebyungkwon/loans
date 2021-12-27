package com.loanscrefia.admin.stats.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.stats.domain.StatsDomain;
import com.loanscrefia.admin.stats.repository.StatsRepository;

@Service
public class StatsService {

	@Autowired private StatsRepository statsRepository;
	
	//회사별 모집인
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult1(StatsDomain statsDomain){
		return statsRepository.selectStatsResult1(statsDomain);
	}
	
	//경력신규
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult2(StatsDomain statsDomain){
		return statsRepository.selectStatsResult2(statsDomain);
	}
	
	//경력 현황
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult3(StatsDomain statsDomain){
		return statsRepository.selectStatsResult3(statsDomain);
	}
	
	//부적격처리건수(모집인별)
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult4(StatsDomain statsDomain){
		return statsRepository.selectStatsResult4(statsDomain);
	}
	
	//부적격버튼 클릭 시 통계 데이터 insert or update
	@Transactional
	public int saveInaqInfoForStats(StatsDomain statsDomain){
		return statsRepository.saveInaqInfoForStats(statsDomain);
	}
	
	//등록처리현황(회사별)
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult5(StatsDomain statsDomain){
		return statsRepository.selectStatsResult5(statsDomain);
	}
	
	//등록처리현황(모집인별)
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult6(StatsDomain statsDomain){
		return statsRepository.selectStatsResult6(statsDomain);
	}
	
	//해지처리현황(회사별)
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult7(StatsDomain statsDomain){
		return statsRepository.selectStatsResult7(statsDomain);
	}
	
	//해지처리현황(모집인별)
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult8(StatsDomain statsDomain){
		return statsRepository.selectStatsResult8(statsDomain);
	}
	
	//불법모집 이력현황
	@Transactional(readOnly=true)
	public List<StatsDomain> selectStatsResult9(StatsDomain statsDomain){
		return statsRepository.selectStatsResult9(statsDomain);
	}
}
