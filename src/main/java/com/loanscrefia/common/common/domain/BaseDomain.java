package com.loanscrefia.common.common.domain;

import lombok.Data;

@Data
public class BaseDomain extends PagingDomain{

    private String useYn;
    private String regTimestamp;
    private int regSeq;
    private String updTimestamp; 
    private int updSeq;
}
