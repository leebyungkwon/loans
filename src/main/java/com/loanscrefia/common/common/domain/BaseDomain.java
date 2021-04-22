package com.loanscrefia.common.common.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public abstract class BaseDomain extends PagingDomain{

    private String useYn;
    private String regDate; 
    private int regNo;
    private String updDate; 
    private int updNo;
}
