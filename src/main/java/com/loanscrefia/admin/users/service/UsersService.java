package com.loanscrefia.admin.users.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.users.domain.CorpUsersExcelDomain;
import com.loanscrefia.admin.users.domain.IndvUsersExcelDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.admin.users.repository.UsersRepository;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.email.repository.EmailRepository;
import com.loanscrefia.common.common.sms.domain.SmsDomain;
import com.loanscrefia.common.common.sms.repository.SmsRepository;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.member.user.service.NewUserService;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.repository.BatchRepository;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;
import com.loanscrefia.util.UtilMask;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private SmsRepository smsRepository;
	
	//????????? ????????????
	@Value("${email.apply}")
	public boolean emailApply;
	
	//????????? ????????????
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//SMS ????????????
	@Value("${sms.apply}")
	public boolean smsApply;
	
	@Autowired private UtilFile utilFile;
	@Autowired private UtilExcel<T> utilExcel;
	@Autowired private NewUserService newUserService;
	
	//???????????? ??????
	@Value("${upload.filePath}")
	public String uPath;
	
	@Autowired
	private BatchRepository batchRepository;
	
	// ?????? ???????????? ????????? ??????
	@Transactional(readOnly=true)
	public List<UsersDomain> selectIndvUsersList(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		// ???????????? ??? ???????????? ????????? ??? ??????
		if(StringUtils.isNotEmpty(usersDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				usersDomain.setPlMerchantNo(CryptoUtil.encrypt(usersDomain.getPlMerchantNo()));
			}else {
				usersDomain.setPlMerchantNo(usersDomain.getPlMerchantNo());
			}
		}
		if(StringUtils.isNotEmpty(usersDomain.getPlMZId())) {
			if(cryptoApply) {
				usersDomain.setPlMZId(CryptoUtil.encrypt(usersDomain.getPlMZId()));
			}else {
				usersDomain.setPlMZId(usersDomain.getPlMZId());
			}
		}
		
		List<UsersDomain> resultList = usersRepository.selectIndvUsersList(usersDomain);
		String plMZId = "";
		for(UsersDomain list : resultList) {
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				if(cryptoApply) {
					plMZId 	= CryptoUtil.decrypt(list.getPlMZId());
				}else {
					plMZId 	= list.getPlMZId();
					usersDomain.setPlMZId(plMZId);
				}
				if(plMZId != null) {
					if(!"false".equals(usersDomain.getIsPaging())) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId = plMZId.substring(0, 6) + "-" + plMZId.substring(6);
				}
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
		
		return resultList;
	}
	
	
	// ?????? ???????????? ??????
	@Transactional(readOnly=true)
	public UsersDomain getIndvUsersDetail(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		UsersDomain result = usersRepository.getIndvUsersDetail(usersDomain);
		String plMZId = "";
		if(StringUtils.isNotEmpty(result.getPlMZId())) {
			if(cryptoApply) {
				plMZId 	= CryptoUtil.decrypt(result.getPlMZId());
			}else {
				plMZId 	= result.getPlMZId();
				usersDomain.setPlMZId(plMZId);
			}

			if(plMZId != null) {
				plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			}
			result.setPlMZId(plMZId);
		}
		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(result.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(result.getPlMerchantNo()));
			}else {
				merchantNo.append(result.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			result.setPlMerchantNo(merchantNo.toString());
		}
		
		return result;
	}
	
	// ???????????? ???????????? ??????
	@Transactional
	public ResponseMsg updateIndvUserDis(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		
		UsersDomain usersDomain1 = new UsersDomain();
		usersDomain1.setUserSeq(usersDomain.getUserSeq());
		usersDomain1.setDisCd("7");
		usersDomain1.setDisVal(usersDomain.getDis1());
		usersRepository.updateIndvUserDis(usersDomain1);
		
		UsersDomain usersDomain2 = new UsersDomain();
		usersDomain2.setUserSeq(usersDomain.getUserSeq());
		usersDomain2.setDisCd("8");
		usersDomain2.setDisVal(usersDomain.getDis2());
		usersRepository.updateIndvUserDis(usersDomain2);
		
		return responseMsg;
	}
	
	// ????????? ?????? ??????
	@Transactional
	public ResponseMsg loginStopUpdate(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		int[] userSeqArr 	= usersDomain.getUserSeqArr();
		for(int i = 0; i < userSeqArr.length; i++) {
			UsersDomain resultDomain = new UsersDomain();
			resultDomain.setUserSeq(userSeqArr[i]);
			usersRepository.loginStopUpdate(resultDomain);
		}
		
		return responseMsg;
	}
	
	
	//???????????? ???????????? ?????? ?????????
	@Transactional
	public ResponseMsg indvUsersDisExcelUpload(MultipartFile[] files, UsersDomain usersDomain) throws IOException{
		
		//?????? ??????
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		//???????????? ??????(?????????????????? path??? ?????? ??? ????????? ?????? ??????)
		Map<String, Object> ret = utilFile.setPath("dis")
				.setFiles(files)
				.setExt("excel")
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//???????????? ????????? ????????????
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//?????? ?????????
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam2(usersDomain.getPlClass()).disUpload(uPath, filePath, fileSaveNm, fileExt, IndvUsersExcelDomain.class);
				
				//?????? ????????? ??? ???????????????
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				if(errorMsg != null && !errorMsg.equals("")) {
					//??????????????? ??????
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//??????????????? ?????? -> ??????
					int insertResult = 0;
					for(int c=0; c<excelResult.size(); c++) {
						
						Map<String, Object> paramResult1 = excelResult.get(c);
						UsersDomain usersDomain1 = new UsersDomain();
						usersDomain1.setUserName(paramResult1.get("A").toString());
						usersDomain1.setPlMZId(paramResult1.get("B").toString());
						usersDomain1.setDisCd("7");
						usersDomain1.setDisVal(paramResult1.get("E").toString());
						usersRepository.indvUsersDisExcelUpload(usersDomain1);
						
						Map<String, Object> paramResult2 = excelResult.get(c);
						UsersDomain usersDomain2 = new UsersDomain();
						usersDomain2.setUserName(paramResult2.get("A").toString());
						usersDomain2.setPlMZId(paramResult2.get("B").toString());
						usersDomain2.setDisCd("8");
						usersDomain2.setDisVal(paramResult1.get("F").toString());
						usersRepository.indvUsersDisExcelUpload(usersDomain2);
						insertResult++;
					}
					// ???????????????
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "???????????? ???????????? ?????????????????????.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
	}
	
	
	
	
	
	
	
	

	
	// ?????? ???????????? ????????? ??????
	@Transactional(readOnly=true)
	public List<UsersDomain> selectCorpUsersList(UsersDomain usersDomain){
		UtilMask mask = new UtilMask();
		// ???????????? ??? ???????????? ????????? ??? ??????
		if(StringUtils.isNotEmpty(usersDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				usersDomain.setPlMerchantNo(CryptoUtil.encrypt(usersDomain.getPlMerchantNo()));
			}else {
				usersDomain.setPlMerchantNo(usersDomain.getPlMerchantNo());
			}
		}
		if(StringUtils.isNotEmpty(usersDomain.getPlMZId())) {
			if(cryptoApply) {
				usersDomain.setPlMZId(CryptoUtil.encrypt(usersDomain.getPlMZId()));
			}else {
				usersDomain.setPlMZId(usersDomain.getPlMZId());
			}
		}
		
		List<UsersDomain> resultList = usersRepository.selectCorpUsersList(usersDomain);
		String plMZId = "";
		for(UsersDomain list : resultList) {
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				if(cryptoApply) {
					plMZId 	= CryptoUtil.decrypt(list.getPlMZId());
				}else {
					plMZId 	= list.getPlMZId();
					usersDomain.setPlMZId(plMZId);
				}
				if(plMZId != null) {
					if(!"false".equals(usersDomain.getIsPaging())) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId = plMZId.substring(0, 6) + "-" + plMZId.substring(6);
				}
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
		
		return resultList;
	}
	
	
	// ?????? ???????????? ??????
	@Transactional(readOnly=true)
	public UsersDomain getCorpUsersDetail(UsersDomain usersDomain){
		UtilMask mask = new UtilMask();
		UsersDomain result = usersRepository.getCorpUsersDetail(usersDomain);
		String plMZId = "";
		if(StringUtils.isNotEmpty(result.getPlMZId())) {
			if(cryptoApply) {
				plMZId 	= CryptoUtil.decrypt(result.getPlMZId());
			}else {
				plMZId 	= result.getPlMZId();
				usersDomain.setPlMZId(plMZId);
			}

			if(plMZId != null) {
				plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			}
			
			result.setPlMZId(plMZId);
		}
		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(result.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(result.getPlMerchantNo()));
			}else {
				merchantNo.append(result.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			result.setPlMerchantNo(merchantNo.toString());
		}
		
		return result;
	}
	
	// ???????????? ?????? ????????????
	@Transactional
	public ResponseMsg usersCorpApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		int result = usersRepository.usersCorpApply(usersDomain);
		if(result <= 0) {
			responseMsg.setMessage("?????????????????????");
			responseMsg.setCode("fail");
			return responseMsg;
		}else {
			UsersDomain userInfo = usersRepository.getCorpUsersDetail(usersDomain);
			
			//BO ????????? FO??? ??????
			NewUserDomain param = new NewUserDomain();
			param.setPlClass("2");
			param.setPlMerchantNo(userInfo.getPlMerchantNo());
			param.setUserSeq(usersDomain.getUserSeq());
			newUserService.switchPrevContractToFo(param);
			
			// 2021-09-16 ???????????? ????????? ????????? ?????? ?????? ??????
			
			/*
			
			if(emailApply) {
				UsersDomain usersResult = usersRepository.getUsersDetail(usersDomain);
				int emailResult = 0;
				EmailDomain emailDomain = new EmailDomain();
				emailDomain.setName("??????????????????");
				emailDomain.setEmail(usersDomain.getEmail());
				emailDomain.setInstId("139");
				emailDomain.setSubsValue(usersResult.getUserId());
				emailResult = emailRepository.sendEmail(emailDomain);
				if(emailResult == 0) {
					responseMsg.setMessage("??????????????? ?????????????????????");
					responseMsg.setCode("fail");
					return responseMsg;
				}
			}
			
			*/
			return responseMsg;
		}
		
	}
	
	// ???????????? ?????? ???????????????
	@Transactional
	public ResponseMsg usersCorpTempApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		int result = usersRepository.usersCorpTempApply(usersDomain);
		if(result <= 0) {
			responseMsg.setMessage("?????????????????????");
			responseMsg.setCode("fail");
			return responseMsg;
		}else {
			// 2021-09-16 ???????????? ????????? ????????? ?????? ?????? ??????
			
			/*
			
			if(emailApply) {
				UsersDomain usersResult = usersRepository.getUsersDetail(usersDomain);
				int emailResult = 0;
				EmailDomain emailDomain = new EmailDomain();
				emailDomain.setName("??????????????????");
				emailDomain.setEmail(usersDomain.getEmail());
				emailDomain.setInstId("139");
				emailDomain.setSubsValue(usersResult.getUserId());
				emailResult = emailRepository.sendEmail(emailDomain);
				if(emailResult == 0) {
					responseMsg.setMessage("??????????????? ?????????????????????");
					responseMsg.setCode("fail");
					return responseMsg;
				}
			}
			
			*/
			return responseMsg;
		}
		
	}
	
	
	
	// ???????????? ???????????? ??????
	@Transactional
	public ResponseMsg updateCorpUserDis(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		
		UsersDomain usersDomain1 = new UsersDomain();
		usersDomain1.setUserSeq(usersDomain.getUserSeq());
		usersDomain1.setDisCd("9");
		usersDomain1.setDisVal(usersDomain.getDis9());
		usersRepository.updateCorpUserDis(usersDomain1);
		/*
		UsersDomain usersDomain2 = new UsersDomain();
		usersDomain2.setUserSeq(usersDomain.getUserSeq());
		usersDomain2.setDisCd("10");
		usersDomain2.setDisVal(usersDomain.getDis10());
		usersRepository.updateCorpUserDis(usersDomain2);
		
		UsersDomain usersDomain3 = new UsersDomain();
		usersDomain3.setUserSeq(usersDomain.getUserSeq());
		usersDomain3.setDisCd("11");
		usersDomain3.setDisVal(usersDomain.getDis11());
		usersRepository.updateCorpUserDis(usersDomain3);
		*/
		UsersDomain usersDomain4 = new UsersDomain();
		usersDomain4.setUserSeq(usersDomain.getUserSeq());
		usersDomain4.setDisCd("12");
		usersDomain4.setDisVal(usersDomain.getDis12());
		usersRepository.updateCorpUserDis(usersDomain4);
		
		UsersDomain usersDomain5 = new UsersDomain();
		usersDomain5.setUserSeq(usersDomain.getUserSeq());
		usersDomain5.setDisCd("13");
		usersDomain5.setDisVal(usersDomain.getDis13());
		usersRepository.updateCorpUserDis(usersDomain5);
		
		return responseMsg;
	}
	
	// ??????????????? ???????????? ??????
	@Transactional
	public ResponseMsg updatePassYn(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		int result = usersRepository.updatePassYn(usersDomain);
		if(result <= 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
		}
		return responseMsg;
	}
	
	//???????????? ???????????? ?????? ?????????
	@Transactional
	public ResponseMsg corpUsersDisExcelUpload(MultipartFile[] files, UsersDomain usersDomain) throws IOException{
		
		//?????? ??????
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		//???????????? ??????(?????????????????? path??? ?????? ??? ????????? ?????? ??????)
		Map<String, Object> ret = utilFile.setPath("dis")
				.setFiles(files)
				.setExt("excel")
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//???????????? ????????? ????????????
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//?????? ?????????
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam2(usersDomain.getPlClass()).disUpload(uPath, filePath, fileSaveNm, fileExt, CorpUsersExcelDomain.class);
				
				//?????? ????????? ??? ???????????????
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				if(errorMsg != null && !errorMsg.equals("")) {
					//??????????????? ??????
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//??????????????? ?????? -> ??????
					int insertResult = 0;
					for(int c=0; c<excelResult.size(); c++) {
						
						Map<String, Object> paramResult1 = excelResult.get(c);
						UsersDomain usersDomain1 = new UsersDomain();
						usersDomain1.setPlMerchantName(paramResult1.get("C").toString());
						usersDomain1.setPlMerchantNo(paramResult1.get("D").toString());
						usersDomain1.setDisCd("9");
						usersDomain1.setDisVal(paramResult1.get("E").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain1);

						/*
						
						Map<String, Object> paramResult2 = excelResult.get(c);
						UsersDomain usersDomain2 = new UsersDomain();
						usersDomain2.setPlMerchantName(paramResult2.get("C").toString());
						usersDomain2.setPlMerchantNo(paramResult2.get("D").toString());
						usersDomain2.setDisCd("10");
						usersDomain2.setDisVal(paramResult2.get("F").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain2);
						
						Map<String, Object> paramResult3 = excelResult.get(c);
						UsersDomain usersDomain3 = new UsersDomain();
						usersDomain3.setPlMerchantName(paramResult3.get("C").toString());
						usersDomain3.setPlMerchantNo(paramResult3.get("D").toString());
						usersDomain3.setDisCd("11");
						usersDomain3.setDisVal(paramResult3.get("G").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain3);
						
						*/
						
						Map<String, Object> paramResult4 = excelResult.get(c);
						UsersDomain usersDomain4 = new UsersDomain();
						usersDomain4.setPlMerchantName(paramResult4.get("C").toString());
						usersDomain4.setPlMerchantNo(paramResult4.get("D").toString());
						usersDomain4.setDisCd("12");
						usersDomain4.setDisVal(paramResult4.get("F").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain4);
						
						Map<String, Object> paramResult5 = excelResult.get(c);
						UsersDomain usersDomain5 = new UsersDomain();
						usersDomain5.setPlMerchantName(paramResult5.get("C").toString());
						usersDomain5.setPlMerchantNo(paramResult5.get("D").toString());
						usersDomain5.setDisCd("13");
						usersDomain5.setDisVal(paramResult5.get("G").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain5);
						insertResult++;
					}
					// ???????????????
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "???????????? ???????????? ?????????????????????.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "??????????????????.");
	}
	
	
	
	
	
	
	
	
	
	
	// ?????????????????? ????????? ??????
	@Transactional(readOnly=true)
	public List<UsersDomain> selectInactiveList(UsersDomain usersDomain){
		return usersRepository.selectInactiveList(usersDomain);
	}
	
	// ?????????????????? ??????
	@Transactional(readOnly=true)
	public UsersDomain getInactiveDetail(UsersDomain usersDomain){
		return usersRepository.getInactiveDetail(usersDomain);
	}
	
	// ???????????? ?????????
	@Transactional
	public ResponseMsg boInactiveUser(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		int[] userSeqArr 	= usersDomain.getUserSeqArr();
		for(int i = 0; i < userSeqArr.length; i++) {
			UsersDomain resultDomain = new UsersDomain();
			resultDomain.setUserSeq(userSeqArr[i]);
			usersRepository.updateBoInactiveUser(resultDomain);
			usersRepository.deleteBoInactiveUser(resultDomain);
		}
		return responseMsg;
	}
	
	
	
	// ???????????? ?????????????????? ????????? ??????
	@Transactional(readOnly=true)
	public List<UsersDomain> selectUpdateIndvUsersList(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		// ???????????? ??? ???????????? ????????? ??? ??????
		if(StringUtils.isNotEmpty(usersDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				usersDomain.setPlMerchantNo(CryptoUtil.encrypt(usersDomain.getPlMerchantNo()));
			}else {
				usersDomain.setPlMerchantNo(usersDomain.getPlMerchantNo());
			}
		}
		if(StringUtils.isNotEmpty(usersDomain.getPlMZId())) {
			if(cryptoApply) {
				usersDomain.setPlMZId(CryptoUtil.encrypt(usersDomain.getPlMZId()));
			}else {
				usersDomain.setPlMZId(usersDomain.getPlMZId());
			}
		}
		
		List<UsersDomain> resultList = usersRepository.selectUpdateIndvUsersList(usersDomain);
		String plMZId = "";
		for(UsersDomain list : resultList) {
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				if(cryptoApply) {
					plMZId 	= CryptoUtil.decrypt(list.getPlMZId());
				}else {
					plMZId 	= list.getPlMZId();
					usersDomain.setPlMZId(plMZId);
				}
				if(plMZId != null) {
					if(!"false".equals(usersDomain.getIsPaging())) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
				}
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
		
		return resultList;
	}
	
	
	// ???????????? ?????????????????? ??????
	@Transactional(readOnly=true)
	public UsersDomain getUpdateIndvUsersDetail(UsersDomain usersDomain){
		
		UsersDomain result = usersRepository.getUpdateIndvUsersDetail(usersDomain);
		String plMZId = "";
		if(StringUtils.isNotEmpty(result.getPlMZId())) {
			if(cryptoApply) {
				plMZId 	= CryptoUtil.decrypt(result.getPlMZId());
			}else {
				plMZId 	= result.getPlMZId();
			}
			if(plMZId != null) {
				plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			}
			result.setPlMZId(plMZId);
		}
		
		String reqPlMZId = "";
		if(StringUtils.isNotEmpty(result.getReqPlMZId())) {
			if(cryptoApply) {
				reqPlMZId 	= CryptoUtil.decrypt(result.getReqPlMZId());
			}else {
				reqPlMZId 	= result.getReqPlMZId();
			}
			if(reqPlMZId != null) {
				reqPlMZId 		= reqPlMZId.substring(0, 6) + "-" + reqPlMZId.substring(6);
			}
			result.setReqPlMZId(reqPlMZId);
		}
		
		String originPlMZId = "";
		if(StringUtils.isNotEmpty(result.getOriginPlMZId())) {
			if(cryptoApply) {
				originPlMZId 	= CryptoUtil.decrypt(result.getOriginPlMZId());
			}else {
				originPlMZId 	= result.getOriginPlMZId();
			}
			if(originPlMZId != null) {
				originPlMZId 	= originPlMZId.substring(0, 6) + "-" + originPlMZId.substring(6);
			}
			result.setOriginPlMZId(originPlMZId);
		}
		
		return result;
	}
	
	
	// ???????????? ?????????????????? ????????????
	@Transactional
	public ResponseMsg updateIndvUsersStat(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		
		// ???????????? ?????? -> API????????? ?????? ?????? ????????? insert
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setUserSeq(usersDomain.getUserSeq());
		
		// ???????????? ?????? ????????? ??????
		List<NewApplyDomain> resultList = usersRepository.selectUserSeqIndvList(newApplyDomain);
		
		// ?????????????????? ????????????
		UsersDomain usersResult = usersRepository.getUpdateIndvUsersDetail(usersDomain);
		
		if("2".equals(usersDomain.getStat())) {

			String userName = usersResult.getReqUserName();
			String mobileNo = usersResult.getReqMobileNo();
			String ssn = usersResult.getReqPlMZId();
			
			//String userCi = usersResult.getReqUserCi();
			String userCi = usersResult.getUserCi();
			
			if(resultList.size() > 0) {
				
				// ?????????????????? ?????? ?????? insert ??????
				JSONObject ssnJsonParam = new JSONObject();
				if(StringUtils.isNotEmpty(ssn)) {
					ssnJsonParam.put("user_seq", usersResult.getUserSeq());
					ssnJsonParam.put("bef_ssn", usersResult.getPlMZId());
					ssnJsonParam.put("aft_ssn", ssn);
					ssnJsonParam.put("aft_ci", userCi);
					
					BatchDomain ssnBatchDomain = new BatchDomain();
					ssnBatchDomain.setScheduleName("loanSsnUpd");
					ssnBatchDomain.setParam(ssnJsonParam.toString());
					ssnBatchDomain.setProperty01("1"); 													// ??????,?????? ?????????
					ssnBatchDomain.setProperty02(Integer.toString(usersResult.getUserSeq())); 			// ????????? ????????? ???????????? ????????? user_seq
					ssnBatchDomain.setProperty03(Integer.toString(usersDomain.getUserCorpReqSeq()));	// ????????? ????????? ???????????? ????????? ???????????? seq
					ssnBatchDomain.setProperty04("IndvReq");											// ??????????????? ?????????????????? ????????? ????????? param
					batchRepository.insertBatchPlanInfo(ssnBatchDomain);
				}
				
				for(NewApplyDomain result : resultList) {
					//?????? ????????? ??????
					BatchDomain batchDomain = new BatchDomain();
					JSONObject jsonParam = new JSONObject();
					JSONObject jsonArrayParam = new JSONObject();
					JSONArray jsonArray = new JSONArray();
					
					jsonParam.put("user_seq", result.getUserSeq());
					jsonParam.put("master_seq", result.getMasterSeq());
					jsonParam.put("lc_num", result.getPlRegistNo());
					if(StringUtils.isEmpty(userName)) {
						jsonParam.put("name", result.getPlMName());
					}else {
						jsonParam.put("name", userName);
					}
					
					// ??????
					jsonArrayParam.put("con_num", result.getConNum());
					jsonArrayParam.put("con_date", result.getComContDate().replaceAll("-", ""));
					if(StringUtils.isEmpty(mobileNo)) {
						jsonArrayParam.put("con_mobile", result.getPlCellphone());
					}else {
						jsonArrayParam.put("con_mobile", mobileNo);
					}
					jsonArrayParam.put("loan_type", result.getPlProduct());
					jsonArray.put(jsonArrayParam);
					jsonParam.put("con_arr", jsonArray);
					
					batchDomain.setScheduleName("loanUpd");
					batchDomain.setParam(jsonParam.toString());
					batchDomain.setProperty01("1"); //??????,?????? ?????????
					batchDomain.setProperty02(Integer.toString(result.getUserSeq())); 				// ????????? ????????? ???????????? ????????? user_seq
					batchDomain.setProperty03(Integer.toString(usersDomain.getUserIndvReqSeq()));	// ????????? ????????? ???????????? ????????? ???????????? seq
					batchDomain.setProperty04("IndvReq");											// ??????????????? ?????????????????? ????????? ????????? param
					
					batchRepository.insertBatchPlanInfo(batchDomain);
					
				}
			}else {
				
				// ???????????? ??? ???????????? ?????? ??????
				int updResult = usersRepository.updateIndvUsersStat(usersDomain);
				// user?????? update
				int updInfoResult = usersRepository.updateIndvUsersApplyInfo(usersResult);
				
				if(updResult > 0 && updInfoResult > 0) {
					return responseMsg;
				}else {
					return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
				}
			}

			
		}else if("3".equals(usersDomain.getStat())) {
			/*
			// ???????????? ????????? ?????? ????????? ??????
			int smsResult = 0;
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("0220110770");
			smsDomain.setTranStatus("1");
			smsDomain.setTranEtc1("10070");
			smsDomain.setTranPhone(usersResult.getMobileNo());
			String tranMsg = "";
			tranMsg+= usersResult.getUserName()+"?????? ???????????? ????????? ?????????????????????.";
			smsDomain.setTranMsg(tranMsg);
			
			smsResult = smsRepository.sendSms(smsDomain);
			*/
			
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
		int result = usersRepository.updateIndvUsersStat(usersDomain);
		if(result > 0) {
			return responseMsg;
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	// ???????????? ?????????????????? ????????? ??????
	@Transactional(readOnly=true)
	public List<UsersDomain> selectUpdateCorpUsersList(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		// ???????????? ??? ???????????? ????????? ??? ??????
		if(StringUtils.isNotEmpty(usersDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				usersDomain.setPlMerchantNo(CryptoUtil.encrypt(usersDomain.getPlMerchantNo()));
			}else {
				usersDomain.setPlMerchantNo(usersDomain.getPlMerchantNo());
			}
		}
		if(StringUtils.isNotEmpty(usersDomain.getPlMZId())) {
			if(cryptoApply) {
				usersDomain.setPlMZId(CryptoUtil.encrypt(usersDomain.getPlMZId()));
			}else {
				usersDomain.setPlMZId(usersDomain.getPlMZId());
			}
		}
		
		List<UsersDomain> resultList = usersRepository.selectUpdateCorpUsersList(usersDomain);
		String plMZId = "";
		for(UsersDomain list : resultList) {
			if(StringUtils.isNotEmpty(list.getPlMZId())) {
				if(cryptoApply) {
					plMZId 	= CryptoUtil.decrypt(list.getPlMZId());
				}else {
					plMZId 	= list.getPlMZId();
					usersDomain.setPlMZId(plMZId);
				}
				if(plMZId != null) {
					if(!"false".equals(usersDomain.getIsPaging())) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
				}
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
		
		return resultList;
	}
	
	
	// ???????????? ?????????????????? ??????
	@Transactional(readOnly=true)
	public UsersDomain getUpdateCorpUsersDetail(UsersDomain usersDomain){
		UsersDomain result = usersRepository.getUpdateCorpUsersDetail(usersDomain);
		String plMZId = "";
		if(StringUtils.isNotEmpty(result.getPlMZId())) {
			if(cryptoApply) {
				plMZId 	= CryptoUtil.decrypt(result.getPlMZId());
			}else {
				plMZId 	= result.getPlMZId();
			}
			
			if(plMZId != null) {
				plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			}
			
			result.setPlMZId(plMZId);
		}
		
		StringBuilder merchantNo = new StringBuilder();
		if(StringUtils.isNotEmpty(result.getPlMerchantNo())) {
			if(cryptoApply) {
				merchantNo.append(CryptoUtil.decrypt(result.getPlMerchantNo()));
			}else {
				merchantNo.append(result.getPlMerchantNo());
			}
			merchantNo.insert(6, "-");
			result.setPlMerchantNo(merchantNo.toString());
		}
		
		String reqPlMZId = "";
		if(StringUtils.isNotEmpty(result.getReqPlMZId())) {
			if(cryptoApply) {
				reqPlMZId 	= CryptoUtil.decrypt(result.getReqPlMZId());
			}else {
				reqPlMZId 	= result.getReqPlMZId();
			}
			
			if(reqPlMZId != null) {
				reqPlMZId 		= reqPlMZId.substring(0, 6) + "-" + reqPlMZId.substring(6);
			}
			
			result.setReqPlMZId(reqPlMZId);
		}
		
		String originPlMZId = "";
		if(StringUtils.isNotEmpty(result.getOriginPlMZId())) {
			if(cryptoApply) {
				originPlMZId 	= CryptoUtil.decrypt(result.getOriginPlMZId());
			}else {
				originPlMZId 	= result.getOriginPlMZId();
			}
			if(originPlMZId != null) {
				originPlMZId 	= originPlMZId.substring(0, 6) + "-" + originPlMZId.substring(6);
			}
			result.setOriginPlMZId(originPlMZId);
		}
		
		return result;
	}
	
	// ???????????? ?????????????????? ????????????
	@Transactional
	public ResponseMsg updateCorpUsersStat(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "?????????????????????.");
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setUserSeq(usersDomain.getUserSeq());
		
		// ???????????? ?????? ????????? ??????
		List<NewApplyDomain> resultList = usersRepository.selectUserSeqCorpList(newApplyDomain);
		
		// ?????????????????? ????????????
		UsersDomain usersResult = usersRepository.getUpdateCorpUsersDetail(usersDomain);
		
		if("2".equals(usersDomain.getStat())) {
			// ???????????? ?????? -> API????????? ?????? ?????? ????????? insert
			String plMerchantName = usersResult.getReqPlMerchantName();
			String userName = usersResult.getReqUserName();
			// ?????????????????? ????????? 
			String ssn = usersResult.getReqPlMZId();
			String mobileNo = usersResult.getReqMobileNo();
			String userCi = usersResult.getReqUserCi();
			
			if(resultList.size() > 0) {
				
				JSONObject ssnJsonParam = new JSONObject();
				if(StringUtils.isNotEmpty(ssn)) {
					ssnJsonParam.put("user_seq", usersResult.getUserSeq());
					ssnJsonParam.put("bef_ssn", usersResult.getPlMZId());
					ssnJsonParam.put("aft_ssn", ssn);
					ssnJsonParam.put("aft_ci", userCi);
				
					BatchDomain ssnBatchDomain = new BatchDomain();
					ssnBatchDomain.setScheduleName("loanSsnUpd");
					ssnBatchDomain.setParam(ssnJsonParam.toString());
					ssnBatchDomain.setProperty01("2"); 													//??????,?????? ?????????
					ssnBatchDomain.setProperty02(Integer.toString(usersResult.getUserSeq())); 			// ????????? ????????? ???????????? ????????? user_seq
					ssnBatchDomain.setProperty03(Integer.toString(usersDomain.getUserCorpReqSeq()));	// ????????? ????????? ???????????? ????????? ???????????? seq
					ssnBatchDomain.setProperty04("CorpReq");											// ??????????????? ?????????????????? ????????? ????????? param
					batchRepository.insertBatchPlanInfo(ssnBatchDomain);
				}
				
				for(NewApplyDomain result : resultList) {
					//?????? ????????? ??????
					BatchDomain batchDomain = new BatchDomain();
					JSONObject jsonParam = new JSONObject();
					JSONObject jsonArrayParam = new JSONObject();
					JSONArray jsonArray = new JSONArray();
					
					jsonParam.put("user_seq", result.getUserSeq());
					jsonParam.put("master_seq", result.getMasterSeq());
					mobileNo = (StringUtils.isEmpty(mobileNo)) ? 
									(result.getMobileNo() == null) ? "" : result.getMobileNo() : mobileNo;
										
					jsonParam.put("mobile_no", mobileNo);
					jsonParam.put("corp_lc_num", result.getPlRegistNo());
					if(StringUtils.isEmpty(plMerchantName)) {
						jsonParam.put("corp_name", result.getPlMerchantName());
					}else {
						jsonParam.put("corp_name", plMerchantName);
					}
					
					if(StringUtils.isEmpty(userName)) {
						jsonParam.put("corp_rep_name", result.getPlCeoName());
					}else {
						jsonParam.put("corp_rep_name", userName);
					}
					
					// ??????
					jsonArrayParam.put("con_num", result.getConNum());
					jsonArrayParam.put("con_date", result.getComContDate().replaceAll("-", ""));
					jsonArrayParam.put("loan_type", result.getPlProduct());
					
					jsonArray.put(jsonArrayParam);
					jsonParam.put("con_arr", jsonArray);
					
					batchDomain.setScheduleName("loanUpd");
					batchDomain.setParam(jsonParam.toString());
					batchDomain.setProperty01("2");													//??????,?????? ?????????
					batchDomain.setProperty02(Integer.toString(result.getUserSeq())); 				// ????????? ????????? ???????????? ????????? user_seq
					batchDomain.setProperty03(Integer.toString(usersDomain.getUserCorpReqSeq()));	// ????????? ????????? ???????????? ????????? ???????????? seq
					batchDomain.setProperty04("CorpReq");											// ??????????????? ?????????????????? ????????? ????????? param
					batchRepository.insertBatchPlanInfo(batchDomain);
					

				}
			}else {
				
				// ???????????? ??? ???????????? ?????? ??????
				int updResult = usersRepository.updateCorpUsersStat(usersDomain);
				// user?????? update
				int updInfoResult = usersRepository.updateCorpUsersApplyInfo(usersResult);
				
				int updCorpResult = usersRepository.updateCorpApplyInfo(usersResult);
				
				if(updResult > 0 && updInfoResult > 0) {
					return responseMsg;
				}else {
					return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
				}
				
				
			}
			
		}else if("3".equals(usersDomain.getStat())) {
			
			// ???????????? ????????? ?????? ????????? ??????
			/*
			int smsResult = 0;
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("0220110770");
			smsDomain.setTranStatus("1");
			smsDomain.setTranEtc1("10070");
			smsDomain.setTranPhone(usersResult.getMobileNo());
			String tranMsg = "";
			tranMsg+= usersResult.getUserName()+"?????? ???????????? ????????? ?????????????????????.";
			smsDomain.setTranMsg(tranMsg);
			
			smsResult = smsRepository.sendSms(smsDomain);
			*/
			
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
		int result = usersRepository.updateCorpUsersStat(usersDomain);
		if(result > 0) {
			return responseMsg;
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
}
