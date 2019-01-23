package com.pab.framework.crawlerengine.service;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.*;
import com.pab.framework.crawlerdb.domain.CrawlerContent;
import com.pab.framework.crawlerdb.service.DbService;
import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.enums.ActionTypeEnum;
import com.pab.framework.crawlerengine.util.PdfUtil;
import com.pab.framework.crawlerengine.vo.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class PdfService {

    private static Font chapterFont = PdfUtil.createCHineseFont(20.0F, 1, new BaseColor(0, 0, 0));
    private static Font sectionFont = PdfUtil.createCHineseFont(16.0F, 1, new BaseColor(0, 0, 255));
    private static Font briefFont = PdfUtil.createCHineseFont(10.0F, 0, new BaseColor(0, 0, 255));
    private static Font textFont = PdfUtil.createCHineseFont(10.0F, 0, new BaseColor(0, 0, 0));
    private static final String PDF_SUBFIX = ".pdf";

    @Value("${news.path}")
    private String pdfNewsFolder;
    @Autowired
    DbService dbService;

    public boolean generateNewsFile() {
        PdfUtil pdfUtil = null;
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date = localDate.format(df);
            File folder = new File(this.pdfNewsFolder +
                    (this.pdfNewsFolder.endsWith("/") ? "" : "/") + date);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String pdfFileName = folder.getAbsolutePath() + "/" + Global.NEWS_PDF_FILE_NAME + "_" + date + PDF_SUBFIX;
            log.info("开始生成PDF:"+pdfFileName);
            File file = new File(pdfFileName);
            if (file.exists()) {
                file.delete();
            }
            pdfUtil = new PdfUtil(pdfFileName);
            List<Integer> newsActionList = dbService.getActionIdByActionType(ActionTypeEnum.NEWS.getLabel());
            for (int actionId : newsActionList) {
                String flowName = this.dbService.getFlowNameByActionId(actionId);
                Chapter chapter = PdfUtil.createChapter(flowName, 1, 1, 0, chapterFont);
                chapter.setBookmarkTitle(flowName);
                chapter.setBookmarkOpen(true);
                List<CrawlerContent> contentList = dbService.getContentByActionIdAndCrawlerDate(actionId, localDate);
                if(contentList.size()==0){
                    continue;
                }
                for (CrawlerContent crawlerContent : contentList) {
                    News news = JSON.parseObject(crawlerContent.getContent(), News.class);
                    Section section = PdfUtil.createSection(chapter, news.getTitle(), sectionFont, 1);
                    Phrase breifLine = PdfUtil.createPhrase(news.getBrief(), briefFont);
                    Phrase dateLine = PdfUtil.createPhrase(news.getDate(), briefFont);
                    section.add(breifLine);
                    section.add(dateLine);
                    for (String str : news.getContent())
                        if ((StringUtils.isNotBlank(str)) && (str.length() >= 4) && (
                                (Global.WWW_FLAG.equalsIgnoreCase(str.substring(0, 3))) ||
                                        (Global.HTTP_FLAG.equalsIgnoreCase(str.substring(0, 4))))) {
                            Chunk chunk = new Chunk(str, briefFont);
                            chunk.setAnchor(str);
                            Paragraph paragraph = new Paragraph(chunk);
                            section.add(paragraph);
                        } else {
                            Phrase textLine = PdfUtil.createPhrase(str, textFont);
                            section.add(textLine);
                        }
                }
                pdfUtil.writeChapterToDoc(chapter);
            }
            log.info("PDF文件生成结束");
        }catch (Exception e){
            log.error("PDF文件生成失败", e);
        }finally {
            pdfUtil.closeDocument();
        }
        return true;
    }
}