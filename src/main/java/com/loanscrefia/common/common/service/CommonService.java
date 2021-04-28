package com.loanscrefia.common.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.VersionDomain;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.common.common.repository.VersionRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Service
public class CommonService {

	@Autowired private VersionRepository verRepo;
	
	@Autowired
	private CommonRepository commonRepository;
	
	@CacheEvict(value = "static", allEntries=true)
	public VersionDomain verSave(VersionDomain versionDomain) {
		return verRepo.save(versionDomain);
	}
	
	@Cacheable(value = "static")
	public Optional<VersionDomain> getVer(VersionDomain versionDomain) {
		return verRepo.findById(versionDomain.getVerId());
	}
	
	
	
	// 공통코드조회
	public List<CodeDtlDomain> selectCommonCodeList(CodeDtlDomain codeDtlDomain){
		return commonRepository.selectCommonCodeList(codeDtlDomain);
	}
	
	// 공통회원사조회
	public List<CodeDtlDomain> selectCompanyCodeList(CodeDtlDomain codeDtlDomain){
		return commonRepository.selectCompanyCodeList(codeDtlDomain);
	}
	
	// 첨부파일 단건 조회
	public FileDomain getFile(FileDomain fileDomain) {
		return commonRepository.getFile(fileDomain);
	}

}