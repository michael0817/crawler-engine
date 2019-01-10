package com.pab.framework.crawlerengine.service;

import com.itextpdf.text.*;
import com.pab.framework.crawlerengine.constant.Global;
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

    @Value("${pdf.news.path}")
    private String pdfNewsFolder;
    @Autowired
    DbService dbService;

    public boolean generateNewsFile(int actionId, List<News> newsList) {
        String flowName = this.dbService.getFlowNameByActionId(actionId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = LocalDate.now().format(df);
        File folder = new File(this.pdfNewsFolder +
                (this.pdfNewsFolder.endsWith("/") ? "" : "/") + date);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String pdfFileName = folder.getAbsolutePath() + "/" + flowName + PDF_SUBFIX;
        File file = new File(pdfFileName);
        if (file.exists()) {
            file.delete();
        }
        PdfUtil pdfUtil = new PdfUtil(pdfFileName);
        Chapter chapter = PdfUtil.createChapter(flowName, 1, 1, 0, chapterFont);
        for (News news : newsList) {
            Section section = PdfUtil.createSection(chapter, news.getTitle(), sectionFont, 0);
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
        pdfUtil.closeDocument();
        return true;
    }

    public static void main(String[] args) {
        String s = "";
        char a = ' ';
        char b = ' ';
        s = "a" + a + b + "b" + a + "a" + b + "b";
        s = s.replaceAll("[\\u00a0|\\u0020][\\u00a0|\\u0020]", "c");
        System.out.println(s);
    }
}