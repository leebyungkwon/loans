package com.loanscrefia.common.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.board.repository.BoardRepository;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.util.UtilFile;

@Service
public class BoardService {
   
	@Autowired private BoardRepository boardRepository;
	@Autowired private UtilFile utilFile;
	   
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
	
	// 공지사항 상세 -> 조회수 증가
	@Transactional
	public BoardDomain updNoticeCnt(BoardDomain boardDomain){
		return boardRepository.updNoticeCnt(boardDomain);
	}

	// 공지사항 리스트 -> 글쓰기 / 수정 페이지
	@Transactional(readOnly=true)
	public BoardDomain noticeReg(BoardDomain boardDomain){
		return boardRepository.getNoticeDetail(boardDomain);
	}

	// 공지사항 - 글 쓰기 페이지 -> Insert (글 등록)
	@Transactional
	public ResponseMsg saveNoticeReg(MultipartFile[] files, BoardDomain boardDomain) {
		
		Map<String, Object> ret = utilFile.setPath("notice") 
				  .setFiles(files)
				  .setExt("doc") 
				  .upload();

		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				boardDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		boardRepository.saveNoticeReg(boardDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "글이 등록 되었습니다.");
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Update (글 수정)
	@Transactional
	public ResponseMsg updNoticeReg(MultipartFile[] files, BoardDomain boardDomain) {
		
		Map<String, Object> ret = utilFile.setPath("notice") 
				  .setFiles(files)
				  .setExt("doc") 
				  .upload();

		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				boardDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		boardRepository.updNoticeReg(boardDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "글이 수정 되었습니다.");
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Delete (글 삭제)
	@Transactional
	public ResponseMsg delNoticeReg(BoardDomain boardDomain) {
		boardRepository.delNoticeReg(boardDomain);
		return new ResponseMsg(HttpStatus.OK, "COM0001", "글이 삭제 되었습니다.");
	}
	
}