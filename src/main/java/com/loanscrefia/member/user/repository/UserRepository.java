package com.loanscrefia.member.user.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;

@Mapper
public interface UserRepository {
	
	//모집인 등록 > 리스트
	List<UserDomain> selectUserRegList(UserDomain userDomain);
	
	//모집인 등록 > 개인 등록
	int insertUserRegIndvInfoByExcel(UserDomain userDomain);
	
	//모집인 등록 > 법인 등록
	int insertUserRegCorpInfoByExcel(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 등록
	int insertUserRegCorpImwonInfoByExcel(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 등록
	int insertUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 등록
	int insertUserRegCorpExpertInfoByExcel(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 등록
	int insertUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 정보 등록
	int insertUserRegCorpItInfoByExcel(UserItDomain userItDomain);
	
	//모집인 등록 > 법인 : 전산인력 정보 등록
	int insertUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 > 승인요청
	int updatePlRegStat(UserDomain userDomain);
	
	//모집인 등록 > 상세
	UserDomain getUserRegDetail(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 상세
	List<UserImwonDomain> selectUserRegCorpImwonList(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 상세
	List<UserExpertDomain> selectUserRegCorpExpertList(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 전산인력 상세
	List<UserItDomain> selectUserRegCorpItList(UserItDomain userItDomain);
	
	//모집인 등록 > 수정
	int updateUserRegInfo(UserDomain userDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 수정
	int updateUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 수정
	int updateUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 수정
	int updateUserRegCorpItInfo(UserItDomain userItDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 삭제
	int deleteUserRegCorpImwonInfo(UserImwonDomain userImwonDomain);
	
	//모집인 등록 > 법인 : 전문인력 정보 삭제
	int deleteUserRegCorpExpertInfo(UserExpertDomain userExpertDomain);
	
	//모집인 등록 > 법인 : 대표자 및 임원 정보 삭제
	int deleteUserRegCorpItInfo(UserItDomain userItDomain);

	
	
	
	
	
	
	
	
}
