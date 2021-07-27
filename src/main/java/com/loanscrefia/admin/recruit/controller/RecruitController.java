package com.loanscrefia.admin.recruit.controller;

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

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.admin.recruit.service.RecruitService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
import com.loanscrefia.util.UtilExcel;

@Controller
@RequestMapping(value="/admin")
public class RecruitController {
	
	@Autowired 
	private RecruitService recruitService;

	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/recruit/recruitPage")
	public String recruitPage() {
		return CosntPage.BoRecruitPage+"/recruitList";
	}
	
	//리스트
	@PostMapping(value="/recruit/recruitList")
	public ResponseEntity<ResponseMsg> selectRecruitList(RecruitDomain recruitDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(recruitService.selectRecruitList(recruitDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 다운로드
	@PostMapping("/recruit/recruitListExcelDown")
	public void recruitListExcelDown(RecruitDomain recruitDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		// 2021-07-27 페이징 false
		recruitDomain.setIsPaging("false");
 		List<RecruitDomain> excelDownList = recruitService.selectRecruitList(recruitDomain);
 		new UtilExcel().downLoad(excelDownList, RecruitDomain.class, response.getOutputStream());
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/recruit/recruitIndvDetail")
    public ModelAndView recruitIndvDetail(RecruitDomain recruitDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoRecruitPage+"/recruitIndvDetail");
    	Map<String, Object> result 	= recruitService.getRecruitIndvDetail(recruitDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/recruit/recruitCorpDetail")
    public ModelAndView recruitCorpDetail(RecruitDomain recruitDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoRecruitPage+"/recruitCorpDetail");
    	Map<String, Object> result 	= recruitService.getRecruitCorpDetail(recruitDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/recruit/recruitCorpImwonDetail")
    public ModelAndView recruitCorpImwonDetail(RecruitImwonDomain recruitImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoRecruitPage+"/recruitCorpImwonDetail");
    	Map<String, Object> result 	= recruitService.getRecruitCorpImwonDetail(recruitImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/recruit/recruitCorpExpertDetail")
    public ModelAndView recruitCorpExpertDetail(RecruitExpertDomain recruitExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoRecruitPage+"/recruitCorpExpertDetail");
    	Map<String, Object> result 	= recruitService.getRecruitCorpExpertDetail(recruitExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/recruit/recruitCorpItDetail")
    public ModelAndView recruitCorpItDetail(RecruitItDomain recruitItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoRecruitPage+"/recruitCorpItDetail");
    	Map<String, Object> result 	= recruitService.getRecruitCorpItDetail(recruitItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/recruit/recruitCorpEtcDetail")
    public ModelAndView recruitCorpEtcDetail(RecruitDomain recruitDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoRecruitPage+"/recruitCorpEtcDetail");
    	Map<String, Object> result 	= recruitService.getRecruitCorpEtcDetail(recruitDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 상태변경처리
	@PostMapping(value="/recruit/updatePlStat")
	public ResponseEntity<ResponseMsg> updateRecruitPlStat(RecruitDomain recruitDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(recruitService.updateRecruitPlStat(recruitDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//이력보기
	@GetMapping(value="/recruit/recruitHistoryPopup")
	public ModelAndView recruitHistoryPopup(RecruitDomain recruitDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/recruitHistoryPopup");
		RecruitDomain result = recruitService.getRecruitHistDetail(recruitDomain);
		result.setHistType(recruitDomain.getHistType());
		mav.addObject("recruitHistDetail", result);
		mav.addObject("afterData", recruitDomain.getAfterData());
        return mav;
	}
	
	
	//첨부파일 이력
	@GetMapping(value="/recruit/recruitFileHistoryPopup")
    public ModelAndView recruitFileHistoryPopup(RecruitDomain recruitDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/recruitFileHistoryPopup");
    	Map<String, Object> result 	= recruitService.getFileHistDetail(recruitDomain);
    	mav.addObject("result", result);
        return mav;
    }
	
}
