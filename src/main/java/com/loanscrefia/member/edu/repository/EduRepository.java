package com.loanscrefia.member.edu.repository;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.edu.domain.EduDomain;

@Mapper
public interface EduRepository {

	//회원사 시스템 > 모집인 등록 : 엑셀 업로드 시 교육이수번호 체크
	int plEduNoCheck(EduDomain eduDomain);
}
