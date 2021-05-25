package com.loanscrefia.common.common.email.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.SendEmailDomain;
import com.loanscrefia.common.common.domain.VersionDomain;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.common.common.repository.VersionRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Service
public class EmailService {

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

	//회원사 리스트
	@Transactional(readOnly=true)
	public List<CodeDtlDomain> selectCompanyCodeList(CodeDtlDomain codeDtlDomain){
		return commonRepository.selectCompanyCodeList(codeDtlDomain);
	}
	
	
	
	
	
	
	
	// 이메일 전송 - transactional 확인(멀티DB)
	@Transactional
	public int sendEmail(SendEmailDomain sendEmailDomain){
		// MSSQL DB INSERT
		return 0;
	}
	
	
	
	
}