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
	
	//이메일 적용여부
	@Value("${email.apply}")
	public boolean emailApply;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//SMS 적용여부
	@Value("${sms.apply}")
	public boolean smsApply;
	
	@Autowired private UtilFile utilFile;
	@Autowired private UtilExcel<T> utilExcel;
	
	//첨부파일 경로
	@Value("${upload.filePath}")
	public String uPath;
	
	@Autowired
	private BatchRepository batchRepository;
	
	// 개인 회원관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectIndvUsersList(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		// 주민번호 및 법인번호 암호화 후 비교
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
				if(StringUtils.isNotEmpty(plMZId)) {
					if(!"false".equals(usersDomain.getIsPaging())) {
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
		
		return resultList;
	}
	
	
	// 개인 회원관리 상세
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
			if(StringUtils.isNotEmpty(plMZId)) {
				if(!"false".equals(usersDomain.getIsPaging())) {
					plMZId = mask.maskSSN(plMZId);
				}
			}
			plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
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
	
	// 개인회원 결격요건 수정
	@Transactional
	public ResponseMsg updateIndvUserDis(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		
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
	
	// 로그인 잠금 해제
	@Transactional
	public ResponseMsg loginStopUpdate(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int[] userSeqArr 	= usersDomain.getUserSeqArr();
		for(int i = 0; i < userSeqArr.length; i++) {
			UsersDomain resultDomain = new UsersDomain();
			resultDomain.setUserSeq(userSeqArr[i]);
			usersRepository.loginStopUpdate(resultDomain);
		}
		
		return responseMsg;
	}
	
	
	//개인회원 결격요건 엑셀 업로드
	@Transactional
	public ResponseMsg indvUsersDisExcelUpload(MultipartFile[] files, UsersDomain usersDomain) throws IOException{
		
		//세션 정보
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("dis")
				.setFiles(files)
				.setExt("excel")
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam2(usersDomain.getPlClass()).disUpload(uPath, filePath, fileSaveNm, fileExt, IndvUsersExcelDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
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
					// 결과메세지
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "결격요건 업로드가 완료되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	
	
	
	
	
	
	

	
	// 법인 회원관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectCorpUsersList(UsersDomain usersDomain){
		UtilMask mask = new UtilMask();
		// 주민번호 및 법인번호 암호화 후 비교
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
				if(StringUtils.isNotEmpty(plMZId)) {
					if(!"false".equals(usersDomain.getIsPaging())) {
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
		
		return resultList;
	}
	
	
	// 법인 회원관리 상세
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
			if(StringUtils.isNotEmpty(plMZId)) {
				if(!"false".equals(usersDomain.getIsPaging())) {
					plMZId = mask.maskSSN(plMZId);
				}
			}
			plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
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
	
	// 회원관리 법인 승인처리
	@Transactional
	public ResponseMsg usersCorpApply(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int result = usersRepository.usersCorpApply(usersDomain);
		if(result <= 0) {
			responseMsg.setMessage("실패하였습니다");
			responseMsg.setCode("fail");
			return responseMsg;
		}else {
			// 2021-09-16 법인회원 승인시 이메일 발송 여부 확인
			
			/*
			
			if(emailApply) {
				UsersDomain usersResult = usersRepository.getUsersDetail(usersDomain);
				int emailResult = 0;
				EmailDomain emailDomain = new EmailDomain();
				emailDomain.setName("여신금융협회");
				emailDomain.setEmail(usersDomain.getEmail());
				emailDomain.setInstId("139");
				emailDomain.setSubsValue(usersResult.getUserId());
				emailResult = emailRepository.sendEmail(emailDomain);
				if(emailResult == 0) {
					responseMsg.setMessage("메일발송에 실패하였습니다");
					responseMsg.setCode("fail");
					return responseMsg;
				}
			}
			
			*/
			return responseMsg;
		}
		
	}
	
	
	
	// 법인회원 결격요건 수정
	@Transactional
	public ResponseMsg updateCorpUserDis(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		
		UsersDomain usersDomain1 = new UsersDomain();
		usersDomain1.setUserSeq(usersDomain.getUserSeq());
		usersDomain1.setDisCd("9");
		usersDomain1.setDisVal(usersDomain.getDis9());
		usersRepository.updateCorpUserDis(usersDomain1);
		
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
	
	// 금융감독원 승인여부 수정
	@Transactional
	public ResponseMsg updatePassYn(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int result = usersRepository.updatePassYn(usersDomain);
		if(result <= 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
		}
		return responseMsg;
	}
	
	//법인회원 결격요건 엑셀 업로드
	@Transactional
	public ResponseMsg corpUsersDisExcelUpload(MultipartFile[] files, UsersDomain usersDomain) throws IOException{
		
		//세션 정보
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("dis")
				.setFiles(files)
				.setExt("excel")
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam2(usersDomain.getPlClass()).disUpload(uPath, filePath, fileSaveNm, fileExt, CorpUsersExcelDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					int insertResult = 0;
					for(int c=0; c<excelResult.size(); c++) {
						
						Map<String, Object> paramResult1 = excelResult.get(c);
						UsersDomain usersDomain1 = new UsersDomain();
						usersDomain1.setPlMerchantName(paramResult1.get("C").toString());
						usersDomain1.setPlMerchantNo(paramResult1.get("D").toString());
						usersDomain1.setDisCd("9");
						usersDomain1.setDisVal(paramResult1.get("E").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain1);

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
						
						Map<String, Object> paramResult4 = excelResult.get(c);
						UsersDomain usersDomain4 = new UsersDomain();
						usersDomain4.setPlMerchantName(paramResult4.get("C").toString());
						usersDomain4.setPlMerchantNo(paramResult4.get("D").toString());
						usersDomain4.setDisCd("12");
						usersDomain4.setDisVal(paramResult4.get("H").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain4);
						
						Map<String, Object> paramResult5 = excelResult.get(c);
						UsersDomain usersDomain5 = new UsersDomain();
						usersDomain5.setPlMerchantName(paramResult5.get("C").toString());
						usersDomain5.setPlMerchantNo(paramResult5.get("D").toString());
						usersDomain5.setDisCd("13");
						usersDomain5.setDisVal(paramResult5.get("I").toString());
						usersRepository.corpUsersDisExcelUpload(usersDomain5);
						insertResult++;
					}
					// 결과메세지
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "결격요건 업로드가 완료되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	
	
	
	
	
	
	
	
	
	// 휴면회원관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectInactiveList(UsersDomain usersDomain){
		return usersRepository.selectInactiveList(usersDomain);
	}
	
	// 휴면회원관리 상세
	@Transactional(readOnly=true)
	public UsersDomain getInactiveDetail(UsersDomain usersDomain){
		return usersRepository.getInactiveDetail(usersDomain);
	}
	
	// 휴면회원 활성화
	@Transactional
	public ResponseMsg boInactiveUser(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		int[] userSeqArr 	= usersDomain.getUserSeqArr();
		for(int i = 0; i < userSeqArr.length; i++) {
			UsersDomain resultDomain = new UsersDomain();
			resultDomain.setUserSeq(userSeqArr[i]);
			usersRepository.updateBoInactiveUser(resultDomain);
			usersRepository.deleteBoInactiveUser(resultDomain);
		}
		return responseMsg;
	}
	
	
	
	// 개인회원 정보변경관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectUpdateIndvUsersList(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		// 주민번호 및 법인번호 암호화 후 비교
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
				if(StringUtils.isNotEmpty(plMZId)) {
					if(!"false".equals(usersDomain.getIsPaging())) {
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
		
		return resultList;
	}
	
	
	// 개인회원 정보변경관리 상세
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
			plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
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
			reqPlMZId 		= reqPlMZId.substring(0, 6) + "-" + reqPlMZId.substring(6);
			result.setReqPlMZId(reqPlMZId);
		}
		
		return result;
	}
	
	
	// 개인회원 정보변경관리 상태변경
	@Transactional
	public ResponseMsg updateIndvUsersStat(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		
		// 정보변경 승인 -> API발송을 위한 배치 테이블 insert
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setUserSeq(usersDomain.getUserSeq());
		
		// 등록자로 모든 계약건 조회
		List<NewApplyDomain> resultList = usersRepository.selectUserSeqIndvList(newApplyDomain);
		
		// 수정되여야할 개인정보
		UsersDomain usersResult = usersRepository.getUpdateIndvUsersDetail(usersDomain);
		
		if("2".equals(usersDomain.getStat())) {

			String userName = usersResult.getReqUserName();
			String mobileNo = usersResult.getReqMobileNo();
			String ssn = usersResult.getReqPlMZId();
			
			//String userCi = usersResult.getReqUserCi();
			String userCi = usersResult.getUserCi();
			
			if(resultList.size() > 0) {
				
				// 주민등록번호 변경 배치 insert 시작
				JSONObject ssnJsonParam = new JSONObject();
				if(StringUtils.isNotEmpty(ssn)) {
					ssnJsonParam.put("user_seq", usersResult.getUserSeq());
					ssnJsonParam.put("bef_ssn", usersResult.getPlMZId());
					ssnJsonParam.put("aft_ssn", ssn);
					ssnJsonParam.put("aft_ci", userCi);
					
					BatchDomain ssnBatchDomain = new BatchDomain();
					ssnBatchDomain.setScheduleName("loanSsnUpd");
					ssnBatchDomain.setParam(ssnJsonParam.toString());
					ssnBatchDomain.setProperty01("1"); 													// 개인,법인 구분값
					ssnBatchDomain.setProperty02(Integer.toString(usersResult.getUserSeq())); 			// 마지막 결과값 변경시에 사용될 user_seq
					ssnBatchDomain.setProperty03(Integer.toString(usersDomain.getUserCorpReqSeq()));	// 마지막 결과값 변경시에 사용될 변경신청 seq
					ssnBatchDomain.setProperty04("CorpReq");											// 개인정보와 주민등록번호 성공에 사용될 param
					batchRepository.insertBatchPlanInfo(ssnBatchDomain);
				}
				
				for(NewApplyDomain result : resultList) {
					//배치 테이블 저장
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
					
					// 배열
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
					batchDomain.setProperty01("1"); //개인,법인 구분값
					batchDomain.setProperty02(Integer.toString(result.getUserSeq())); 				// 마지막 결과값 변경시에 사용될 user_seq
					batchDomain.setProperty03(Integer.toString(usersDomain.getUserIndvReqSeq()));	// 마지막 결과값 변경시에 사용될 변경신청 seq
					
					batchRepository.insertBatchPlanInfo(batchDomain);
					usersDomain.setStat("1");
					
				}
			}else {
				
				// 정보변경 할 계약건이 없는 경우
				int updResult = usersRepository.updateIndvUsersStat(usersDomain);
				// user정보 update
				int updInfoResult = usersRepository.updateIndvUsersApplyInfo(usersResult);
				
				if(updResult > 0 && updInfoResult > 0) {
					return responseMsg;
				}else {
					return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
				}
			}

			
		}else if("3".equals(usersDomain.getStat())) {
			
			// 정보변경 거절에 대한 메세지 발송
			int smsResult = 0;
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("0220110700");
			smsDomain.setTranStatus("1");
			smsDomain.setTranEtc1("10070");
			smsDomain.setTranPhone(usersResult.getMobileNo());
			String tranMsg = "";
			tranMsg+= usersResult.getUserName()+"님의 개인정보 변경이 거절되었습니다.";
			smsDomain.setTranMsg(tranMsg);
			
			smsResult = smsRepository.sendSms(smsDomain);
			
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
		int result = usersRepository.updateIndvUsersStat(usersDomain);
		if(result > 0) {
			return responseMsg;
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	// 법인회원 정보변경관리 리스트 조회
	@Transactional(readOnly=true)
	public List<UsersDomain> selectUpdateCorpUsersList(UsersDomain usersDomain){
		
		UtilMask mask = new UtilMask();
		// 주민번호 및 법인번호 암호화 후 비교
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
				if(StringUtils.isNotEmpty(plMZId)) {
					if(!"false".equals(usersDomain.getIsPaging())) {
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
		
		return resultList;
	}
	
	
	// 법인회원 정보변경관리 상세
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
			plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
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
			reqPlMZId 		= reqPlMZId.substring(0, 6) + "-" + reqPlMZId.substring(6);
			result.setReqPlMZId(reqPlMZId);
		}
		
		return result;
	}
	
	// 법인회원 정보변경관리 상태변경
	@Transactional
	public ResponseMsg updateCorpUsersStat(UsersDomain usersDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "success", null, "완료되었습니다.");
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setUserSeq(usersDomain.getUserSeq());
		
		// 등록자로 모든 계약건 조회
		List<NewApplyDomain> resultList = usersRepository.selectUserSeqCorpList(newApplyDomain);
		
		// 수정되여야할 개인정보
		UsersDomain usersResult = usersRepository.getUpdateCorpUsersDetail(usersDomain);
		
		if("2".equals(usersDomain.getStat())) {
			// 정보변경 승인 -> API발송을 위한 배치 테이블 insert
			String plMerchantName = usersResult.getReqPlMerchantName();
			String userName = usersResult.getReqUserName();
			// 주민등록번호 수정시 
			String ssn = usersResult.getReqPlMZId();
			String mobileNo = usersResult.getReqMobileNo();
			//String userCi = usersResult.getReqUserCi();
			String userCi = usersResult.getUserCi();
			
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
					ssnBatchDomain.setProperty01("2"); 													//개인,법인 구분값
					ssnBatchDomain.setProperty02(Integer.toString(usersResult.getUserSeq())); 				// 마지막 결과값 변경시에 사용될 user_seq
					ssnBatchDomain.setProperty03(Integer.toString(usersDomain.getUserCorpReqSeq()));	// 마지막 결과값 변경시에 사용될 변경신청 seq
					ssnBatchDomain.setProperty04("CorpReq");											// 개인정보와 주민등록번호 성공에 사용될 param
					batchRepository.insertBatchPlanInfo(ssnBatchDomain);
				}
				
				for(NewApplyDomain result : resultList) {
					//배치 테이블 저장
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
					
					// 배열
					jsonArrayParam.put("con_num", result.getConNum());
					jsonArrayParam.put("con_date", result.getComContDate().replaceAll("-", ""));
					jsonArrayParam.put("loan_type", result.getPlProduct());
					
					jsonArray.put(jsonArrayParam);
					jsonParam.put("con_arr", jsonArray);
					
					batchDomain.setScheduleName("loanUpd");
					batchDomain.setParam(jsonParam.toString());
					batchDomain.setProperty01("2");													//개인,법인 구분값
					batchDomain.setProperty02(Integer.toString(result.getUserSeq())); 				// 마지막 결과값 변경시에 사용될 user_seq
					batchDomain.setProperty03(Integer.toString(usersDomain.getUserCorpReqSeq()));	// 마지막 결과값 변경시에 사용될 변경신청 seq
					batchDomain.setProperty04("CorpReq");											// 개인정보와 주민등록번호 성공에 사용될 param
					batchRepository.insertBatchPlanInfo(batchDomain);
					

				}
			}else {
				
				// 정보변경 할 계약건이 없는 경우
				int updResult = usersRepository.updateCorpUsersStat(usersDomain);
				// user정보 update
				int updInfoResult = usersRepository.updateCorpUsersApplyInfo(usersResult);
				
				int updCorpResult = usersRepository.updateCorpApplyInfo(usersResult);
				
				if(updResult > 0 && updInfoResult > 0) {
					return responseMsg;
				}else {
					return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
				}
				
				
			}
			
		}else if("3".equals(usersDomain.getStat())) {
			
			// 정보변경 거절에 대한 메세지 발송
			int smsResult = 0;
			SmsDomain smsDomain = new SmsDomain();
			smsDomain.setTranCallback("0220110700");
			smsDomain.setTranStatus("1");
			smsDomain.setTranEtc1("10070");
			smsDomain.setTranPhone(usersResult.getMobileNo());
			String tranMsg = "";
			tranMsg+= usersResult.getUserName()+"님의 개인정보 변경이 거절되었습니다.";
			smsDomain.setTranMsg(tranMsg);
			
			smsResult = smsRepository.sendSms(smsDomain);
			
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
		int result = usersRepository.updateCorpUsersStat(usersDomain);
		if(result > 0) {
			return responseMsg;
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
}
