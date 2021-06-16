package com.loanscrefia.common.common.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Service
public class CommonService {

	@Autowired private VersionRepository verRepo;
	@Autowired private CommonRepository commonRepository;
	
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
	
	//첨부파일 real 삭제 [TO-DO 서버 파일도 삭제]*****
	@Transactional
	public int realDeleteFile(FileDomain fileDomain) {
		return commonRepository.realDeleteFile(fileDomain);
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
	
	
}