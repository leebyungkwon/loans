package com.loanscrefia.system.batch.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.domain.BatchReqDomain;
import com.loanscrefia.system.batch.service.BatchService;
import com.loanscrefia.util.OutApiConnector;
import com.loanscrefia.util.OutApiParse;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import okhttp3.Response;

@Slf4j
@Component
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class BatchController {

	@Autowired 
	private BatchService batchService;
	
	@Autowired
	private KfbApiService kfbApiService;
	
	private static final String ONE_MIN = "PT30S"; // 1분동안 Lock
	
	/* -------------------------------------------------------------------------------------------
	 * cron =  0~59(초) 0~59(분) 0~23(시) 1~31(일) 1~12(월) 0~6(요일) 생략가능(연도)
	 * ex)매일 오후 2시 5분에 실행 : 0 5 14 * * *
	 * -------------------------------------------------------------------------------------------
	 */
	
	
	@Scheduled(cron="* 0/30 * * * *") 
    @SchedulerLock(name="recruitReg",lockAtMostForString = "PT30S", lockAtLeastForString = "PT30S")
    public void SchedulerTest() {
		
		BatchDomain batch = new BatchDomain();
		batch.setScheduleName("recruitReg");
		
		List<BatchDomain> recruitRegList = batchService.selectReqBatchList(batch);
		int reqCnt = recruitRegList.size();
		batch.setReqCnt(reqCnt);
		
		int successCnt = 0;
		
		//schedule_hist 시작 이력 저장
		//batchService.insertScheduleHist
		
		for(BatchDomain reg : recruitRegList) {
			int success = batchService.recruitReg(reg);
			successCnt = successCnt + success;
		}
		batch.setSuccessCnt(successCnt);
		
		//schedule_hist 완료 이력 저장
		//batchService.insertScheduleHist

    }
		/*
	//모집인 엑셀 업로드 후 1개월동안 처리상태가 미요청 + 모집인상태가 승인전인 경우 -> 모집인 데이터 및 모집인 관련 첨부파일 삭제(+서버)
	//매일 새벽1시
	@Scheduled(cron = "0 0 1 * * *")
	@SchedulerLock(name = "excelUploadUserAndFileDelete")
	public void excelUploadUserAndFileDelete() {
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
	//매일 새벽1시 1분
    @Scheduled(cron = "0 1 1 * * *")
    @SchedulerLock(name = "userUploadExcelFileDelete")
    public void userUploadExcelFileDelete() {
    	log.info("================ userUploadExcelFileDelete() START ================");
    	
    	
    	log.info("================ userUploadExcelFileDelete() END ================");
    }
    
    */
	
	
	//매일0시1분
	@Scheduled(cron = "0 1 0 * * *")
    @SchedulerLock(name = "apiAuthToken" , lockAtMostForString = ONE_MIN, lockAtLeastForString = ONE_MIN)
    public void apiKeyConnection() throws IOException {
    	log.info("================ apiAuthToken() START ================");
    	kfbApiService.getAuthToken();
    	log.info("================ apiAuthToken() END ================");
    }
	
	
	/*
	//30분마다 한번씩 테스트용
    //@Scheduled(cron = "* 0/30 * * * *")
    
	//30분마다 한번씩 테스트용
    @Scheduled(cron = "0/50 * * * * *")
    @SchedulerLock(name = "shedlockTest", lockAtMostForString = ONE_MIN, lockAtLeastForString = ONE_MIN)
    public void shedlockTest() {
    	log.info("================ shedlockTest()1111111111111111111111 START ================");
    	
    	String tempKey = LocalDateTime.now().toString();
    	kfbApiRepository.insertKfbApiKey(tempKey);
    	
    	log.info("================ shedlockTest()11111111111111111111111111111 END ================");
    }
    
    */
	
	
	/*
	//매일0시2분
	@Scheduled(cron = "0 2 0 * * *")
    @SchedulerLock(name = "inactiveUserCheck" , lockAtMostForString = ONE_MIN, lockAtLeastForString = ONE_MIN)
    public void inactiveUserCheck() throws IOException {
    	log.info("================ inactiveUserCheck() START ================");
    	UsersDomain usersDomain = new UsersDomain();
    	List<UsersDomain> inactiveList = batchRepository.selectInactiveUser(usersDomain);
    	for(UsersDomain tmp : inactiveList) {
    		log.info("================ inactiveUserCheck() FOR!!!!!!!!! ================");
    		UsersDomain inactiveDomain = new UsersDomain();
    		inactiveDomain.setUserSeq(tmp.getUserSeq());
    		batchRepository.insertInactiveUserBatch(inactiveDomain);
    		batchRepository.updateInactiveUserBatch(inactiveDomain);
    	}
    	
    	log.info("================ inactiveUserCheck() END ================");
    }
    */
	
	
	
	// 가등록 - 개인
	@Scheduled(cron="* 0/30 * * * *") 
    @SchedulerLock(name="preloanReg", lockAtMostForString = ONE_MIN, lockAtLeastForString = ONE_MIN)
    public void preloanReg() {
		
		BatchDomain batch = new BatchDomain();
		batch.setScheduleName("preloanCorpReg");
		// 스케쥴 이름으로 조회
		List<BatchDomain> batchList = batchService.selectBatchList(batch);
		int reqCnt = batchList.size();
		batch.setReqCnt(reqCnt);
		
		int successCnt = 0;
		
		// schedule_hist 시작 이력 저장
		int scheduleSeq = batchService.insertScheduleHist(batch);
		
		for(BatchDomain reg : batchList) {
			int success = batchService.preloanReg(reg);
			successCnt = successCnt + success;
		}
		
		batch.setScheduleHistSeq(scheduleSeq);
		batch.setSuccessCnt(successCnt);
		
		// schedule_hist 종료 이력 저장
		batchService.updateScheduleHist(batch);

    }
	
	
	
	// 가등록 - 법인
	@Scheduled(cron="* 0/30 * * * *") 
    @SchedulerLock(name="preloanCorpReg", lockAtMostForString = ONE_MIN, lockAtLeastForString = ONE_MIN)
    public void preloanCorpReg() {
		
		BatchDomain batch = new BatchDomain();
		batch.setScheduleName("preloanCorpReg");
		// 스케쥴 이름으로 조회
		List<BatchDomain> batchList = batchService.selectBatchList(batch);
		int reqCnt = batchList.size();
		batch.setReqCnt(reqCnt);
		
		int successCnt = 0;
		
		// schedule_hist 시작 이력 저장
		int scheduleSeq = batchService.insertScheduleHist(batch);
		
		for(BatchDomain reg : batchList) {
			int success = batchService.preloanCorpReg(reg);
			successCnt = successCnt + success;
		}
		
		batch.setScheduleHistSeq(scheduleSeq);
		batch.setSuccessCnt(successCnt);
		
		// schedule_hist 종료 이력 저장
		batchService.updateScheduleHist(batch);

    }
	
	
	
}
