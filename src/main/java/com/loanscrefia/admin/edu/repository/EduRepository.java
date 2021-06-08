package com.loanscrefia.admin.edu.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.edu.domain.EduDomain;

@Mapper
public interface EduRepository {

	// 교육이수정보 리스트
	List<EduDomain> selectEduList(EduDomain eduDomain);
	
	//회원사 시스템 > 모집인 등록 : 엑셀 업로드 시 교육이수번호 체크
	int plEduNoCheck(EduDomain eduDomain);
	
	//회원사 시스템 > 모집인 등록 : 엑셀 업로드 시 교육이수번호 체크
	int plEduNoChk(EduDomain eduDomain);
	
}
