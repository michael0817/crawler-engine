package com.pab.framework.crawlerengine.dto;

import com.pab.framework.crawlerengine.enums.ActionTypeEnum;
import com.pab.framework.crawlerengine.enums.UrlTypeEnum;
import lombok.Data;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * @author xumx
 * @date 2019/1/3
 */
@Data
public class CrawlerJobInfo {

    private List<String> urls;
    private Integer actionType;
    private Integer urlType;
    private String regex;
    private List<Cookie> cookies;
}
