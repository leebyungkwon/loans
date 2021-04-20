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

    private String email;
    private String memberNm;
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