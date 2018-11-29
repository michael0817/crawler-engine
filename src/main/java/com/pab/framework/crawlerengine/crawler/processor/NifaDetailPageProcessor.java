package com.pab.framework.crawlerengine.crawler.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
@Component
public class NifaDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private List<String> list=new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html =page.getHtml();
        String  dabiaoti= html.$("td.dabiaoti").replace("<[^<>]+>", "").get();
        dabiaoti= StringUtils.trimToNull(dabiaoti);
        if (dabiaoti!=null){
            list.add(dabiaoti);
        }
        String  zi8= html.$("td.zi8").replace("<[^<>]+>", "").get();
        zi8= StringUtils.trimToNull(zi8);
        if (zi8!=null){
            list.add(zi8);
        }

        String reproduced = html.xpath("p[@align='justify']/span/text()").get();
        reproduced=StringUtils.trimToNull(reproduced);
        if (reproduced!=null){
            list.add(reproduced);
        }

        String title=html.xpath("p[@style]/span[@style]/span[@style]/text()").get();
        title=StringUtils.trimToNull(title);
        if (title!=null){
            list.add(title);
        }
        Selectable xpath = html.xpath("p[@align='justify']");
        List<Selectable> nodes = xpath.nodes();
        int size=nodes.size();
        String justify;
        for (int i = 0; i < size; i++) {
            justify=nodes.get(i).replace("<[^<>]+>","").get();
            justify=StringUtils.trimToNull(justify);
            if (justify!=null) {
                list.add(justify);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        NifaDetailPageProcessor process=new NifaDetailPageProcessor();
        //http://www.nifa.org.cn/nifa/2955686/2955720/2971011/index.html 政策法规
        //http://www.nifa.org.cn/nifa/2961652/2961654/2976355/index.html 时政要闻
        Spider spider=Spider.create(process).addUrl("http://www.nifa.org.cn/nifa/2961652/2961656/2976303/index.html");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
            System.out.println(process.getList());
        }

    }
}
