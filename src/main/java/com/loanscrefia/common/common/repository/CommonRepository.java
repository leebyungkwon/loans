package com.loanscrefia.common.common.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.system.code.domain.CodeDtlDomain;

@Mapper
public interface CommonRepository {
	
	//첨부파일 리스트
	List<FileDomain> selectFileList(FileDomain fileDomain);
	
	//첨부파일 그룹 시퀀스 조회
	int selectFileGrpSeq(FileDomain fileDomain);
	
	//첨부파일 등록
	void insertFile(FileDomain fileDomain);
	
	//첨부파일 단건 조회
	FileDomain getFile(FileDomain fileDomain);
	
	//첨부파일 삭제
	int deleteFile(FileDomain fileDomain);
	
	//첨부파일 real 삭제
	int realDeleteFile(FileDomain fileDomain);
	
	//회원사 리스트
	List<CodeDtlDomain> selectCompanyCodeList(CodeDtlDomain codeDtlDomain);

	//로그인 회원상세조회
	MemberDomain getMemberDetail(MemberDomain memberDomain);
	
	//회원사 회원상세조회
	MemberDomain getCompanyMemberDetail(MemberDomain memberDomain);
	
	//결제정보 조회
	PayResultDomain getPayResultDetail(PayResultDomain payResultDomain);
}
