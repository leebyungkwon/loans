package com.loanscrefia.admin.apply.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.service.ApplyService;
import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.service.UserService;
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/admin")
public class ApplyController {
	
	@Autowired 
	private ApplyService applyService;

	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/apply/applyPage")
	public String applyPage() {
		return CosntPage.BoApplyPage+"/applyList";
	}
	
	//리스트
	@PostMapping(value="/apply/applyList")
	public ResponseEntity<ResponseMsg> selectApplyList(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.selectApplyList(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 다운로드
	@PostMapping("/apply/applyListExcelDown")
	public void applyListExcelDown(ApplyDomain applyDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
 		List<ApplyDomain> excelDownList = applyService.selectApplyList(applyDomain);
 		new UtilExcel().downLoad(excelDownList, ApplyDomain.class, response.getOutputStream());
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/apply/applyIndvDetail")
    public ModelAndView applyIndvDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyIndvDetail");
    	Map<String, Object> result 	= applyService.getApplyIndvDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/apply/applyCorpDetail")
    public ModelAndView recruitCorpDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/apply/applyCorpImwonDetail")
    public ModelAndView recruitCorpImwonDetail(ApplyImwonDomain applyImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpImwonDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpImwonDetail(applyImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/apply/applyCorpExpertDetail")
    public ModelAndView recruitCorpExpertDetail(ApplyExpertDomain applyExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpExpertDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpExpertDetail(applyExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/apply/applyCorpItDetail")
    public ModelAndView recruitCorpItDetail(ApplyItDomain applyItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpItDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpItDetail(applyItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/apply/applyCorpEtcDetail")
    public ModelAndView recruitCorpEtcDetail(ApplyDomain applyDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoApplyPage+"/applyCorpEtcDetail");
    	Map<String, Object> result 	= applyService.getApplyCorpEtcDetail(applyDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 상태변경처리
	@PostMapping(value="/apply/updatePlStat")
	public ResponseEntity<ResponseMsg> updateRecruitPlStat(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.updateApplyPlStat(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// 첨부서류체크 등록
	@PostMapping(value="/apply/insertApplyCheck")
	public ResponseEntity<ResponseMsg> insertApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.insertApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 첨부서류체크 삭제
	@PostMapping(value="/apply/deleteApplyCheck")
	public ResponseEntity<ResponseMsg> deleteApplyCheck(ApplyCheckDomain applyCheckDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.deleteApplyCheck(applyCheckDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 실무자확인
	@PostMapping(value="/apply/applyCheck")
	public ResponseEntity<ResponseMsg> applyCheck(ApplyDomain applyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(applyService.applyCheck(applyDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
}
