package com.loanscrefia.system.code.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;

import lombok.Data;

@Data
@Alias("codeDtl")
public class CodeDtlDomain extends BaseDomain {

	/* -------------------------------------------------------------------------------------------------------
	 * 코드 상세(tb_lc_cod01_detail)
	 * -------------------------------------------------------------------------------------------------------
	 */
	@NotBlank(message = "코드마스터ID를 선택해 주세요.")
	@Pattern(regexp = "[a-zA-Z0-9]{1,10}", message = "코드마스터ID는 영문/숫자 1~10자리로 입력해 주세요.")
	private String codeMstCd;
	
	@NotBlank(message = "코드상세ID를 입력해 주세요.")
	@Pattern(regexp = "[a-zA-Z0-9]{1,10}", message = "코드상세ID는 영문/숫자 1~10자리로 입력해 주세요.")
	private String codeDtlCd;
	
	@NotBlank(message = "코드상세명을 입력해 주세요.")
	@Pattern(regexp = "^[가-힣]*${1,100}", message = "코드상세명은 한글 1~100자리로 입력해 주세요.")
	private String codeDtlNm;
	
	private String codeDtlDesc;
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
