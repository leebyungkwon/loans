package com.loanscrefia.common.board.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.board.domain.BoardDomain;

@Mapper 
public interface BoardRepository {

	// 공지사항 리스트
	List<BoardDomain> selectNoticeList(BoardDomain boardDomain);
   
	// 공지사항 - 상세보기
	BoardDomain getNoticeDetail(BoardDomain boardDomain);
}