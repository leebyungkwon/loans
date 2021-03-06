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
	
	// 공지사항 - 글 쓰기 페이지 -> Insert (글 등록)
	BoardDomain saveNoticeReg(BoardDomain boardDomain);
	
	// 공지사항 - 글 쓰기 페이지 -> Update (글 수정)
	BoardDomain updNoticeReg(BoardDomain boardDomain);
	
	// 공지사항 - 글 쓰기 페이지 -> Delete (글 삭제)
	BoardDomain delNoticeReg(BoardDomain boardDomain);
	
	// 공지사항 - 글 쓰기 페이지 - 조회수 증가
	BoardDomain updNoticeCnt(BoardDomain boardDomain);

}