package com.pab.framework.crawlerengine.crawler.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pab.framework.crawlerengine.crawler.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class DetailUrlsPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {

        return list;
    }

    private String regex;

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        String rawText = page.getRawText();
        String url=page.getUrl().get();
        if (rawText.startsWith("{") || rawText.startsWith("[")) {
            list.add(url);
        }

        else if (url.contains("https://xueqiu.com/u/7558914709")){
            String path = System.getProperty("user.dir");
            path = path + "/src/main/resources/xueqiu.json";
            String json = null;
            try {
                json = FileUtils.getJson(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONArray statuses = jsonObject.getJSONArray("statuses");
            int maxPage= Integer.parseInt(url.substring(url.indexOf("?")+1));
            for (int i = 0; i < maxPage; i++) {
                list.add(  statuses.getJSONObject(i).getString("target"));
            }

        }
        else {
            Html html = page.getHtml();
            Selectable links = html.links();
            List<String> all = null;
            if (StringUtils.isNotEmpty(regex)) {
                all = links.regex(regex).all();
                Set<String> set = new HashSet<String>();
                set.addAll(all);
                all.clear();
                all.addAll(set);
                int size = all.size();
                for (int i = 0; i < size; i++) {
                    list.add(all.get(i));
                }
            }
        }
    }


    public void process(String regex, String urlAddr) {
        if (StringUtils.isNotEmpty(regex)) {
            this.setRegex(regex);
        }
        Spider spider = Spider.create(this).addUrl(urlAddr);
        spider.run();

    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * /chinese/newShouDoc/\w+.html
     * /nifa/\d+/\d+/\d+/index.html
     * /nifa/\d+/\d+/\d+/index.html|http://www.pbc.gov.cn/goutongjiaoliu/\d+/\d+/\d+/index.html
     * http://www.51kaxun.com/news/13\d{3}.html
     * //www.wdzj.com/news/yc/343\d{2}1\d{1}.html
     * /Detail/report?id=\d+&amp;isfree=\d+
     * <p>
     * http://www.bugutime.com/news/\d+.html
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        DetailUrlsPageProcessor process = new DetailUrlsPageProcessor();
        process.setRegex("/news/13\\d{3}.html");
        Spider spider = Spider.create(process).addUrl("http://www.51kaxun.com/news/search.php?id=3&p=1");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            for (String s : process.list) {
                System.out.println(s);
            }
        }
    }

}
