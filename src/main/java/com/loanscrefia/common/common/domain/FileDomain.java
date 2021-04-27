package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("file")
public class FileDomain extends BaseDomain{

    private Long fileSeq;
    private String fileOrgNm;
    private String fileSaveNm;
    private String filePath;
    private String fileExt;
    private String useYn = "Y";
    private String fileType;
    private String fileTest;
    private String fileDocType;
}
