package com.loanscrefia.common.common.service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.common.domain.VersionDomain;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.common.common.repository.VersionRepository;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.common.member.domain.SignupDomain;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Service
public class CommonService {

	@Autowired private VersionRepository verRepo;
	@Autowired private CommonRepository commonRepository;
	
	@Value("${upload.filePath}")
	public String uploadPath;
	
	@CacheEvict(value = "static", allEntries=true)
	public VersionDomain verSave(VersionDomain versionDomain) {
		return verRepo.save(versionDomain);
	}
	
	@Cacheable(value = "static")
	public Optional<VersionDomain> getVer(VersionDomain versionDomain) {
		return verRepo.findById(versionDomain.getVerId());
	}
	
	//첨부파일 리스트
	@Transactional(readOnly=true)
	public List<FileDomain> selectFileList(FileDomain fileDomain){
		return commonRepository.selectFileList(fileDomain);
	}
	
	//첨부파일 리스트(그룹 시퀀스 사용)
	@Transactional(readOnly=true)
	public List<FileDomain> selectFileListByGrpSeq(FileDomain fileDomain){
		return commonRepository.selectFileListByGrpSeq(fileDomain);
	}
	
	//첨부파일 단건 조회
	@Transactional(readOnly=true)
	public FileDomain getFile(FileDomain fileDomain) {
		return commonRepository.getFile(fileDomain);
	}
	
	//첨부파일 삭제
	@Transactional
	public int deleteFile(FileDomain fileDomain) {
		return commonRepository.deleteFile(fileDomain);
	}
	
	//첨부파일 삭제(그룹 시퀀스 사용)
	@Transactional
	public int deleteFileByGrpSeq(FileDomain fileDomain) {
		return commonRepository.deleteFileByGrpSeq(fileDomain);
	}
	
	//첨부파일 real 삭제
	@Transactional
	public int realDeleteFile(FileDomain fileDomain) {
		
		FileDomain fileInfo = this.getFile(fileDomain);
		
		String oFile = this.uploadPath.toString() + "/" + fileInfo.getFilePath() + "/" + fileInfo.getFileSaveNm() + "." + fileInfo.getFileExt();
		String dFile = this.uploadPath.toString() + "/" + fileInfo.getFilePath() + "/" + fileInfo.getFileSaveNm() + "_dnc." + fileInfo.getFileExt();
		
		File delFile1 = new File(oFile);
		File delFile2 = new File(dFile);
		
		if(delFile1.exists()) {
			delFile1.delete();
		}
		if(delFile2.exists()) {
			delFile2.delete();
		}
		
		return commonRepository.realDeleteFile(fileDomain);
	}
	
	//첨부파일 real 삭제(그룹 시퀀스 사용)
	@Transactional
	public int realDeleteFileByGrpSeq(FileDomain fileDomain) {
		
		List<FileDomain> list = this.selectFileListByGrpSeq(fileDomain);
		
		if(list.size() > 0) {
			for(int i = 0;i < list.size();i++) {
				String oFile = this.uploadPath + "/" + list.get(i).getFilePath() + "/" + list.get(i).getFileSaveNm() + "." + list.get(i).getFileExt();
				String dFile = this.uploadPath + "/" + list.get(i).getFilePath() + "/" + list.get(i).getFileSaveNm() + "_dnc." + list.get(i).getFileExt();
				
				File delFile1 = new File(oFile);
				File delFile2 = new File(dFile);
				
				if(delFile1.exists()) {
					delFile1.delete();
				}
				if(delFile2.exists()) {
					delFile2.delete();
				}
			}
			
			//점부파일 체크 정보 삭제
			commonRepository.realDeleteFileCheckInfoByGrpSeq(fileDomain);
		}
		
		return commonRepository.realDeleteFileByGrpSeq(fileDomain);
	}

	//회원사 리스트
	@Transactional(readOnly=true)
	public List<CodeDtlDomain> selectCompanyCodeList(CodeDtlDomain codeDtlDomain){
		
		//세션 체크
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		if(loginInfo != null) {
			codeDtlDomain.setCreYn(loginInfo.getCreYn());
			codeDtlDomain.setCreGrp(loginInfo.getCreGrp());
		}
		
		List<CodeDtlDomain> comList = commonRepository.selectCompanyCodeList(codeDtlDomain);
		
		return comList;
	}
	
	//로그인 회원상세조회
	@Transactional(readOnly=true)
	public MemberDomain getMemberDetail(MemberDomain memberDomain) {
		return commonRepository.getMemberDetail(memberDomain);
	}
	
	//회원사 회원상세조회
	@Transactional(readOnly=true)
	public MemberDomain getCompanyMemberDetail(MemberDomain memberDomain) {
		return commonRepository.getCompanyMemberDetail(memberDomain);
	}
	
	//결제정보 조회
	@Transactional(readOnly=true)
	public PayResultDomain getPayResultDetail(PayResultDomain payResultDomain) {
		return commonRepository.getPayResultDetail(payResultDomain);
	}
	
	//비밀번호 유효성 체크
	public String pwdValidation(String memberId, String password, String passwordChk) {
		
		String pwdChkResultMsg = "";
		
		//영문,숫자,특수문자
		String pwdPattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*?,./\\\\\\\\<>|_-[+]='\\\\`\\\";:~\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}])[[A-Za-z][0-9]!@#$%^&*?,./\\\\\\\\<>|_-[+]='\\\";:\\\\`~\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}]{8,20}$"; //영문,특수문자
		boolean matchResult1 = Pattern.matches(pwdPattern1, password);
		
		//영문,숫자
		String pwdPattern2 = "^(?=.*[A-Za-z])(?=.*[0-9])[[A-Za-z][0-9]]{8,20}$"; 
		boolean matchResult2 = Pattern.matches(pwdPattern2, password);
		
		//영문,특수문자
		String pwdPattern3 = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*?,./\\\\\\\\<>|_-[+]='\\\\`\\\";:~\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}])[[A-Za-z]!@#$%^&*?,./\\\\\\\\<>|_-[+]='\\\";:\\\\`~\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}]{8,20}$";
		boolean matchResult3 = Pattern.matches(pwdPattern3, password);
		
		//숫자,특수문자
		String pwdPattern4 = "^(?=.*[0-9])(?=.*[!@#$%^&*?,./\\\\\\\\<>|_-[+]='\\\\`\\\";:~\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}])[[0-9]!@#$%^&*?,./\\\\\\\\<>|_-[+]='\\\";:\\\\`~\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}]{8,20}$";
		boolean matchResult4 = Pattern.matches(pwdPattern4, password);
		
		//문자 반복
		String pwdPattern5 = "(\\w)\\1\\1";
		boolean matchResult5 = Pattern.matches(pwdPattern5, password);
		
		if(!password.equals(passwordChk)) {
			pwdChkResultMsg = "동일한 비밀번호를 입력해 주세요.";
		}else if(matchResult1 == false && matchResult2 == false && matchResult3 == false && matchResult4 == false) {
			pwdChkResultMsg = "비밀번호는 영문 대/소문자, 숫자, 특수문자 중 2개 이상의 조합으로 8자리 ~ 20자리 이내로 입력해 주세요.";
		}else if(password.contains(memberId)) {
			pwdChkResultMsg = "비밀번호에 아이디는 포함될 수 없습니다.";
		}else if(matchResult5) {
			pwdChkResultMsg = "비밀번호에 같은 문자를 3번 이상 사용할 수 없습니다.";
		}
		
		return pwdChkResultMsg;
	}
	
	
}