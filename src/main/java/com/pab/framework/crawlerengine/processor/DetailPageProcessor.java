package com.pab.framework.crawlerengine.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerengine.factory.Detail;
import com.pab.framework.crawlerengine.factory.DetailFactory;
import com.pab.framework.crawlerengine.util.CrawlerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DetailPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
            .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
            .addHeader("Content-Type", "application/x-www-form-urlencoded");


    private String title;
    private String date;
    private String content;
    private String domain;

    private List<CrawlerArticle> crawlerArticles = null;
    private String regex;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Detail detail = DetailFactory.getDetail(domain);
        //暂时处理 //http://www.cbrc.gov.cn
        //http://www.nifa.org.cn
        //http://www.51kaxun.com
        //https://www.wdzj.com
      //  http://www.iresearch.com.cn
        //https://www.analysys.cn
        //http://www.bugutime.com
        //https://xueqiu.com
        //https://www.huxiu.com
        String contentType=  page.getHeaders().get("Content-Type").get(0);
        if (contentType.contains("html")) {
                if (StringUtils.isNotEmpty(regex)) {
                    //加载更多
                  //  regex="p[@id='anal_icon']/span[3]/text(),h1[@class='flf']/text(),div[@class='detail_left']";
                 //   regex="time/text(),h1[@class='view-title']/text(),div[@class='wrap']";
                //    regex="a[@class='time']/text(),h1[@class='article__bd__title']/text(),div[@id='app']";
                //    regex="span[@class='article-time']/text(),h1[@class='t-h1']/text(),div[@class='article-section-wrap']";
                    CrawlerArticle crawlerArticle = new CrawlerArticle();
                    String[] regexes = regex.split(",");
                    String date;
                    if (StringUtils.isNotEmpty(regexes[0])) {
                        date = html.xpath(regexes[0]).get();
                        String regex = "(\\d+(-|/|年))?\\d+(-|/|月)\\d+日?( \\d+:\\d+(:\\d+)?)?";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(date);
                        if (matcher.find()) {
                            date = matcher.group();

                            if (date!=null){
                                crawlerArticle.setDate(date);
                            }
                        }
                    }
                    String title;
                    if (StringUtils.isNotEmpty(regexes[1])) {
                        title = html.xpath(regexes[1]).get();
                        if (title != null) {
                            crawlerArticle.setTitle(title);
                        }
                    }
                    String content;
                    if (StringUtils.isNotEmpty(regexes[2])) {
                        content = html.xpath(regexes[2]).smartContent().get();
                        if (content != null) {
                            crawlerArticle.setContent(content);
                            crawlerArticles.add(crawlerArticle);
                        }
                    }

                }
        }
        else{
            JSONObject jsonObject = JSON.parseObject(page.getRawText());
            JSONArray jsonArray = jsonObject.getJSONArray("List");
            int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                jsonObject=jsonArray.getJSONObject(i);
                title = jsonObject.getString("Title");
                date=jsonObject.getString("Uptime");
                content=jsonObject.getString("Content");
                CrawlerArticle crawlerArticle = new CrawlerArticle();
                if (title != null) {
                    crawlerArticle.setTitle(title);
                }
                if (date!=null){
                    crawlerArticle.setDate(date);
                }
                if (content != null) {
                    crawlerArticle.setContent(content);
                    crawlerArticles.add(crawlerArticle);
                }
            }
        }



    }

    @Override
    public Site getSite() {
        return site;
    }



    public List<CrawlerArticle> process(String regex, String baseUrlAddr, List<String> urlAddrs) {
        this.regex = regex;
        crawlerArticles = new ArrayList<CrawlerArticle>();
        int size = urlAddrs.size();
        String[] url_addrs = new String[urlAddrs.size()];
        for (int i = 0; i < size; i++) {
            if (urlAddrs.get(i).startsWith("//")) {
                url_addrs[i] ="https:"+urlAddrs.get(i);
            }
            else if(urlAddrs.get(i).startsWith(baseUrlAddr)){
                url_addrs[i] =urlAddrs.get(i);
            }
            else{
                url_addrs[i] = baseUrlAddr + urlAddrs.get(i);
            }
        }
        domain = CrawlerUtil.domainStr((url_addrs[0]));
        Spider spider = Spider.create(this).addUrl(url_addrs);
        spider.run();
        if (Spider.Status.Stopped.compareTo(spider.getStatus()) == 0) {
            return crawlerArticles;
        }
        return null;
    }

}
