package com.loanscrefia.common.board.controller;

import java.util.List;

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
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;

@Controller
@RequestMapping(value="/common/board")
public class BoardController {

	@Autowired private BoardService boardService;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;

	// 공지사항 이동
	@GetMapping(value="/noticePage")
	public String Notice() {
		return CosntPage.BoBoardPage+"/noticeList";
	}

	// 공지사항 - 리스트
	@PostMapping(value="/noticeList")
	public ResponseEntity<ResponseMsg> noticeList(BoardDomain boardDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(boardService.selectNoticeList(boardDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	// 공지사항 - 상세 페이지
	@PostMapping(value="/noticeDetailPage")
	public ModelAndView noticeDetail(BoardDomain boardDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoBoardPage+"/noticeDetail");
		
		// 방문자 수 조회
		boardService.updNoticeCnt(boardDomain);
		
		BoardDomain noticeInfo = boardService.getNoticeDetail(boardDomain);
		mv.addObject("noticeInfo", noticeInfo);
		
		FileDomain file = new FileDomain();
		file.setFileSeq(noticeInfo.getFileSeq());
		file = commonService.getFile(file);
		mv.addObject("file", file);
		
		CodeDtlDomain param = new CodeDtlDomain();
		param.setCodeMstCd("NTC001");
		List<CodeDtlDomain> noticeCdList = codeService.selectCodeDtlList(param);
		
		
		mv.addObject("noticeCdList", noticeCdList);
		
		return mv;
	}

	// 공지사항 - 글 쓰기 페이지
	@PostMapping(value="/noticeRegPage")
	public ModelAndView writeNoticeReg(BoardDomain boardDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoBoardPage+"/noticeReg");
		
		CodeDtlDomain param = new CodeDtlDomain();
		param.setCodeMstCd("NTC001");
		List<CodeDtlDomain> noticeCdList = codeService.selectCodeDtlList(param);
		
		
		mv.addObject("noticeCdList", noticeCdList);
		
		return mv;
	}

	// 공지사항 - 수정 페이지
	@PostMapping(value="/noticeRegInsPage")
	public ModelAndView insNoticeReg(BoardDomain boardDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.BoBoardPage+"/noticeReg");
		
		BoardDomain noticeInfo = boardService.noticeReg(boardDomain);
		mv.addObject("noticeInfo", noticeInfo);
		
		FileDomain file = new FileDomain();
		file.setFileSeq(noticeInfo.getFileSeq());
		file = commonService.getFile(file);
		mv.addObject("file", file);
		
		CodeDtlDomain param = new CodeDtlDomain();
		param.setCodeMstCd("NTC001");
		List<CodeDtlDomain> noticeCdList = codeService.selectCodeDtlList(param);
	
		
		mv.addObject("noticeCdList", noticeCdList);
		
		return mv;
	}

	// 공지사항 - 글 쓰기 페이지 -> Insert (글 등록)
	@PostMapping(value="/saveNoticeReg")
	public ResponseEntity<ResponseMsg> saveNoticeReg(@RequestParam("files") MultipartFile[] files, BoardDomain boardDomain) {
		ResponseMsg responseMsg = boardService.saveNoticeReg(files, boardDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	// 공지사항 - 글 쓰기 페이지 -> Update (글 수정)
	@PostMapping(value="/updNoticeReg")
	public ResponseEntity<ResponseMsg> updNoticeReg(@RequestParam("files") MultipartFile[] files, BoardDomain boardDomain) {
		ResponseMsg responseMsg = boardService.updNoticeReg(files, boardDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

	// 공지사항 - 글 쓰기 페이지 -> Delete (글 삭제)
	@PostMapping(value="/delNoticeReg")
	public ResponseEntity<ResponseMsg> delNoticeReg(BoardDomain boardDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(boardService.delNoticeReg(boardDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}

}