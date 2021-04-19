package com.loanscrefia.common.common.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VersionDomain {
	private Long verId;
	private String versionNm;
	private Long versionNum;
	private LocalDateTime regDt;
}