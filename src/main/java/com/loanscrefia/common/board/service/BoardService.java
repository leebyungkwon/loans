package com.loanscrefia.common.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.board.repository.BoardRepository;
import com.loanscrefia.config.message.ResponseMsg;

@Service
public class BoardService {
   
	@Autowired private BoardRepository boardRepository;
	   
	// 공지사항 리스트
	@Transactional(readOnly=true)
	public List<BoardDomain> selectNoticeList(BoardDomain boardDomain){
		return boardRepository.selectNoticeList(boardDomain);
	}
   
	// 공지사항 리스트 -> 상세
	@Transactional(readOnly=true)
	public BoardDomain getNoticeDetail(BoardDomain boardDomain){
		return boardRepository.getNoticeDetail(boardDomain);
	}

	// 공지사항 리스트 -> 글쓰기
	@Transactional(readOnly=true)
	public BoardDomain NoticeReg(BoardDomain boardDomain){
		return boardRepository.getNoticeDetail(boardDomain);
	}

	// 공지사항 - 글 쓰기 페이지 -> -> Insert (글 등록)
	@Transactional(readOnly=true)
	public ResponseMsg SaveNoticeReg(BoardDomain boardDomain) {
		boardRepository.SaveNoticeReg(boardDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "글이 등록 되었습니다.");
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Update (글 수정)
	@Transactional(readOnly=true)
	public ResponseMsg UpdNoticeReg(BoardDomain boardDomain) {
		boardRepository.UpdNoticeReg(boardDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "글이 수정 되었습니다.");
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Delete (글 삭제)
	@Transactional(readOnly=true)
	public ResponseMsg DelNoticeReg(BoardDomain boardDomain) {
		boardRepository.DelNoticeReg(boardDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "글이 삭제 되었습니다.");
	}
	
}