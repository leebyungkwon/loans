package com.loanscrefia.common.common.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("file")
public class FileDomain extends BaseDomain{

    private int fileSeq;
    private int fileGrpSeq;
    private String fileExt;
    private String fileOrgNm;
    private String filePath;
    private String fileSaveNm;
    private String fileType;
    private String useYn = "Y";
    
    //ocrTest
    private String fileDocType;
}
