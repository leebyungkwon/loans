package com.loanscrefia.system.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loanscrefia.system.batch.repository.BatchRepository;

import net.javacrumbs.shedlock.core.SchedulerLock;

@Component
@EnableScheduling
public class BatchController {

	@Autowired
	private BatchRepository batchRepository;
	
	/*
	
    @Scheduled(cron = "0/3 * * * * *")
    @SchedulerLock(name = "scheduledTaskName")
    public void excelUploadUserAndFileDelete() {
    	// 업로드 후 1개월동안 상태변경이 없는경우(승인전)
    	
    	// 1. 처리상태(plStat = 1) -> 1개월 초과시 삭제
    	
    	
        System.out.println("3초마다 돌았찌");
    }
    
    @Scheduled(cron = "0/5 * * * * *")
    public void ttt () {
    	System.out.println("5초마다 돌았찌");
    }
    
    */
	
}
