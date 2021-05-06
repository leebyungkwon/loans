
package com.loanscrefia.common.board.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.common.board.domain.BoardDomain;
import com.loanscrefia.common.board.service.BoardService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.util.UtilFile;

@Controller
@RequestMapping(value="/admin/board")
public class BoardController {
   
	
	@Autowired private BoardService boardService;
	@Autowired private CommonService commonService;
	@Autowired UtilFile utilFile;

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
	
	// 공지사항 - 글 쓰기 페이지, 수정 페이지
	@PostMapping(value="/noticeReg")
	public ModelAndView NoticeReg(BoardDomain boardDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoBoardPage+"/noticeReg");
		BoardDomain boardInfo = boardService.NoticeReg(boardDomain);
		mv.addObject("boardInfo", boardInfo);
		
//    	FileDomain file = new FileDomain();
//    	file.setFileSeq(boardInfo.getFileSeq());
//    	file = commonService.getFile(file);
//    	mv.addObject("file", file);
		
		return mv;
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Insert (글 등록)
	@PostMapping(value="/SaveNoticeReg")
	public ResponseEntity<ResponseMsg> SaveNoticeReg(@RequestParam("files") MultipartFile[] files, @Valid BoardDomain boardDomain) {
		Map<String, Object> ret = utilFile.setPath("notice") 
				.setFiles(files)
				.setExt("excel") 
				.upload();
		if((boolean) ret.get("success")) {
			
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				boardDomain.setFileSeq(file.get(0).getFileSeq());
			}
		}
		ResponseMsg responseMsg = boardService.SaveNoticeReg(boardDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Update (글 수정)
	@PostMapping(value="/UpdNoticeReg")
	public ResponseEntity<ResponseMsg> UpdNoticeReg(BoardDomain boardDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(boardService.UpdNoticeReg(boardDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 공지사항 - 글 쓰기 페이지 -> Delete (글 삭제)
	@PostMapping(value="/DelNoticeReg")
	public ResponseEntity<ResponseMsg> DelNoticeReg(BoardDomain boardDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(boardService.DelNoticeReg(boardDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}