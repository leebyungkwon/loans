package com.loanscrefia.system.batch.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.user.domain.UserDomain;

@Mapper
public interface BatchRepository {
	
	//모집인 엑셀 업로드 후 1개월동안 처리상태가 미요청 + 모집인상태가 승인전인 경우 -> 모집인 데이터 및 모집인 관련 첨부파일 삭제
	List<UserDomain> selectExcelUploadUserAndFileDelete();
	
	
}
