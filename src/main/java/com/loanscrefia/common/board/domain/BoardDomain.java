package com.loanscrefia.common.board.domain;


import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("board")
public class BoardDomain extends BaseDomain{
	
    private Integer boardNo;
    private String boardType;
    @ExcelColumn(headerName = "제목", order = 0)
    private String boardTitle;
    @ExcelColumn(headerName = "내용", order = 1)
    private String boardCnts;
    private Long attchNo1;
    private Long attchNo2;
    private Long attchNo3;
    
}