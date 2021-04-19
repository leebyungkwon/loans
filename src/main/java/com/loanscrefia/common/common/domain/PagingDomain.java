package com.loanscrefia.common.common.domain;

import lombok.Data;

@Data
public class PagingDomain {
	private int page;
	private int size;
	private int offset;
	private String sort;
	private String sortName;
	private int totalCnt;
	private String isPaging;
	
	public void setSort(String sort) {
		this.sortName = sort.split(",")[0];
		this.sort = sort.split(",")[1];
		this.offset = this.page * this.size;
	}
}
