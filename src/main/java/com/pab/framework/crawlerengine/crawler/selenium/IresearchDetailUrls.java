package com.pab.framework.crawlerengine.crawler.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.6.0
 */
public class IresearchDetailUrls {
//http://www.iresearch.com.cn/Detail/report?id=3293&isfree=0
    //http://www.iresearch.com.cn/report.shtml
    public static void main(String[] args) throws InterruptedException {
        IresearchDetailUrls detailUrls = new IresearchDetailUrls();
        String base_url_addr = "http://www.iresearch.com.cn";
        String url_addr = "/report.shtml";
        List<String> urls = detailUrls.getUrls(base_url_addr, url_addr);
        urls.forEach(url -> {
            System.out.println(url);
        });
    }

    public List<String> getUrls(String base_url_addr, String url_addr) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(base_url_addr + url_addr);
        Thread.sleep(2000);
        for (int i = 0; i < 1; i++) {
            webDriver.findElement(By.xpath("//a[text()='金融']")).click();
            Thread.sleep(2000);
            webDriver.findElement(By.xpath("//span[text()='研究报告']")).click();
            Thread.sleep(2000);
            webDriver.findElement(By.xpath("//a[text()='查看更多']")).click();
            Thread.sleep(2000);
            webDriver.get(base_url_addr + "/Research/IndustryList.shtml");
            Thread.sleep(2000);
        }
        List<WebElement> elements = webDriver.findElements(By.xpath("//a[text()='立即查看']"));
        int size = elements.size();
        WebElement webElement = null;
        List<String> url_addrs = new ArrayList<>();
        String urlAddr;
        for (int i = 0; i < size; i++) {
            webElement = elements.get(i);
            url_addr = webElement.getAttribute("href");
            //http://www.iresearch.com.cn/Detail/report?id=3287&isfree=0
            //http://www.iresearch.com.cn/Detail/report?id=&isfree=0
            if (url_addr.indexOf("=&") == -1) {
                urlAddr = url_addr.substring(base_url_addr.length());
                url_addrs.add(urlAddr);
            }
        }
        return url_addrs;
    }

}
