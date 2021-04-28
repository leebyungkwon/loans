package com.loanscrefia.common.member.domain;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("memberRole")
public class MemberRoleDomain extends BaseDomain{
    private Long roleId;
    private String roleName;
    private Long memberSeq;
}
