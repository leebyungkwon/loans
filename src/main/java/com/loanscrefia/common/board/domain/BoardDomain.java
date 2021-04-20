package com.loanscrefia.common.board.domain;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
	@NotBlank(message = "제목을 입력하세요.")
    @Email(message = "올바르지 않다.")
    private String boardTitle;
    
    @ExcelColumn(headerName = "내용", order = 1)
	@NotBlank(message = "내용을 입력하세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{2,20}", message = "내용은 영문/숫자 2~20자리로 입력해 주세요.")
    private String boardCnts;
    private Long attchNo1;
    private Long attchNo2;
    private Long attchNo3;
    
}