package com.pab.framework.crawlerweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pab.framework.crawlerengine.task.MallTaskHandler;
import com.pab.framework.crawlerengine.task.NewsTaskHandler;

import lombok.extern.slf4j.Slf4j;

/*
 * xiangn806 
 */
@Controller
@Slf4j
@RequestMapping("/task")
public class CrawlerTaskController {
	
	@Autowired
	private NewsTaskHandler newsTaskHandler;
	@Autowired
	private MallTaskHandler mallTaskHandler;

	/**
	 * 手动触发task
	 * @param taskType
	 * @param 
	 * 
	 */
	@RequestMapping("/trigger/{taskType}")
	public void trigger(@PathVariable("taskType") String taskType) {
		if("news".equals(taskType)) {
			newsTaskHandler.taskRun();
		}else if("mall".equals(taskType)) {
			mallTaskHandler.taskRun();
		}
	}
	
}
