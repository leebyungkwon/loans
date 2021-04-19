package com.loanscrefia.system.templete.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.system.templete.repository.TempleteRepository;

@Service
public class TempleteService {

	@Autowired private TempleteRepository templeteRepository;

	@Transactional
	public ResponseMsg templeteSave(BoardDomain board) {
		if(null == board.getBoardNo())	templeteRepository.insert(board);
		else							templeteRepository.update(board);
		if (null == board.getBoardNo())
			return new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002");
		return new ResponseMsg(HttpStatus.OK, "COM0001", board, "저장</br>저장하였습니다.");
	}

	@Transactional(readOnly=true)
	public BoardDomain findById(BoardDomain board) {
		//return templeteRepository.findById(board.getBoardNo());
		return null;
	}

	@Transactional
	public List<BoardDomain> selectTemplete(BoardDomain board) {
		return templeteRepository.selectTemplete(board);
	}

	public FileDomain getFile(FileDomain fileDomain) {
		return templeteRepository.getFile(fileDomain);
	}

}
