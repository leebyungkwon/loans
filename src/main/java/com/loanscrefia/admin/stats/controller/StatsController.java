package com.loanscrefia.admin.stats.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.stats.domain.StatsDomain;
import com.loanscrefia.admin.stats.service.StatsService;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;

@Controller
@RequestMapping(value="/admin")
public class StatsController {

	@Autowired private StatsService statsService;
	@Autowired private CodeService codeService;
	
	//회사별 모집인
	@GetMapping(value="/stats1/statsPage")
	public ModelAndView statsPage1(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats1");
		mav.addObject("plRegStat",statsDomain.getPlRegStat());
		mav.addObject("plStat",statsDomain.getPlStat());
		mav.addObject("result", statsService.selectStatsResult1(statsDomain));
		return mav;
	}
	
	//회사별 모집인 엑셀 다운로드
	@GetMapping(value="/stats1/excelDown")
	public ModelAndView statsExcelDown1(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats1");
		mav.addObject("result", statsService.selectStatsResult1(statsDomain));
		return mav;
	}
	
	//경력신규
	@GetMapping(value="/stats2/statsPage")
	public ModelAndView statsPage2(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats2");
		mav.addObject("plRegStat",statsDomain.getPlRegStat());
		mav.addObject("plStat",statsDomain.getPlStat());
		mav.addObject("result", statsService.selectStatsResult2(statsDomain));
		return mav;
	}
	
	//경력신규 엑셀 다운로드
	@GetMapping(value="/stats2/excelDown")
	public ModelAndView statsExcelDown2(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats2");
		mav.addObject("result", statsService.selectStatsResult2(statsDomain));
		return mav;
	}
	
	//경력 현황
	@GetMapping(value="/stats3/statsPage")
	public ModelAndView statsPage3(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats3");
		mav.addObject("result", statsService.selectStatsResult3(statsDomain));
		return mav;
	}
	
	//경력 현황 엑셀 다운로드
	@GetMapping(value="/stats3/excelDown")
	public ModelAndView statsExcelDown3(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats3");
		mav.addObject("result", statsService.selectStatsResult3(statsDomain));
		return mav;
	}
	
	//부적격처리건수(모집인별)
	@GetMapping(value="/stats4/statsPage")
	public ModelAndView statsPage4(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats4");
		mav.addObject("result", statsService.selectStatsResult4(statsDomain));
		return mav;
	}
	
	//부적격처리건수(모집인별) 엑셀 다운로드
	@GetMapping(value="/stats4/excelDown")
	public ModelAndView statsExcelDown4(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats4");
		mav.addObject("result", statsService.selectStatsResult4(statsDomain));
		return mav;
	}
	
	//등록처리현황(회사별)
	@GetMapping(value="/stats5/statsPage")
	public ModelAndView statsPage5(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats5");
		mav.addObject("result", statsService.selectStatsResult5(statsDomain));
		return mav;
	}
	
	//등록처리현황(회사별) 엑셀 다운로드
	@GetMapping(value="/stats5/excelDown")
	public ModelAndView statsExcelDown5(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats5");
		mav.addObject("result", statsService.selectStatsResult5(statsDomain));
		return mav;
	}
	
	//등록처리현황(모집인별)
	@GetMapping(value="/stats6/statsPage")
	public ModelAndView statsPage6(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats6");
		mav.addObject("result", statsService.selectStatsResult6(statsDomain));
		return mav;
	}
	
	//등록처리현황(모집인별) 엑셀 다운로드
	@GetMapping(value="/stats6/excelDown")
	public ModelAndView statsExcelDown6(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats6");
		mav.addObject("result", statsService.selectStatsResult6(statsDomain));
		return mav;
	}
		
	//해지처리현황(회사별)
	@GetMapping(value="/stats7/statsPage")
	public ModelAndView statsPage7(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats7");
		mav.addObject("result", statsService.selectStatsResult7(statsDomain));
		return mav;
	}
	
	//해지처리현황(회사별) 엑셀 다운로드
	@GetMapping(value="/stats7/excelDown")
	public ModelAndView statsExcelDown7(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats7");
		mav.addObject("result", statsService.selectStatsResult7(statsDomain));
		return mav;
	}
	
	//해지처리현황(모집인별)
	@GetMapping(value="/stats8/statsPage")
	public ModelAndView statsPage8(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats8");
		mav.addObject("result", statsService.selectStatsResult8(statsDomain));
		return mav;
	}
	
	//해지처리현황(모집인별) 엑셀 다운로드
	@GetMapping(value="/stats8/excelDown")
	public ModelAndView statsExcelDown8(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats8");
		mav.addObject("result", statsService.selectStatsResult8(statsDomain));
		return mav;
	}
	
	//불법모집 이력현황
	@GetMapping(value="/stats9/statsPage")
	public ModelAndView statsPage9(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/stats9");
		
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		mav.addObject("violationCdList", violationCodeList);
		mav.addObject("result", statsService.selectStatsResult9(statsDomain));
		return mav;
	}
	
	//불법모집 이력현황 엑셀 다운로드
	@GetMapping(value="/stats9/excelDown")
	public ModelAndView statsExcelDown9(StatsDomain statsDomain) {
		ModelAndView mav = new ModelAndView(CosntPage.BoStatsPage+"/excel/stats9");
		
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		mav.addObject("violationCdList", violationCodeList);
		mav.addObject("result", statsService.selectStatsResult9(statsDomain));
		return mav;
	}
	
}
