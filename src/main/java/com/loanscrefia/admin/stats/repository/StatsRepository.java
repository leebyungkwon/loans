package com.loanscrefia.admin.stats.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.stats.domain.StatsDomain;

@Mapper
public interface StatsRepository {

	//회사별 모집인
	List<StatsDomain> selectStatsResult1(StatsDomain statsDomain);
	
	//경력신규
	List<StatsDomain> selectStatsResult2(StatsDomain statsDomain);
	
	//경력 현황
	List<StatsDomain> selectStatsResult3(StatsDomain statsDomain);
	
	//부적격처리건수(모집인별)
	List<StatsDomain> selectStatsResult4(StatsDomain statsDomain);
	
	//부적격버튼 클릭 시 통계 데이터 insert or update
	int saveInaqInfoForStats(StatsDomain statsDomain);
	
	//등록처리현황(회사별)
	List<StatsDomain> selectStatsResult5(StatsDomain statsDomain);
	
	//등록처리현황(모집인별)
	List<StatsDomain> selectStatsResult6(StatsDomain statsDomain);
	
	//해지처리현황(회사별)
	List<StatsDomain> selectStatsResult7(StatsDomain statsDomain);
	
	//해지처리현황(모집인별)
	List<StatsDomain> selectStatsResult8(StatsDomain statsDomain);
	
	//불법모집 이력현황
	List<StatsDomain> selectStatsResult9(StatsDomain statsDomain);
}
