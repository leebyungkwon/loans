package com.loanscrefia.system.templete.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.common.domain.FileDomain;

@Mapper
public interface TempleteRepository  {
	List<BoardDomain> selectTemplete(BoardDomain board);
	Long save(BoardDomain board);
	BoardDomain findById(Long boardNo);
	void insert(BoardDomain board);
	void update(BoardDomain board);
	
	
	// 첨부파일 단건 조회
	FileDomain getFile(FileDomain fileDomain);
	
	// 첨부파일 리스트 조회
	List<FileDomain> getFileList(FileDomain fileDomain);
	
}
