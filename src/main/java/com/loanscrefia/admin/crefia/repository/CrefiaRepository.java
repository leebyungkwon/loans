package com.loanscrefia.admin.crefia.repository;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;

@Mapper
public interface CrefiaRepository {
	
	// 협회 관리자 관리 조회
	List<CrefiaDomain> selectCrefiaList(CrefiaDomain crefiaDomain);

	// 협회 관리자 관리 상세 조회
	CrefiaDomain crefiaDetail(CrefiaDomain crefiaDomain);
	
	// 협회 관리자 등록
	int insertCrefia(CrefiaDomain crefiaDomain);
	
	// 협회 관리자 수정
	int updateCrefia(CrefiaDomain crefiaDomain);
	
	// 등록되어있는 아이디 확인
	int getmemberId(CrefiaDomain crefiaDomain);
	
	// 협회 관리자 삭제
	int deleteCrefia(CrefiaDomain crefiaDomain);
	
	//협회 관리자 업무분장 - 멤버시퀀스
	List<CrefiaDomain> selectCrefiaWorkMemberList(CrefiaDomain crefiaDomain);
	
	//협회 관리자 업무분장 - 회원사코드 - 체크박스 포함
	List<CrefiaDomain> selectCrefiaWorkCompanyList(CrefiaDomain crefiaDomain);
	
	// 협회 관리자 업무분장 등록
	int insertCrefiaWork(@Valid CrefiaDomain crefiaDomain);
	
	// 협회 관리자 업무분장 삭제
	int deleteCrefiaWork(@Valid CrefiaDomain crefiaDomain);

}