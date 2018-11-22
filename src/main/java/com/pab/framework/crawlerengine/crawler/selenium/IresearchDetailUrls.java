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

    //http://www.iresearch.com.cn/report.shtml
    public static void main(String[] args) throws InterruptedException {
        IresearchDetailUrls detailUrls=new IresearchDetailUrls();
        String base_url_addr="http://www.iresearch.com.cn";
        String url_addr="/report.shtml";
        List<String> urls = detailUrls.getUrls(base_url_addr,url_addr);
        urls.forEach(url->{
            System.out.println(url);
        });
    }

    public  List<String> getUrls(String base_url_addr,String url_addr) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(base_url_addr+url_addr);
        Thread.sleep(1000);
        webDriver.findElement(By.xpath("//span[text()='研究报告']")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.xpath("//a[text()='金融']")).click();
        Thread.sleep(1000);
        List<WebElement> elements = webDriver.findElements(By.xpath("//a[text()='立即查看']"));
        int size = elements.size();
        WebElement webElement =null;
        List<String> urls=new ArrayList<>();
        for (int i = 0; i < size; i++) {
            webElement = elements.get(i);
            url_addr=webElement.getAttribute("href");
            if (url_addr.indexOf("type=tznc")!=-1){
                urls.add(url_addr.substring(base_url_addr.length()));
            }

        }
        return  urls;

    }

}
