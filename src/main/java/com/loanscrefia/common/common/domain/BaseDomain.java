package com.loanscrefia.common.common.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public abstract class BaseDomain extends PagingDomain{

    private String useYn;
    private String regDate; //LocalDateTime regDate;
    private int regNo;
    private String updDate; //LocalDateTime updDate;
    private int updNo;
}
