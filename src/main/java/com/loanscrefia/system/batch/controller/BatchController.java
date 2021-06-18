package com.loanscrefia.system.batch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.service.UserService;
import com.loanscrefia.system.batch.repository.BatchRepository;

import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.core.SchedulerLock;

@Log4j2
@Component
@EnableScheduling
public class BatchController {

	@Autowired private BatchRepository batchRepository;
	@Autowired private UserService userService;
	
	/* -------------------------------------------------------------------------------------------
	 * cron =  0~59(초) 0~59(분) 0~23(시) 1~31(일) 1~12(월) 0~6(요일) 생략가능(연도)
	 * ex)매일 오후 2시 5분에 실행 : 0 5 14 * * *
	 * -------------------------------------------------------------------------------------------
	 */
	
	/*
	
	//모집인 엑셀 업로드 후 1개월동안 처리상태가 미요청 + 모집인상태가 승인전인 경우 -> 모집인 데이터 및 모집인 관련 첨부파일 삭제(+서버)
	@Scheduled(cron = "0/3 * * * * *")
	@SchedulerLock(name = "scheduledTaskName")
	public void excelUploadUserAndFileDelete() {
		System.out.println("3초마다 돌았찌");
		log.info("================ excelUploadUserAndFileDelete() START ================");
		//(1)모집인 엑셀 업로드 후 1개월동안 처리상태가 미요청인 리스트
    	List<UserDomain> list = batchRepository.selectExcelUploadUserAndFileDelete();
    	
    	//(2)모집인 데이터 삭제 + 모집인 관련 첨부파일 삭제(+서버)
    	if(list.size() > 0) {
    		log.info("list.size() :: " + list.size());
    		for(int i = 0;i < list.size();i++) {
    			log.info("masterSeq :: " + list.get(i).getMasterSeq() + ", fileGrpSeq :: " + list.get(i).getFileSeq());
    			UserDomain userDomain = new UserDomain();
    			userDomain.setMasterSeq(list.get(i).getMasterSeq());
    			userService.deleteUserRegInfo(userDomain);
    		}
    	}
    	log.info("================ excelUploadUserAndFileDelete() END ================");
    }
    
	//모집인 엑셀 등록 파일 삭제
    @Scheduled(cron = "0/5 * * * * *")
    @SchedulerLock(name = "scheduledTaskName")
    public void userUploadExcelFileDelete() {
    	System.out.println("5초마다 돌았찌");
    	log.info("================ userUploadExcelFileDelete() START ================");
    	
    	
    	
    	
    	log.info("================ userUploadExcelFileDelete() END ================");
    }
    
    */
    
	
}
