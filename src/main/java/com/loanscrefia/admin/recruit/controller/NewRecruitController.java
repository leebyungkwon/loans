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

import com.loanscrefia.admin.recruit.domain.NewRecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.admin.recruit.domain.RecruitExpertDomain;
import com.loanscrefia.admin.recruit.domain.RecruitImwonDomain;
import com.loanscrefia.admin.recruit.domain.RecruitItDomain;
import com.loanscrefia.admin.recruit.service.NewRecruitService;
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
public class NewRecruitController {
	
	@Autowired 
	private NewRecruitService recruitService;

	/* -------------------------------------------------------------------------------------------------------
	 * 협회 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//리스트 페이지
	@GetMapping(value="/newRecruit/newRecruitPage")
	public String recruitPage() {
		return CosntPage.BoNewRecruitPage+"/newRecruitList";
	}
	
	//리스트
	@PostMapping(value="/newRecruit/newRecruitList")
	public ResponseEntity<ResponseMsg> newRecruitList(NewRecruitDomain recruitDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(recruitService.selectNewRecruitList(recruitDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//엑셀 다운로드
	@PostMapping("/newRecruit/newRecruitListExcelDown")
	public void newRecruitListExcelDown(NewRecruitDomain recruitDomain, HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
		// 2021-07-27 페이징 false
		recruitDomain.setIsPaging("false");
 		List<NewRecruitDomain> excelDownList = recruitService.selectNewRecruitList(recruitDomain);
 		new UtilExcel().downLoad(excelDownList, NewRecruitDomain.class, response.getOutputStream());
	}
	
	//상세 페이지 : 개인
	@PostMapping(value="/newRecruit/newRecruitIndvDetail")
    public ModelAndView newRecruitIndvDetail(NewRecruitDomain recruitDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewRecruitPage+"/newRecruitIndvDetail");
    	Map<String, Object> result 	= recruitService.getNewRecruitIndvDetail(recruitDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 등록정보 탭
	@PostMapping(value="/newRecruit/newRecruitCorpDetail")
    public ModelAndView newRecruitCorpDetail(NewRecruitDomain recruitDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewRecruitPage+"/newRecruitCorpDetail");
    	Map<String, Object> result 	= recruitService.getNewRecruitCorpDetail(recruitDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 대표자 및 임원관련사항 탭
	@PostMapping(value="/newRecruit/newRecruitCorpImwonDetail")
    public ModelAndView newRecruitCorpImwonDetail(RecruitImwonDomain recruitImwonDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewRecruitPage+"/newRecruitCorpImwonDetail");
    	Map<String, Object> result 	= recruitService.getNewRecruitCorpImwonDetail(recruitImwonDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전문인력 탭
	@PostMapping(value="/newRecruit/newRecruitCorpExpertDetail")
    public ModelAndView newRecruitCorpExpertDetail(RecruitExpertDomain recruitExpertDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewRecruitPage+"/newRecruitCorpExpertDetail");
    	Map<String, Object> result 	= recruitService.getNewRecruitCorpExpertDetail(recruitExpertDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 전산인력 탭
	@PostMapping(value="/newRecruit/newRecruitCorpItDetail")
    public ModelAndView newRecruitCorpItDetail(RecruitItDomain recruitItDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewRecruitPage+"/newRecruitCorpItDetail");
    	Map<String, Object> result 	= recruitService.getNewRecruitCorpItDetail(recruitItDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	//상세 페이지 : 법인 > 기타 탭
	@PostMapping(value="/newRecruit/newRecruitCorpEtcDetail")
    public ModelAndView newRecruitCorpEtcDetail(NewRecruitDomain recruitDomain) {
    	ModelAndView mv 			= new ModelAndView(CosntPage.BoNewRecruitPage+"/newRecruitCorpEtcDetail");
    	Map<String, Object> result 	= recruitService.getNewRecruitCorpEtcDetail(recruitDomain);
    	mv.addObject("result", result);
        return mv;
    }
	
	// 상태변경처리
	@PostMapping(value="/newRecruit/updateNewPlStat")
	public ResponseEntity<ResponseMsg> updateNewPlStat(NewRecruitDomain recruitDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(recruitService.updateNewRecruitPlStat(recruitDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//이력보기
	@GetMapping(value="/newRecruit/recruitHistoryPopup")
	public ModelAndView recruitHistoryPopup(NewRecruitDomain recruitDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/recruitHistoryPopup");
		NewRecruitDomain result = recruitService.getNewRecruitHistDetail(recruitDomain);
		result.setHistType(recruitDomain.getHistType());
		mav.addObject("recruitHistDetail", result);
		mav.addObject("afterData", recruitDomain.getAfterData());
        return mav;
	}
	
	
	//첨부파일 이력
	@GetMapping(value="/newRecruit/newRecruitFileHistoryPopup")
    public ModelAndView newRecruitFileHistoryPopup(NewRecruitDomain recruitDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.Popup+"/recruitFileHistoryPopup");
    	Map<String, Object> result 	= recruitService.getNewFileHistDetail(recruitDomain);
    	mav.addObject("result", result);
        return mav;
    }
	
}
