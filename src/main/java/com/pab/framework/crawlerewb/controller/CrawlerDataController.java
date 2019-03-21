package com.pab.framework.crawlerewb.controller;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerdb.domain.CrawlerContent;
import com.pab.framework.crawlerdb.service.DbService;
import com.pab.framework.crawlerengine.model.ContentInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author xumx
 * @date 2019/1/21
 */
@Controller
@RequestMapping("/content")
@Slf4j
public class CrawlerDataController {

    @Autowired
    private DbService dbService;
    @Value("${news.path}")
    private String newPath;

    /**
     * 下载爬虫数据
     * @param crawlerDate
     * @param actionType
     * @param pageStart
     * @param pageEnd
     * @return
     */
    @ResponseBody
    @GetMapping("/sync/{crawlerDate}/{actionType}/{pageStart}/{pageEnd}")
    public ContentInfo sync(@PathVariable("crawlerDate") String crawlerDate,
                              @PathVariable("actionType") int actionType,
                              @PathVariable("pageStart") int pageStart,
                              @PathVariable("pageEnd") int pageEnd) { 
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(crawlerDate,dtf);
        int totalCount = this.dbService.CountNum(actionType, localDate);
        int totalPageNum = 0;
        if(totalCount%(Global.CONTENT_PAGE_SIZE) == 0) {
        	totalPageNum = totalCount/(Global.CONTENT_PAGE_SIZE);
        }else if(totalCount%(Global.CONTENT_PAGE_SIZE) != 0) {
        	totalPageNum = totalCount/(Global.CONTENT_PAGE_SIZE) + 1;
        }
        int start;
        int count = 0;
        if(pageStart > 0) {
        	start = (pageStart-1) * (Global.CONTENT_PAGE_SIZE);
        }else {
        	start = 0;
        }
        ContentInfo contentInfo = new ContentInfo();
        if(pageStart <= pageEnd) {
        	if(pageEnd < totalPageNum) {
        		count  = pageEnd * (Global.CONTENT_PAGE_SIZE) - start;
        	}else if(pageEnd >= totalPageNum) {
        		count = totalCount - start;
        	}
        	List<CrawlerContent> contentList =  dbService.getContentByActionTypeAndCrawlerDateForPage(actionType, localDate, start, count);
        	contentInfo.setTotalCount(totalCount);
        	contentInfo.setCrawlerContent(contentList);
        	contentInfo.setTotalPageNum(totalPageNum);
        	contentInfo.setPagePerNum(Global.CONTENT_PAGE_SIZE);
        }
        return contentInfo;
    }

    /**
              *下载新闻pdf
     * @param crawlerDate
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download/news/{crawlerDate}")
    public String download(@PathVariable("crawlerDate") String crawlerDate,
                           HttpServletRequest request,
                           HttpServletResponse response){
        File pdfFile = null;
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(crawlerDate,dtf);
            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            File folder = new File(this.newPath+"/"+localDate.format(dtf1));
            for(File pdf : folder.listFiles()){
                if(pdf.getName().contains(Global.NEWS_PDF_FILE_NAME)){
                    pdfFile = pdf;
                }
            }
            response.setContentType("text/html;charset=utf-8");
            request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(pdfFile!=null) {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                long fileLength = pdfFile.length();
                response.setContentType("application/x-msdownload;");
                response.setHeader("Content-disposition", "attachment; filename* = UTF-8''" + URLEncoder.encode
                        (pdfFile.getName(), "UTF-8").replaceAll("\\+", "%20"));
                response.setHeader("Content-Length", String.valueOf(fileLength));
                bis = new BufferedInputStream(new FileInputStream(pdfFile));
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) try {
                    bis.close();
                } catch (IOException e) {
                }
                if (bos != null) try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
