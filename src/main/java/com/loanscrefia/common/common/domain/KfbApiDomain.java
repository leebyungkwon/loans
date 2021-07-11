package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("kfbApi")
public class KfbApiDomain extends BaseDomain {

	/* -------------------------------------------------------------------------------------------------------
	 * 은행연합회 API 연동
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//공통
	private String code;				//권한코드
	private String token;				//토큰
	private String url;					//요청URL
	
	//은행연합회송신정보(tb_lc_kfb_req)
	private String sendData;			//요청데이터
	private int sendUser;				//요청자
	private String sendTimestamp;		//요청일시
	
	//은행연합회수신정보(tb_lc_kfb_res)
	private String resCode;				//응답코드
	private String resMsg;				//응답메세지
	private String resData;				//응답데이터
	private String resTimestamp;		//응답일시
	
	private String insDt;				//배치등록일
	private String searchPreLcNum;		//조회용가등록번호
	private String searchLcNum;			//조회용등록번호
	private String searchLoanType;		//조회용취급상품
	private String searchConNum;		//조회용계약번호
	private String searchFinCode;		//조회용금융기관
}
