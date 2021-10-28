package com.loanscrefia.common.board.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;

import lombok.Data;

@Data
@Alias("board")
public class BoardDomain extends BaseDomain{

	private String 	BoardNo;            	// (?)								// (삭제 가능 ? - templete 에서 사용중)
	private String 	BoardType;         		// (?)								// (삭제 가능 ? - templete 에서 사용중)
	
	private Long 	memberSeq;    			// 멤버 	시퀀스
	private Long 	noticeSeq;     		 	// 공지사항 시퀀스
	private String 	title;      			// 제목
	private String 	info;      				// 내용
	private int 	viewCnt;         		// 조회수		
	private int 	fileSeq;         		// 첨부파일 시퀀스
	private String 	delYn;           		// 삭제여부
	private int 	regSeq;            	 	// 등록자 	시퀀스
	private String 	regTimestamp;  	 		// 등록일시		
	private int 	updSeq;            	 	// 수정자 	시퀀스
	private String 	updTimestamp;    		// 수정일시
	private String	noticeDispCd;
	private String	noticeDispCdNm;
	
	List<FileDomain> fileList;
	
}