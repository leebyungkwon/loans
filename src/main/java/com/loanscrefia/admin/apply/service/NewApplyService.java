package com.loanscrefia.admin.apply.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.loanscrefia.admin.apply.domain.ApplyCheckDomain;
import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.apply.repository.NewApplyRepository;
import com.loanscrefia.admin.corp.service.CorpService;
import com.loanscrefia.admin.stats.domain.StatsDomain;
import com.loanscrefia.admin.stats.service.StatsService;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.repository.UsersRepository;
import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.email.domain.EmailDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.ApiService;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.common.common.sms.domain.SmsDomain;
import com.loanscrefia.common.common.sms.repository.SmsRepository;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.repository.SearchRepository;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.domain.ProductDtlDomain;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.NewUserRepository;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.service.BatchService;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;
import com.loanscrefia.util.UtilMask;

import sinsiway.CryptoUtil;

@Service
public class NewApplyService {

	@Autowired
	private NewApplyRepository applyRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private NewUserRepository userRepo;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private SmsRepository smsRepository;
	
	//???????????????
	@Autowired 
	private KfbApiService kfbApiService;
	
	@Autowired
	private KfbApiRepository kfbApiRepository;
	
	@Autowired private CorpService corpService;
	
	@Autowired private ApiService apiService; //???????????????
	@Autowired private BatchService batchService;
	@Autowired private SearchRepository searchRepository;
	@Autowired private StatsService statsService;
	
	//????????? ????????????
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//??????????????? API ????????????
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;
	
	//????????? ????????????
	@Value("${email.apply}")
	public boolean emailApply;
	
	//SMS ????????????
	@Value("${sms.apply}")
	public boolean smsApply;

	
	// 2021-10-13 ????????? ?????? ???????????? ?????????
	@Transactional(readOnly=true)
	public List<NewApplyDomain> selectNewApplyList(NewApplyDomain newApplyDomain){
		
		UtilMask mask = new UtilMask();
		
		MemberDomain memberDomain = new MemberDomain();
		MemberDomain result = commonService.getMemberDetail(memberDomain);
		newApplyDomain.setCreGrp(result.getCreGrp());
		
		// ???????????? ??? ???????????? ????????? ??? ??????
		if(StringUtils.isNotEmpty(newApplyDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				newApplyDomain.setPlMerchantNo(CryptoUtil.encrypt(newApplyDomain.getPlMerchantNo()));
			}else {
				newApplyDomain.setPlMerchantNo(newApplyDomain.getPlMerchantNo());
			}
		}
		if(StringUtils.isNotEmpty(newApplyDomain.getPlMZId())) {
			if(cryptoApply) {
				newApplyDomain.setPlMZId(CryptoUtil.encrypt(newApplyDomain.getPlMZId()));
			}else {
				newApplyDomain.setPlMZId(newApplyDomain.getPlMZId());
			}
		}
		
		List<NewApplyDomain> applyResultList = applyRepository.selectNewApplyList(newApplyDomain);
		String plMZId = "";
		for(NewApplyDomain list : applyResultList) {
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				if(cryptoApply) {
					plMZId 	= CryptoUtil.decrypt(list.getPlMZId());
				}else {
					plMZId 	= list.getPlMZId();
					newApplyDomain.setPlMZId(plMZId);
				}
				if(StringUtils.isNotEmpty(plMZId)) {
					// 2021-09-30 ????????????????????? ???????????? ????????? ??????
					if(!"false".equals(newApplyDomain.getIsPaging())) {
						plMZId = mask.maskSSN(plMZId);
					}
				}
				plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
				list.setPlMZId(plMZId);
			}
			
			StringBuilder merchantNo = new StringBuilder();
			if(StringUtils.isNotEmpty(list.getPlMerchantNo())) {
				if(cryptoApply) {
					merchantNo.append(CryptoUtil.decrypt(list.getPlMerchantNo()));
				}else {
					merchantNo.append(list.getPlMerchantNo());
				}
				merchantNo.insert(6, "-");
				list.setPlMerchantNo(merchantNo.toString());
			}
		}
		return applyResultList;
	}
	
	// 2021-10-13 ????????? ?????? ?????? : ??????
	@Transactional(readOnly=true)
	public Map<String,Object> getNewApplyIndvDetail(NewApplyDomain newApplyDomain){
		//?????? ??????
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewApplyDomain applyInfo = applyRepository.getNewApplyDetail(newApplyDomain);
		UsersDomain usersDomain = new UsersDomain();
		
		// user_seq??? ???????????? ????????? ??????
		usersDomain.setUserSeq(applyInfo.getUserSeq());
		usersDomain.setPlClass(applyInfo.getPlClass());
		List<UsersDomain> disList = usersRepository.selectUsersDisList(usersDomain);
		int disCnt = 0;
		if(disList.size() > 0) {
			for(UsersDomain dis : disList) {
				if(StringUtils.isNotEmpty(dis.getDisVal())) {
					if("Y".equals(dis.getDisVal())) {
						disCnt++;
					}
				}
			}
			if(disCnt > 0) {
				applyInfo.setDisVal("Y");
			}else {
				applyInfo.setDisVal("N");
			}
		}else {
			applyInfo.setDisVal("");
		}
		
		
		// ORIGIN ???????????? ????????? ??????
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(applyInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//????????????
    	if(applyInfo.getFileSeq() != null) {
    		FileDomain fileParam = new FileDomain();
    		
    		fileParam.setFileGrpSeq(applyInfo.getFileSeq());
        	List<FileDomain> fileList = commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				applyInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				applyInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				applyInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				applyInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				applyInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				applyInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("7")) {
        				applyInfo.setFileType7(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("8")) {
        				applyInfo.setFileType8(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("9")) {
        				applyInfo.setFileType9(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("10")) { //???????????? ??? ????????????
        				applyInfo.setFileType10(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("11")) { //???????????? ??? ????????????
        				applyInfo.setFileType11(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("12")) {
        				applyInfo.setFileType12(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("13")) {
        				applyInfo.setFileType13(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("14")) {
        				applyInfo.setFileType14(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				applyInfo.setFileType15(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("16")) {
        				applyInfo.setFileType16(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("17")) {
        				applyInfo.setFileType17(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("18")) {
        				applyInfo.setFileType18(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("19")) {
        				applyInfo.setFileType19(fileList.get(i));
        			}
        			
        			
        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
        			checkDomain.setFileSeq(fileList.get(i).getFileSeq());
        			List<ApplyCheckDomain> checkList = applyRepository.selectNewApplyCheckList(checkDomain);
        			for(ApplyCheckDomain tmp : checkList) {
        				if("1".equals(tmp.getCheckCd())){
        					applyInfo.setCheckCd1(tmp.getCheckCd());
        				}else if("2".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd2(tmp.getCheckCd());
        				}else if("3".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd3(tmp.getCheckCd());
        				}else if("4".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd4(tmp.getCheckCd());
        				}else if("5".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd5(tmp.getCheckCd());
        				}else if("6".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd6(tmp.getCheckCd());
        				}else if("7".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd7(tmp.getCheckCd());
        				}else if("8".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd8(tmp.getCheckCd());
        				}else if("9".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd9(tmp.getCheckCd());
        				}else if("10".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd10(tmp.getCheckCd());
        				}else if("11".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd11(tmp.getCheckCd());
        				}else if("12".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd12(tmp.getCheckCd());
        				}else if("13".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd13(tmp.getCheckCd());
        				}else if("14".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd14(tmp.getCheckCd());
        				}else if("15".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd15(tmp.getCheckCd());
        				}else if("16".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd16(tmp.getCheckCd());
        				}else if("17".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd17(tmp.getCheckCd());
        				}else if("18".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd18(tmp.getCheckCd());
        				}else if("19".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd19(tmp.getCheckCd());
        				}else if("20".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd20(tmp.getCheckCd());
        				}else if("21".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd21(tmp.getCheckCd());
        				}else if("22".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd22(tmp.getCheckCd());
        				}
        			}
        		}
        	}
    	}
    	
    	NewUserDomain userDomain = new NewUserDomain();
    	userDomain.setMasterSeq(newApplyDomain.getMasterSeq());
    	userDomain.setUserSeq(applyInfo.getUserSeq());
    	userDomain.setVioNum("notEmpty");
    	//????????????
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
		
    	MemberDomain memberDomain = new MemberDomain();
    	memberDomain.setMemberSeq(Long.valueOf(applyInfo.getMemberSeq()));
    	MemberDomain memberResult = commonService.getCompanyMemberDetail(memberDomain);
    	
    	
    	//???????????????????????? ????????? ??????
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(userDomain);
    	
    	
    	//??????
    	result.put("addrCodeList", addrCodeList);
    	result.put("applyInfo", applyInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("memberResult", memberResult);
    	if(loginInfo.getCreGrp() != null) {
    		result.put("adminCreGrp", loginInfo.getCreGrp());
    	}
    	result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	// 2021-10-13 ????????? ?????? ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewApplyCorpDetail(NewApplyDomain newApplyDomain){
		
		//?????? ??????
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//?????? ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewApplyDomain applyInfo = applyRepository.getNewApplyDetail(newApplyDomain);
		
		// 2021-11-04 ???????????? ?????????????????? ???????????? ????????? ???????????? ??????
		UsersDomain usersDomain = new UsersDomain();
		
		// user_seq??? ???????????? ????????? ??????
		usersDomain.setUserSeq(applyInfo.getUserSeq());
		usersDomain.setPlClass(applyInfo.getPlClass());
		List<UsersDomain> disList = usersRepository.selectUsersDisList(usersDomain);
		int disCnt = 0;
		if(disList.size() > 0) {
			for(UsersDomain dis : disList) {
				if(StringUtils.isNotEmpty(dis.getDisVal())) {
					if("Y".equals(dis.getDisVal())) {
						disCnt++;
					}
				}
			}
			if(disCnt > 0) {
				applyInfo.setDisVal("Y");
			}else {
				applyInfo.setDisVal("N");
			}
		}else {
			applyInfo.setDisVal("");
		}
		
		// ORIGIN ???????????? ????????? ??????
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(applyInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//????????????
		if(applyInfo.getFileSeq() != null) {
			FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(applyInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				applyInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				applyInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				applyInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				applyInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				applyInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				applyInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				applyInfo.setFileType15(fileList.get(i));
					}else if(fileList.get(i).getFileType().equals("31")) {
        				applyInfo.setFileType31(fileList.get(i));
					}else if(fileList.get(i).getFileType().equals("32")) {
        				applyInfo.setFileType32(fileList.get(i));
					}
        			
        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
        			checkDomain.setFileSeq(fileList.get(i).getFileSeq());
        			List<ApplyCheckDomain> checkList = applyRepository.selectNewApplyCheckList(checkDomain);
        			for(ApplyCheckDomain tmp : checkList) {
        				if("1".equals(tmp.getCheckCd())){
        					applyInfo.setCheckCd1(tmp.getCheckCd());
        				}else if("2".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd2(tmp.getCheckCd());
        				}else if("3".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd3(tmp.getCheckCd());
        				}else if("4".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd4(tmp.getCheckCd());
        				}else if("5".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd5(tmp.getCheckCd());
        				}else if("6".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd6(tmp.getCheckCd());
        				}else if("7".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd7(tmp.getCheckCd());
        				}else if("8".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd8(tmp.getCheckCd());
        				}else if("9".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd9(tmp.getCheckCd());
        				}else if("10".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd10(tmp.getCheckCd());
        				}else if("11".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd11(tmp.getCheckCd());
        				}else if("12".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd12(tmp.getCheckCd());
        				}else if("13".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd13(tmp.getCheckCd());
        				}else if("112".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd112(tmp.getCheckCd());
        				}else if("116".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd116(tmp.getCheckCd());
        				}else if("117".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd117(tmp.getCheckCd());
        				}else if("118".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd118(tmp.getCheckCd());
        				}else if("119".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd119(tmp.getCheckCd());
        				}else if("120".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd120(tmp.getCheckCd());
        				}
        			}
        		}
        	}
		}
		
    	NewUserDomain userDomain = new NewUserDomain();
    	userDomain.setMasterSeq(newApplyDomain.getMasterSeq());
    	userDomain.setUserSeq(applyInfo.getUserSeq());
    	userDomain.setVioNum("notEmpty");
    	//????????????
    	List<NewUserDomain> violationInfoList = userRepo.selectNewUserViolationInfoList(userDomain);
    	
    	//???????????????????????? ????????? ??????
    	List<ProductDtlDomain> plProductDetailList	= userRepo.selectPlProductDetailList(userDomain);
		
		//??????
		result.put("addrCodeList", addrCodeList);
		result.put("applyInfo", applyInfo);
		result.put("violationInfoList", violationInfoList);
    	if(loginInfo.getCreGrp() != null) {
    		result.put("adminCreGrp", loginInfo.getCreGrp());
    	}
    	result.put("plProductDetailList", plProductDetailList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(????????? ??? ?????????????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewApplyCorpImwonDetail(ApplyImwonDomain applyImwonDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();

		//??????(??????,??????) ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//???????????? ?????? ?????????
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("FTM001");
		List<CodeDtlDomain> fullTmStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//?????????????????? ?????? ?????????
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("EXP001");
		List<CodeDtlDomain> expertStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewApplyDomain dtlParam			= new NewApplyDomain();
		dtlParam.setMasterSeq(applyImwonDomain.getMasterSeq());
		NewApplyDomain applyInfo 		= applyRepository.getNewApplyDetail(dtlParam);
		
		// ORIGIN ???????????? ????????? ??????
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(applyInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		
		//????????? ??? ?????? ?????????
		List<ApplyImwonDomain> imwonList = applyRepository.selectNewApplyCorpImwonList(applyImwonDomain);
		
		//????????????
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
				// ???????????? ????????? ??????		
				StringBuilder imwonZid = new StringBuilder();
				if(StringUtils.isNotEmpty(imwonList.get(i).getPlMZId())) {
					if(cryptoApply) {
						imwonZid.append(CryptoUtil.decrypt(imwonList.get(i).getPlMZId()));
					}else {
						imwonZid.append(imwonList.get(i).getPlMZId());
					}
					imwonZid.insert(6, "-");
					imwonList.get(i).setPlMZId(imwonZid.toString());
				}
				
				if(imwonList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(imwonList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("7")) {
							imwonList.get(i).setFileType7(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("8")) {
							imwonList.get(i).setFileType8(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("9")) {
							imwonList.get(i).setFileType9(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("10")) {
							imwonList.get(i).setFileType10(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("11")) {
							imwonList.get(i).setFileType11(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("12")) {
							imwonList.get(i).setFileType12(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("13")) {
							imwonList.get(i).setFileType13(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("14")) {
							imwonList.get(i).setFileType14(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("27")) {
							imwonList.get(i).setFileType27(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("28")) {
							imwonList.get(i).setFileType28(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("30")) {
							imwonList.get(i).setFileType30(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("33")) {
							imwonList.get(i).setFileType33(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("34")) {
							imwonList.get(i).setFileType34(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("46")) {
							imwonList.get(i).setFileType46(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("47")) {
							imwonList.get(i).setFileType47(fileList.get(j));
						}
						
						
	        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
	        			checkDomain.setFileSeq(fileList.get(j).getFileSeq());
	        			List<ApplyCheckDomain> checkList = applyRepository.selectNewApplyCheckList(checkDomain);
	        			for(ApplyCheckDomain tmp : checkList) {
	        				if("100".equals(tmp.getCheckCd())){
	        					imwonList.get(i).setCheckCd100(tmp.getCheckCd());
	        				}else if("101".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd101(tmp.getCheckCd());
	        				}else if("102".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd102(tmp.getCheckCd());
	        				}else if("103".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd103(tmp.getCheckCd());
	        				}else if("104".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd104(tmp.getCheckCd());
	        				}else if("105".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd105(tmp.getCheckCd());
	        				}else if("106".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd106(tmp.getCheckCd());
	        				}else if("107".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd107(tmp.getCheckCd());
	        				}else if("108".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd108(tmp.getCheckCd());
	        				}else if("109".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd109(tmp.getCheckCd());
	        				}else if("110".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd110(tmp.getCheckCd());
	        				}else if("111".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd111(tmp.getCheckCd());
	        				}else if("113".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd113(tmp.getCheckCd());
	        				}else if("114".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd114(tmp.getCheckCd());
	        				}else if("115".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd115(tmp.getCheckCd());
	        				}else if("116".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd116(tmp.getCheckCd());
	        				}else if("117".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd117(tmp.getCheckCd());
	        				}else if("118".equals(tmp.getCheckCd())) {
	        					imwonList.get(i).setCheckCd118(tmp.getCheckCd());
	        				}
	        			}
						
					}
				}
			}
		}
		
		//??????
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("applyInfo", applyInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewApplyCorpExpertDetail(ApplyExpertDomain applyExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????(??????,??????) ?????? ?????????
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//??????
		NewApplyDomain dtlParam			= new NewApplyDomain();
		dtlParam.setMasterSeq(applyExpertDomain.getMasterSeq());
		NewApplyDomain applyInfo 		= applyRepository.getNewApplyDetail(dtlParam);
		
		// ORIGIN ???????????? ????????? ??????
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(applyInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//???????????? ?????????
		List<ApplyExpertDomain> expertList = applyRepository.selectNewApplyCorpExpertList(applyExpertDomain);
		
		//????????????
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
				
				// ???????????? ????????? ??????		
				StringBuilder expertZid = new StringBuilder();
				if(StringUtils.isNotEmpty(expertList.get(i).getPlMZId())) {
					if(cryptoApply) {
						expertZid.append(CryptoUtil.decrypt(expertList.get(i).getPlMZId()));
					}else {
						expertZid.append(expertList.get(i).getPlMZId());
					}
					expertZid.insert(6, "-");
					expertList.get(i).setPlMZId(expertZid.toString());
				}
				
				if(expertList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(expertList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("16")) {
							expertList.get(i).setFileType16(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("17")) {
							expertList.get(i).setFileType17(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("18")) {
							expertList.get(i).setFileType18(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("31")) {
							expertList.get(i).setFileType31(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("35")) {
							expertList.get(i).setFileType35(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("36")) {
							expertList.get(i).setFileType36(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("39")) {
							expertList.get(i).setFileType39(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("40")) {
							expertList.get(i).setFileType40(fileList.get(j));
						}
						
	        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
	        			checkDomain.setFileSeq(fileList.get(j).getFileSeq());
	        			List<ApplyCheckDomain> checkList = applyRepository.selectNewApplyCheckList(checkDomain);
	        			for(ApplyCheckDomain tmp : checkList) {
	        				if("200".equals(tmp.getCheckCd())){
	        					expertList.get(i).setCheckCd200(tmp.getCheckCd());
	        				}else if("201".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd201(tmp.getCheckCd());
	        				}else if("202".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd202(tmp.getCheckCd());
	        				}else if("203".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd203(tmp.getCheckCd());
	        				}else if("204".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd204(tmp.getCheckCd());
	        				}else if("205".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd205(tmp.getCheckCd());
	        				}else if("206".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd206(tmp.getCheckCd());
	        				}else if("207".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd207(tmp.getCheckCd());
	        				}else if("208".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd208(tmp.getCheckCd());
	        				}else if("209".equals(tmp.getCheckCd())) {
	        					expertList.get(i).setCheckCd209(tmp.getCheckCd());
	        				}
	        			}
					}
				}
			}
		}
		
		//??????
		result.put("careerTypList", careerTypList);
		result.put("applyInfo", applyInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(???????????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewApplyCorpItDetail(ApplyItDomain applyItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
		NewApplyDomain dtlParam			= new NewApplyDomain();
		dtlParam.setMasterSeq(applyItDomain.getMasterSeq());
		NewApplyDomain applyInfo 		= applyRepository.getNewApplyDetail(dtlParam);
		
		
		// ORIGIN ???????????? ????????? ??????
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(applyInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//???????????? ?????????
		List<ApplyItDomain> itList 	= applyRepository.selectNewApplyCorpItList(applyItDomain);
		
		//????????????
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
				
				// ???????????? ????????? ??????		
				StringBuilder itZid = new StringBuilder();
				if(StringUtils.isNotEmpty(itList.get(i).getPlMZId())) {
					if(cryptoApply) {
						itZid.append(CryptoUtil.decrypt(itList.get(i).getPlMZId()));
					}else {
						itZid.append(itList.get(i).getPlMZId());
					}
					itZid.insert(6, "-");
					itList.get(i).setPlMZId(itZid.toString());
				}
				
				if(itList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(itList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("19")) {
							itList.get(i).setFileType19(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("20")) {
							itList.get(i).setFileType20(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("37")) {
							itList.get(i).setFileType37(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("38")) {
							itList.get(i).setFileType38(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("48")) {
							itList.get(i).setFileType48(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("49")) {
							itList.get(i).setFileType49(fileList.get(j));
						}
						
	        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
	        			checkDomain.setFileSeq(fileList.get(j).getFileSeq());
	        			List<ApplyCheckDomain> checkList = applyRepository.selectNewApplyCheckList(checkDomain);
	        			for(ApplyCheckDomain tmp : checkList) {
	        				if("300".equals(tmp.getCheckCd())){
	        					itList.get(i).setCheckCd300(tmp.getCheckCd());
	        				}else if("301".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd301(tmp.getCheckCd());
	        				}else if("302".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd302(tmp.getCheckCd());
	        				}else if("303".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd303(tmp.getCheckCd());
	        				}else if("304".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd304(tmp.getCheckCd());
	        				}else if("305".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd305(tmp.getCheckCd());
	        				}else if("306".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd306(tmp.getCheckCd());
	        				}else if("307".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd307(tmp.getCheckCd());
	        				}else if("308".equals(tmp.getCheckCd())) {
	        					itList.get(i).setCheckCd308(tmp.getCheckCd());
	        				}
	        			}
						
					}
				}
			}
		}
		
		//??????
		result.put("applyInfo", applyInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ?????? : ??????(?????? ???)
	@Transactional(readOnly=true)
	public Map<String,Object> getNewApplyCorpEtcDetail(NewApplyDomain newApplyDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//??????
		NewApplyDomain applyInfo 		= applyRepository.getNewApplyDetail(newApplyDomain);
		
		// ORIGIN ???????????? ????????? ??????
		StringBuilder orgMerchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getOriginPlMerchantNo())) {
			if(cryptoApply) {
				orgMerchantNo.append(CryptoUtil.decrypt(applyInfo.getOriginPlMerchantNo()));
			}else {
				orgMerchantNo.append(applyInfo.getOriginPlMerchantNo());
			}
			orgMerchantNo.insert(6, "-");
			applyInfo.setOriginPlMerchantNo(orgMerchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(applyInfo.getPlMerchantNo()));
			}else {
				merchantNo.append(applyInfo.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			applyInfo.setPlMerchantNo(merchantNo.toString());
		}
		
		// ???????????? ????????? ??????		
		StringBuilder zid = new StringBuilder();
		if(StringUtils.isNotEmpty(applyInfo.getPlMZId())) {
			if(cryptoApply) {
				zid.append(CryptoUtil.decrypt(applyInfo.getPlMZId()));
			}else {
				zid.append(applyInfo.getPlMZId());
			}
			zid.insert(6, "-");
			applyInfo.setPlMZId(zid.toString());
		}
		
		//????????????
    	if(applyInfo.getFileSeq() != null) {
    		FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(applyInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("21")) {
        				applyInfo.setFileType21(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("22")) {
        				applyInfo.setFileType22(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("23")) {
        				applyInfo.setFileType23(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("24")) {
        				applyInfo.setFileType24(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("25")) {
        				applyInfo.setFileType25(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("26")) {
        				applyInfo.setFileType26(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("29")) {
        				applyInfo.setFileType29(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("41")) {
        				applyInfo.setFileType41(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("42")) {
        				applyInfo.setFileType42(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("43")) {
        				applyInfo.setFileType43(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("44")) {
        				applyInfo.setFileType44(fileList.get(i));
        			}
        			
        			ApplyCheckDomain checkDomain = new ApplyCheckDomain();
        			checkDomain.setFileSeq(fileList.get(i).getFileSeq());
        			List<ApplyCheckDomain> checkList = applyRepository.selectNewApplyCheckList(checkDomain);
        			for(ApplyCheckDomain tmp : checkList) {
        				if("400".equals(tmp.getCheckCd())){
        					applyInfo.setCheckCd400(tmp.getCheckCd());
        				}else if("401".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd401(tmp.getCheckCd());
        				}else if("402".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd402(tmp.getCheckCd());
        				}else if("403".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd403(tmp.getCheckCd());
        				}else if("404".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd404(tmp.getCheckCd());
        				}else if("405".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd405(tmp.getCheckCd());
        				}else if("406".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd406(tmp.getCheckCd());
        				}else if("407".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd407(tmp.getCheckCd());
        				}else if("408".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd408(tmp.getCheckCd());
        				}else if("409".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd409(tmp.getCheckCd());
        				}else if("410".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd410(tmp.getCheckCd());
        				}else if("411".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd411(tmp.getCheckCd());
        				}else if("412".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd412(tmp.getCheckCd());
        				}else if("413".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd413(tmp.getCheckCd());
        				}else if("414".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd414(tmp.getCheckCd());
        				}else if("415".equals(tmp.getCheckCd())) {
        					applyInfo.setCheckCd415(tmp.getCheckCd());
        				}
        			}
        			
        		}
        	}
    	}
    	
		//??????
		result.put("applyInfo", applyInfo);
		
		return result;
	}
	
	//????????? ?????? ??? ?????? > ??????????????????
	@Transactional
	public ResponseMsg updateNewApplyPlStat(NewApplyDomain newApplyDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", null,  "????????? ?????????????????????.");
		NewApplyDomain statCheck = applyRepository.getNewApplyDetail(newApplyDomain);
		
		// ?????? ??????????????? ????????? ?????? ???????????? ??????
		if(!newApplyDomain.getOldPlStat().equals(statCheck.getPlStat())){
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}

		//??????????????? SMS ??????
		if(StringUtils.isEmpty(statCheck.getPlCellphone())) {
			return new ResponseMsg(HttpStatus.OK, "fail", "?????????????????? ????????? ?????????.");
		}
		
		// 2021-10-20 SMS ??????
		int smsResult = 0;
		boolean smsCheck = false;
		SmsDomain smsDomain = new SmsDomain();
		smsDomain.setTranCallback("0220110770");
		smsDomain.setTranStatus("1");
		smsDomain.setTranEtc1("10070");
		//smsDomain.setTranPhone("01031672126");
		smsDomain.setTranPhone(statCheck.getPlCellphone());
		
		// API????????????
		boolean apiCheck = false;
		
		// ?????????
		if("10".equals(newApplyDomain.getPlStat())){
			apiCheck = true;
			
			BatchDomain batchDomain = new BatchDomain();
			JSONObject jsonParam 	= new JSONObject();
			jsonParam.put("master_seq", statCheck.getMasterSeq());
			jsonParam.put("pre_lc_num", statCheck.getPreLcNum());
			
			batchDomain.setScheduleName("preLoanDel");
			batchDomain.setParam(jsonParam.toString());
			
			if("1".equals(statCheck.getPlClass())) {
				batchDomain.setProperty01("1");
			}else {
				batchDomain.setProperty01("2");
			}
			
			batchService.insertBatchPlanInfo(batchDomain);
			// ???????????? -> ???????????? ?????????(?????????)
			apiCheck = true;
			newApplyDomain.setPlRegStat("1");
			
		}else if("5".equals(newApplyDomain.getPlStat())) { // ????????????
			apiCheck = true;
			
			BatchDomain batchDomain = new BatchDomain();
			JSONObject jsonParam 	= new JSONObject();
			jsonParam.put("master_seq", statCheck.getMasterSeq());
			jsonParam.put("pre_lc_num", statCheck.getPreLcNum());
			
			batchDomain.setScheduleName("preLoanDel");
			batchDomain.setParam(jsonParam.toString());
			if("1".equals(statCheck.getPlClass())) {
				batchDomain.setProperty01("1");
			}else {
				batchDomain.setProperty01("2");
			}
			batchService.insertBatchPlanInfo(batchDomain);
			// ???????????? -> ????????????
			apiCheck = true;
			newApplyDomain.setPlRegStat("1");
			
			String smsMsg = "";
			
			if("1".equals(statCheck.getPlClass())) {
				smsMsg += statCheck.getPlMName()+"?????? ??????????????? ????????? ??????/???????????? ??????????????? ??????????????????.";
			}else {
				smsMsg += statCheck.getPlMerchantName()+"????????? ??????????????? ????????? ??????/???????????? ??????????????? ??????????????????.";
			}
			smsDomain.setTranMsg(smsMsg);
			smsCheck = true;
			
		}else if("9".equals(newApplyDomain.getPlStat()) && "2".equals(newApplyDomain.getPlRegStat())) { // ????????????
			ApiDomain apiDomain = new ApiDomain();
			if("1".equals(statCheck.getPlClass())) {
				// 2021-08-11 ?????????????????? ??? -> ?????? ????????? ????????? ?????? ??????????????? ??? ?????? + ??????????????? ??????????????? Y?????? ??????
				if(statCheck.getCorpUserYn().equals("Y")) {
					UserDomain user = new UserDomain();
					user.setPlMerchantNo(statCheck.getPlMerchantNo());
					user.setPlProduct(statCheck.getPlProduct());

					NewUserDomain newUser = new NewUserDomain();
					newUser.setPlMerchantNo(statCheck.getPlMerchantNo());
					newUser.setPlProduct(statCheck.getPlProduct());
					
					
					int corpCheck = applyRepository.applyNewCorpStatCheck(newUser);
					int corpPassCheck 	= corpService.corpPassCheck(user);
					if(corpCheck == 0 && corpPassCheck == 0) {
						return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????? ????????????\n??????????????? ??????????????? ????????????(Y/N)??? ????????? ?????????.");
					}
				}
				
				if(kfbApiApply) {
					if("Y".equals(statCheck.getPreRegYn())) {
						// ??????????????? ?????? ??????????????? insert ??? ?????????????????? ??????
						BatchDomain batchDomain = new BatchDomain();
						JSONObject jsonParam 	= new JSONObject();
						
						jsonParam.put("master_seq", statCheck.getMasterSeq());
						jsonParam.put("pre_lc_num", statCheck.getPreLcNum());
						
						batchDomain.setScheduleName("loanReg");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01("1");
						batchService.insertBatchPlanInfo(batchDomain);
						// ???????????? -> ???????????? -> ????????????
						apiCheck = true;
						smsDomain.setTranMsg(statCheck.getPlMName()+"?????? ??????????????? ????????? ??????????????? ?????????????????????.");
						
					}else {
						// ???????????? -> ????????????
						apiCheck = true;
						newApplyDomain.setPlRegStat("2");
						newApplyDomain.setPlStat("9");
						smsDomain.setTranMsg(statCheck.getPlMName()+"?????? ??????????????? ????????? ??????????????? ????????????????????? ?????????????????? ??????????????????.");
					}
					
					smsCheck = true;
					
				}else {
					// ???????????? -> ????????????
					apiCheck = true;
					newApplyDomain.setPlRegStat("2");
					newApplyDomain.setPlStat("9");
				}
				
			}else if("2".equals(statCheck.getPlClass())){
				if("01".equals(statCheck.getPlProduct()) || "05".equals(statCheck.getPlProduct())) {
					if(kfbApiApply) {
						if("Y".equals(statCheck.getPreRegYn())) {
							// ??????????????? ?????? ??????????????? insert ??? ?????????????????? ??????
							BatchDomain batchDomain = new BatchDomain();
							JSONObject jsonParam 	= new JSONObject();
							
							jsonParam.put("master_seq", statCheck.getMasterSeq());
							jsonParam.put("pre_corp_lc_num", statCheck.getPreLcNum());
							
							batchDomain.setScheduleName("loanReg");
							batchDomain.setParam(jsonParam.toString());
							batchDomain.setProperty01("2");
							batchService.insertBatchPlanInfo(batchDomain);
							// ???????????? -> ???????????? -> ????????????
							apiCheck = true;
							smsDomain.setTranMsg(statCheck.getPlMerchantName()+"????????? ??????????????? ????????? ??????????????? ?????????????????????.");
							
						}else {
							// ???????????? -> ????????????
							apiCheck = true;
							newApplyDomain.setPlRegStat("2");
							newApplyDomain.setPlStat("9");
							smsDomain.setTranMsg(statCheck.getPlMerchantName()+"????????? ??????????????? ????????? ??????????????? ????????????????????? ?????????????????? ??????????????????.");
						}
						
						smsCheck = true;
					}else {
						// ???????????? -> ????????????
						apiCheck = true;
						newApplyDomain.setPlRegStat("2");
						newApplyDomain.setPlStat("9");
					}
				}else {
					//TM ????????? ????????????
					newApplyDomain.setPlRegStat("2");
					newApplyDomain.setPlStat("9");
					apiCheck = true;
					
				}
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "????????? ????????? ?????????????????????.[PL_CLASS]");
			}
		}else{
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
		}
		
		if(apiCheck) {
			//????????? ?????? ??????
			NewUserDomain param = new NewUserDomain();
			param.setMasterSeq(newApplyDomain.getMasterSeq());
			userRepo.insertNewUserHistory(param);
			
			//????????? ?????? ??????
			int result = applyRepository.updateNewApplyPlStat(newApplyDomain);
			
			// 2021-10-20 SMS??????
			if(smsApply && smsCheck) {
				smsResult = smsRepository.sendSms(smsDomain);
			}else {
				smsResult = 1;
			}
			
			if(smsResult > 0 && result > 0) {
				// ?????????????????????
				applyRepository.insertNewMasterStep(newApplyDomain);
				
				//-------------- ????????? ????????? ?????? ??????(2021.12.22) : S --------------
				if("10".equals(newApplyDomain.getPlStat())) {
					StatsDomain sParam 	= new StatsDomain();
					String plClass 		= statCheck.getPlClass();
					String uKey 		= "";
					
					if("1".equals(plClass)) {
						uKey = statCheck.getPlMZId();
					}else {
						uKey = statCheck.getPlMerchantNo();
					}
					
					sParam.setPlProduct(statCheck.getPlProduct());
					sParam.setPlClass(plClass);
					sParam.setCorpUserYn(statCheck.getCorpUserYn());
					sParam.setName(statCheck.getPlMName());
					sParam.setUKey(uKey);
					statsService.saveInaqInfoForStats(sParam);
				}
				//-------------- ????????? ????????? ?????? ??????(2021.12.22) : E --------------
				
				return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "?????????????????????.");
			}else {
				return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", responseMsg, "API????????? ?????????????????????.");
		}
	}

	//????????? ?????? ??? ?????? > ?????????????????? ??????
	@Transactional
	public ResponseMsg insertNewApplyCheck(ApplyCheckDomain applyCheckDomain){
		int result = applyRepository.insertNewApplyCheck(applyCheckDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	//????????? ?????? ??? ?????? > ?????????????????? ??????
	@Transactional
	public ResponseMsg deleteNewApplyCheck(ApplyCheckDomain applyCheckDomain){
		int result = applyRepository.deleteNewApplyCheck(applyCheckDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	//????????? ?????? ??? ?????? > ???????????????
	@Transactional
	public ResponseMsg applyNewCheck(NewApplyDomain newApplyDomain){
		
		int result = applyRepository.applyNewcheck(newApplyDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	//????????? ?????? ??? ?????? > ???????????????
	@Transactional
	public ResponseMsg applyNewAdminCheck(NewApplyDomain newApplyDomain){
		
		int result = applyRepository.applyNewAdmincheck(newApplyDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	
	//????????? ?????? ??? ?????? > ????????? ??????
	@Transactional
	public ResponseMsg newAppDateHold(NewApplyDomain newApplyDomain){
		// ?????????????????????
		applyRepository.insertNewMasterStep(newApplyDomain);
		int result = applyRepository.newAppDateHold(newApplyDomain);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	
	
	//???????????????????????????
	@Transactional
	public ResponseMsg prevNewRegCheck(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		NewApplyDomain statCheck = applyRepository.getNewApplyDetail(newApplyDomain);
		return new ResponseMsg(HttpStatus.OK, "fail", "API????????? ?????????????????????.");
	}
	
	
	
	
	
	
	//????????? ?????? ??? ?????? > ??????????????????
	@Transactional
	public ResponseMsg checkboxNewUpdatePlStat(NewApplyDomain newApplyDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		int[] masterSeqArr 	= newApplyDomain.getMasterSeqArr();
		boolean applyCheck = false;
		String applyCheckMessage = "";
		
		List<NewApplyDomain> applyList = new ArrayList<NewApplyDomain>();
		
		for(int i = 0; i < masterSeqArr.length; i++) {
			int applyCheckCnt = 0;
			NewApplyDomain resultDomain = new NewApplyDomain();
			resultDomain.setMasterSeq(masterSeqArr[i]);
			NewApplyDomain statCheck = applyRepository.getNewApplyDetail(resultDomain);
			if(statCheck == null) {
				applyCheckMessage = i+1+"?????? ???????????? ????????? ?????????.";
				applyCheck = true;
				break;
			}
			
			if("Y".equals(statCheck.getChkYn())) {
				applyCheckCnt++;
			}
			
			if("Y".equals(statCheck.getAdminChkYn())) {
				applyCheckCnt++;
			}
			
			if(applyCheckCnt == 0) {
				applyCheckMessage = i+1+"?????? ????????? ?????? ????????? ??????????????? ????????? ?????????.";
				applyCheck = true;
				break;				
			}
			
			/*
			//??????????????? ????????? ??????
			if(StringUtils.isEmpty(statCheck.getEmail())) {
				applyCheckMessage = i+1+"?????? ???????????? ????????? ?????????.";
				applyCheck = true;
				break;
			}
			*/
			
			/*
			
			// ??????????????? SMS ??????
			if(StringUtils.isEmpty(statCheck.getPlCellphone())) {
				applyCheckMessage = i+1+"?????? ?????????????????? ????????? ?????????.";
				applyCheck = true;
				break;
			}
			
			*/
			
			//??????????????? ???????????????????????? ??????
			if(!"2".equals(statCheck.getPlStat())) {
				applyCheckMessage = i+1+"?????? ??????????????? ????????? ?????????.";
				applyCheck = true;
				break;
			}
			
			// 2021-08-11 ?????????????????? ??? -> ?????? ????????? ????????? ?????? ??????????????? ??? ?????? + ??????????????? ??????????????? Y?????? ??????
			if(statCheck.getCorpUserYn().equals("Y")) {
				UserDomain user = new UserDomain();
				user.setPlMerchantNo(statCheck.getPlMerchantNo());
				user.setPlProduct(statCheck.getPlProduct());
				
				NewUserDomain newUser = new NewUserDomain();
				newUser.setPlMerchantNo(statCheck.getPlMerchantNo());
				newUser.setPlProduct(statCheck.getPlProduct());
				
				int corpCheck = applyRepository.applyNewCorpStatCheck(newUser);
				int corpPassCheck 	= corpService.corpPassCheck(user);
				if(corpCheck == 0 && corpPassCheck == 0) {
					applyCheckMessage = i+1+"?????? ????????? ???????????? ????????? ????????????\n??????????????? ??????????????? ????????????(Y/N)??? ????????? ?????????.";
					applyCheck = true;
					break;
				}
			}
			
			applyList.add(statCheck);
		}
		
		// ????????? ??????
		if(applyCheck) {
			responseMsg.setMessage(applyCheckMessage); 
			return responseMsg;
		}else {
			responseMsg.setData(applyList);
			return responseMsg;
		}
	}
	
	
	// ????????? ??????
	public int applyNewSendEmail(List<EmailDomain> resultEmailDomain) {
		int emailResult = 0;
		for(int i=0; i<resultEmailDomain.size(); i++) {
			EmailDomain emailDomain = new EmailDomain();
			emailDomain.setName("??????????????????");
			emailDomain.setEmail(resultEmailDomain.get(i).getEmail());
			emailDomain.setInstId(resultEmailDomain.get(i).getInstId());
			emailDomain.setSubsValue(resultEmailDomain.get(i).getSubsValue());
			emailRepository.sendEmail(emailDomain);
			emailResult++;
		}
		
		if(resultEmailDomain.size() != emailResult) {
			return -1;
		}else {
			return emailResult;
		}
	}
	
	// sms ??????
	public int applyNewSendSms(List<SmsDomain> resultSmsDomain) {
		int smsResult = 0;
		for(int i=0; i<resultSmsDomain.size(); i++) {
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("????????????");
			smsDomain.setTranPhone(resultSmsDomain.get(i).getTranPhone());
			smsDomain.setTranStatus("1");
			smsDomain.setTranMsg(resultSmsDomain.get(i).getTranMsg());
			smsDomain.setTranEtc1(resultSmsDomain.get(i).getTranEtc1());
			smsRepository.sendSms(smsDomain);
			smsResult++;
		}
		
		if(resultSmsDomain.size() != smsResult) {
			return -1;
		}else {
			return smsResult;
		}
	}
	
	
	
	
	//????????? ?????? ??? ?????? > ??????????????????
	@Transactional
	public ResponseMsg updateNewApplyListPlStat(List<NewApplyDomain> applyDomainList) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		// API????????????
		List<EmailDomain> resultEmail = new ArrayList<EmailDomain>();
		for(NewApplyDomain tmp : applyDomainList) {
			EmailDomain emailDomain = new EmailDomain();
			emailDomain.setEmail(tmp.getEmail());
			// API????????????
			boolean apiCheck = false;
			KfbApiDomain kfbApiDomain = new KfbApiDomain();
			NewApplyDomain resultDomain = new NewApplyDomain();
			
			if("2".equals(tmp.getPlStat()) && "1".equals(tmp.getPlRegStat()) && "N".equals(tmp.getPreRegYn())) {
				
				// ??????????????? ?????? ??????
				emailDomain.setInstId("142");
				emailDomain.setSubsValue(tmp.getMasterToId());
				resultDomain.setPlStat("9");
				resultDomain.setPlRegStat("2");
				resultEmail.add(emailDomain);
				apiCheck = true;
				
			}else if("2".equals(tmp.getPlStat()) && "1".equals(tmp.getPlRegStat()) && "Y".equals(tmp.getPreRegYn())) {
				// ??????????????? ?????? ??????????????? ??????????????? ??????(???????????? / ??????)
				emailDomain.setInstId("143");
				emailDomain.setSubsValue(tmp.getMasterToId());
				resultEmail.add(emailDomain);
				resultDomain.setPlStat("9");
				resultDomain.setPlRegStat("3");
				
				if(kfbApiApply) {
					// ???????????? 3, 6??? ??????
					String prdCheck = tmp.getPlProduct();
					String lcNum = "";
					String conNum = "";
					// ??????????????? ???????????? ???????????? ??????
					
					UserDomain userDomain = new UserDomain();
					if("01".equals(prdCheck) || "05".equals(prdCheck)) {
						
						// 2021-06-25 ??????????????? API ?????? - ??????
						String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
						JSONObject jsonParam = new JSONObject();
						String plClass = tmp.getPlClass();
						if("1".equals(plClass)) {
							jsonParam.put("pre_lc_num", tmp.getPreLcNum());
							responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", plClass, "Y");				
						}else {
							jsonParam.put("pre_corp_lc_num", tmp.getPreLcNum());
							responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", plClass, "Y");
						}
						
						if("success".equals(responseMsg.getCode())) {
							JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
							
							if("1".equals(tmp.getPlClass())) {
								lcNum = responseJson.getString("lc_num");
							}else {
								lcNum = responseJson.getString("corp_lc_num");
							}
							
							if(StringUtils.isEmpty(lcNum)) {
								
								System.out.println("########### apply return222222222222222");
								
								responseMsg.setMessage("???????????? ???????????? ???????????? ??????.\n???????????? ????????? ?????????.");
								return responseMsg;
								//return new ResponseMsg(HttpStatus.OK, "fail", "???????????? ???????????? ???????????? ??????.\n???????????? ????????? ?????????.");
							}
							
							// ???????????? ????????????[??????]
							// ????????? ?????????????????? ?????????????????? ????????? 1:N??? ??????????????? ??????
							// ????????? KEY??? ??????????????? ????????? ??????????????? ????????? ???????????? ?????? ??????????????? ???????????????????????
							// ??????????????????????????? ??????????
							// ???????????? ??????????????????????????? ????????? ??????????????? - ??????
							
							JSONObject jsonObj = new JSONObject();
							JSONArray conArr = responseJson.getJSONArray("con_arr");
							// ????????????????????????(?????????????????? ????????? ??????)
							String comCode = tmp.getComCode();
							for(int j=0; j<conArr.length(); j++){
								jsonObj = conArr.getJSONObject(j);
								String loanType = jsonObj.getString("loan_type");
								String finCode = jsonObj.getString("fin_code");
								// ????????? ???????????????????????? ??? ??????????????? ????????????(????????????)??? ????????? ????????? ??????(?????????, ?????????????????????????????? ?????? ????????????)
								if(loanType.equals(prdCheck) && finCode.equals(comCode)) {
									conNum = jsonObj.getString("con_num");
								}
							}
							
							if(StringUtils.isEmpty(conNum)) {
								responseMsg.setMessage("??????????????????????????? ?????????????????????.\\n???????????? ????????? ?????????.");
								return responseMsg;
								//return new ResponseMsg(HttpStatus.OK, "success", "??????????????????????????? ?????????????????????.\n???????????? ????????? ?????????.");
							}
							
							userDomain.setMasterSeq(tmp.getMasterSeq());
							userDomain.setPlRegistNo(lcNum);
							userDomain.setConNum(conNum);
							int updateCnt = kfbApiRepository.updateKfbApiByUserInfo(userDomain);
							if(updateCnt > 0) {
								apiCheck = true;
							}else {
								responseMsg.setMessage("API?????? ??? ??????????????? ?????? ??????\\n???????????? ????????? ?????????.");
								return responseMsg;
								//return new ResponseMsg(HttpStatus.OK, "success", "API?????? ??? ??????????????? ?????? ??????\n???????????? ????????? ?????????.");
							}
						}else {
							
							JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
							responseMsg.setData(null);
							if(responseJson.getString("res_code").equals("401")) {
								responseMsg.setMessage("???????????? ?????? [CODE :: 401]");
							}else if(responseJson.getString("res_code").equals("403")) {
								responseMsg.setMessage("??????????????? ?????? ????????? ?????? [CODE :: 403]");
							}else if(responseJson.getString("res_code").equals("404")) {
								responseMsg.setMessage("?????? URI ?????? [CODE :: 404]");
							}else if(responseJson.getString("res_code").equals("405")) {
								responseMsg.setMessage("???????????? ?????? ????????? ?????? [CODE :: 405]");
							}else if(responseJson.getString("res_code").equals("406")) {
								responseMsg.setMessage("JSON?????? ?????? ?????? [CODE :: 406]");
							}else if(responseJson.getString("res_code").equals("421")) {
								responseMsg.setMessage("???????????? ?????? ?????? [CODE :: 421]");
							}else if(responseJson.getString("res_code").equals("422")) {
								responseMsg.setMessage("??? ?????? ????????? ?????? ??? [CODE :: 422]");
							}else if(responseJson.getString("res_code").equals("423")) {
								responseMsg.setMessage("??????????????? ?????? ?????? [CODE :: 423]");
							}else if(responseJson.getString("res_code").equals("424")) {
								responseMsg.setMessage("?????? ????????? ?????? [CODE :: 424]");
							}else if(responseJson.getString("res_code").equals("425")) {
								responseMsg.setMessage("??? ?????? ?????? ??? ????????? [CODE :: 425]");
							}else if(responseJson.getString("res_code").equals("426")) {
								responseMsg.setMessage("?????? ??? ????????? [CODE :: 426]");
							}else if(responseJson.getString("res_code").equals("427")) {
								responseMsg.setMessage("????????? ?????? ?????? [CODE :: 427]");
							}else if(responseJson.getString("res_code").equals("428")) {
								responseMsg.setMessage("????????? ???????????? ?????? [CODE :: 428]");
							}
							System.out.println("########### apply return111111111111");
							System.out.println(responseMsg);
							return responseMsg;
						}
					}else {
						apiCheck = true;
					}
				}else {
					apiCheck = true;
				}
				
			}else{
				responseMsg.setMessage("??????????????? ???????????? ????????????.\\n???????????? ??? ?????? ????????? ?????????.");
				return responseMsg;
				//return new ResponseMsg(HttpStatus.OK, "success", "??????????????? ???????????? ????????????.\n???????????? ??? ?????? ????????? ?????????.");
			}
			
			if(apiCheck) {
				//????????? ?????? ??????
				NewUserDomain param = new NewUserDomain();
				param.setMasterSeq(tmp.getMasterSeq());
				resultDomain.setMasterSeq(tmp.getMasterSeq());
				userRepo.insertNewUserHistory(param);
				
				//????????? ?????? ??????
				int updateResult = applyRepository.updateNewApplyPlStat(resultDomain);
				if(updateResult > 0) {
					// ?????????????????????
					applyRepository.insertNewMasterStep(tmp);
					//return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "?????????????????????.");
				}else {
					responseMsg.setMessage("????????? ??????????????? ????????? ?????????????????????.");
					return responseMsg;
					//return new ResponseMsg(HttpStatus.OK, "success", "????????? ?????????????????????.");
				}
			}else {
				responseMsg.setMessage("API????????? ?????????????????????.");
				return responseMsg;
				//return new ResponseMsg(HttpStatus.OK, "success", responseMsg, "API????????? ?????????????????????.");
			}
			responseMsg.setData(resultEmail);
		}
		return responseMsg;
	}
	
	
	
	//????????? ?????? > ????????????
	@Transactional
	public int checkboxNewImproveUpdate(NewApplyDomain newApplyDomain){
		int result 			= 0;
		int[] masterSeqArr 	= newApplyDomain.getMasterSeqArr();
		
		for(int i = 0;i < masterSeqArr.length;i++) {
			NewApplyDomain applyResultDomain = new NewApplyDomain();
			applyResultDomain.setMasterSeq(masterSeqArr[i]);
			NewUserDomain param = new NewUserDomain();
			param.setMasterSeq(applyResultDomain.getMasterSeq());
			userRepo.insertNewUserHistory(param);
			int updateResult = applyRepository.updateNewApplyImprovePlStat(applyResultDomain);
			if(updateResult > 0) {
				applyRepository.insertNewMasterStep(applyResultDomain);
			}else {
				result = -1;
				break;
			}
			result++;
		}
		return result;
	}
	
	
	
	//????????? ?????? ??????
	@Transactional
	public int updatePlRegStat(SearchDomain searchDomain) {
		int updateResult = searchRepository.updatePlRegStat(searchDomain);
		if(updateResult > 0) {
			applyRepository.insertUserStepHistory(searchDomain);
		}
		return updateResult;
	}
	
}
