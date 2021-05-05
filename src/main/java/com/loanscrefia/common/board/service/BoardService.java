package com.loanscrefia.common.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.board.repository.BoardRepository;

@Service
public class BoardService {
   
   @Autowired private BoardRepository boardRepository;
   
   // 회원사 회원 조회
   @Transactional(readOnly=true)
   public List<BoardDomain> selectNoticeList(BoardDomain boardDomain){
      return boardRepository.selectNoticeList(boardDomain);
   }
   
	// 회원사 조회 -> 상세
	@Transactional(readOnly=true)
	public BoardDomain getNoticeDetail(BoardDomain boardDomain){
		return boardRepository.getNoticeDetail(boardDomain);
	}
	
}