package com.pab.framework.crawlerengine.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class PdfUtil {
    public static final String CHARACTOR_FONT_CH_FILE = "MSYH.TTF";
    public static final Rectangle PAGE_SIZE = PageSize.A4;
    public static final float MARGIN_LEFT = 50.0F;
    public static final float MARGIN_RIGHT = 50.0F;
    public static final float MARGIN_TOP = 50.0F;
    public static final float MARGIN_BOTTOM = 50.0F;
    public static final float SPACING = 20.0F;
    public static final String IMG_TYPE_JPG = "jpg";
    private Document document = null;

    public PdfUtil() {
    }

    public PdfUtil(String fileName) {
        createDocument(fileName);
    }


    public void createDocument(String fileName) {
        File file = new File(fileName);
        FileOutputStream out = null;
        this.document = new Document(
                PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);
        try {
            out = new FileOutputStream(file);
            PdfWriter.getInstance(this.document, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        this.document.open();
    }


    public void writeChapterToDoc(Chapter chapter) {
        try {
            if (this.document != null) {
                if (!this.document.isOpen())
                    this.document.open();
                this.document.add(chapter);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public static Chapter createChapter(String title, int chapterNum, int alignment, int numberDepth, Font font) {
        Paragraph chapterTitle = new Paragraph(title, font);
        chapterTitle.setAlignment(alignment);
        Chapter chapter = new Chapter(chapterTitle, chapterNum);
        chapter.setNumberDepth(numberDepth);
        return chapter;
    }


    public static Section createSection(Chapter chapter, String title, Font font, int numberDepth) {
        Section section = null;
        if (chapter != null) {
            Paragraph sectionTitle = new Paragraph(title, font);
            sectionTitle.setSpacingBefore(SPACING);
            section = chapter.addSection(sectionTitle);
            section.setNumberDepth(numberDepth);
        }
        return section;
    }


    public static Phrase createPhrase(String text, Font font) {
        Phrase phrase = new Paragraph(text, font);
        return phrase;
    }


    public static List createList(boolean numbered, boolean lettered, float symbolIndent) {
        List list = new List(numbered, lettered, symbolIndent);
        return list;
    }


    public static ListItem createListItem(String content, Font font) {
        ListItem listItem = new ListItem(content, font);
        return listItem;
    }


    public static Font createFont(String fontname, float size, int style, BaseColor color) {
        Font font = FontFactory.getFont(fontname, size, style, color);
        return font;
    }


    public static Font createCHineseFont(float size, int style, BaseColor color) {
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont(
                    CHARACTOR_FONT_CH_FILE, "Identity-H", true);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Font(bfChinese, size, style, color);
    }


    public void closeDocument() {
        if (this.document != null) {
            this.document.close();
        }
    }


    public void readPDF(String fileName) {
        File file = null;
        org.apache.pdfbox.io.RandomAccessRead randomAccessRead = null;
        int buff = 1048576;
        try {
            file = new File(fileName);
            if (file.exists()) {
                randomAccessRead = new RandomAccessBufferedFileInputStream(file);
                PDFParser parser = new PDFParser(randomAccessRead);
                parser.parse();
                PDDocument pdfdocument = parser.getPDDocument();
                PDFTextStripper stripper = new PDFTextStripper();
                String result = stripper.getText(pdfdocument);
                System.out.println(result);
            }
            return;
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (randomAccessRead != null) {
                try {
                    randomAccessRead.close();
                } catch (IOException localIOException2) {
                }
            }
        }
    }


    public static void main(String[] args) {
        String fileName = "/Users/fjn/Desktop/test.pdf";
        PdfUtil pdfUtil = new PdfUtil();
        Font chapterFont = createCHineseFont(20.0F, 1, new BaseColor(0, 0, 255));
        Font sectionFont = createCHineseFont(16.0F, 1, new BaseColor(0, 0, 255));
        Font textFont = createCHineseFont(10.0F, 0, new BaseColor(0, 0, 0));
        pdfUtil.createDocument(fileName);
        Chapter chapter = createChapter("adobe PDF范例", 1, 1, 0, chapterFont);
        Section section1 = createSection(chapter, "一", sectionFont, 0);
        Phrase text1 = createPhrase("你好吗", textFont);
        section1.add(text1);
        Section section2 = createSection(chapter, "二", sectionFont, 0);
        Phrase text2 = createPhrase("你好吗", textFont);
        ((Paragraph) text2).setFirstLineIndent(20.0F);
        section2.add(text2);
        List list = createList(true, false, 20.0F);
        ListItem listItem1 = createListItem("我KO你", textFont);
        ListItem listItem2 = createListItem("你OK我 ", textFont);
        list.add(listItem1);
        list.add(listItem2);
        section2.add(list);
        pdfUtil.writeChapterToDoc(chapter);
        pdfUtil.closeDocument();
    }
}