package com.pab.framework.crawlerengine;

import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerEngineApplicationTest {

    @Autowired
    CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;

    @Test
    public void contextLoads() {

        System.out.println( crawlerNewsMilestoneDao.findUrlAddrsByCrawl() );
    }

}
