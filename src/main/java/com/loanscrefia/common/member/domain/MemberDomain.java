package com.loanscrefia.common.member.domain;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Builder;
import lombok.Data;

@Data
@Alias("member")
public class MemberDomain extends BaseDomain{
	
	private Long memberSeq;
    private String memberId;				
	private String password;				 
	private int comCode;
	private String memberName;
	private String email;
	private String mobileNo;
	private String deptNm;
	private String positionNm;
	private Long fileSeq;
    List<MemberRoleDomain> roles;
}