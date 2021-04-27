package com.loanscrefia.system.code.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("codeMst")
public class CodeMstDomain extends BaseDomain {

	/* -------------------------------------------------------------------------------------------------------
	 * 코드 마스터(tb_lc_cod01)
	 * -------------------------------------------------------------------------------------------------------
	 */
	@NotBlank(message = "코드마스터ID를 입력해 주세요.")
	@Pattern(regexp = "[a-zA-Z0-9]{1,10}", message = "코드마스터ID는 영문/숫자 1~10자리로 입력해 주세요.")
	private String codeMstCd;
	
	@NotBlank(message = "코드마스터명을 입력해 주세요.")
	@Pattern(regexp = "^[가-힣]*${1,100}", message = "코드마스터명은 한글 1~100자리로 입력해 주세요.")
	private String codeMstNm;
	
	private String codeMstDesc;
	private String property01;
	private String property02;
	private String property03;
	private String property04;
	private String property05;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 그외
	 * -------------------------------------------------------------------------------------------------------
	 */
	private String saveType;
}
