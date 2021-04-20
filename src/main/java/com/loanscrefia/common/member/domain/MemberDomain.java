package com.loanscrefia.common.member.domain;

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
    private Long memberNo;

	@NotBlank(message = "ID를 입력하세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{2,20}", message = "내용은 영문/숫자 2~20자리로 입력해 주세요.")
    private String email;
    private String memberNm;
    
	@NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    List<MemberRoleDomain> roles;

    @Builder
    public MemberDomain(Long memberNo, String email, String password, String memberNm) {
        this.memberNo = memberNo;
        this.email = email;
        this.password = password;
        this.memberNm = memberNm;
    }
}