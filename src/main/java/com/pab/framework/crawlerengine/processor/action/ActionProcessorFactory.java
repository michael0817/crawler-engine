package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerengine.enums.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class ActionProcessorFactory {


    @Resource
    private ActionProcessor cookieActionProcessor;
    @Resource
    private ActionProcessor turnpageActionProcessor;
    @Resource
    private ActionProcessor newsActionProcessor;
    @Resource
    private ActionProcessor mallActionProcessor;
    @Resource
    private ActionProcessor mallturnpageActionProcessor;


    public boolean process(CrawlerActionInfo cai) {
        int actionType = cai.getActionType();
        if (ActionTypeEnum.TURNPAGE.getLabel() == actionType)
            return this.turnpageActionProcessor.actionHandler(cai);
        else if(ActionTypeEnum.MALLTURNPAGE.getLabel() == actionType)
        	return this.mallturnpageActionProcessor.actionHandler(cai);
        else if (ActionTypeEnum.COOKIE.getLabel() == actionType)
            return this.cookieActionProcessor.actionHandler(cai);
        else if (ActionTypeEnum.NEWS.getLabel() == actionType)
            return this.newsActionProcessor.actionHandler(cai);
        else if (ActionTypeEnum.MALL.getLabel() == actionType)
        	return this.mallActionProcessor.actionHandler(cai);
        return false;
    }
}