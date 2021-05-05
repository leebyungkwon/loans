
package com.loanscrefia.common.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.board.service.BoardService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;

@Controller
@RequestMapping(value="/admin/board")
public class BoardController {
   
	@Autowired private BoardService boardService;

	// 공지사항 이동
	@GetMapping(value="/noticePage")
	public String Notice() {
		return CosntPage.BoBoardPage+"/noticeList";
	}

	// 공지사항 - 리스트
	@PostMapping(value="/noticeList")
	public ResponseEntity<ResponseMsg> NoticeList(BoardDomain boardDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(boardService.selectNoticeList(boardDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	// 공지사항 - 상세 페이지
	@PostMapping(value="/noticeDetail")
	public ModelAndView NoticeDetail(BoardDomain boardDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoBoardPage+"/noticeDetail");
		BoardDomain boardInfo = boardService.getNoticeDetail(boardDomain);
		mv.addObject("boardInfo", boardInfo);
		return mv;
	}

}