package com.loanscrefia.common.main.repository;


import org.apache.ibatis.annotations.Mapper;
import com.loanscrefia.common.main.domain.MainDomain;

@Mapper
public interface MainRepository {
	
	MainDomain selectDashBoard(MainDomain mainDomain);
}
