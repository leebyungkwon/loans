package com.loanscrefia.common.common.repository;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.common.domain.VersionDomain;



@Mapper
public interface VersionRepository {

	VersionDomain save(VersionDomain versionDomain);
	Optional<VersionDomain> findById(Long verId);
}