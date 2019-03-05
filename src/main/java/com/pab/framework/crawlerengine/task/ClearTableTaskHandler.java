package com.pab.framework.crawlerengine.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pab.framework.crawlerdb.service.DbService;

import lombok.extern.slf4j.Slf4j;

/*
 * XIANGNENG806  2019/03/04
 */
@Component
@Configurable
@EnableScheduling
@Slf4j
public class ClearTableTaskHandler implements TaskHandler {

	@Autowired
	private DbService dbService;
	
	
	@Scheduled(cron = "0 0 12 * * ?")
	@Override
	public void taskRun() {
		try {
			dbService.deleteContentByCrawlerDate();
			log.info("crawler_conetent表清理完成(保留30天数据)");
			dbService.deleteLogByCrawlerDateTime();
			log.info("crawler_log表清理完成(保留30天数据)");
		}catch(Exception e) {
			log.error("crawler_content,crawler_log表清理失败(保留30天数据)",e);
		}
	}
}
