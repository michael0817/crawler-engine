package com.pab.framework.crawlerengine.model;

import java.util.List;

import com.pab.framework.crawlerdb.domain.CrawlerContent;

import lombok.Data;

@Data
public class ContentInfo {

	//总记录条数
	private int totalCount;
	//总页数
	private int totalPageNum;
	//每页大小
	private int pagePerNum;
	//数据
	private List<CrawlerContent> crawlerContent;
}
