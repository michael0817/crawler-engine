package com.pab.framework.crawlerengine.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class News implements Serializable {
    private String title;
    private String author;
    private String from;
    private String category;
    private String date;
    private String brief;
    private List<String> content;
}