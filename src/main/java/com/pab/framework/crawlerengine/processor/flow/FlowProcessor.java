package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerengine.processor.BaseProcessor;

import java.io.IOException;

/**
 *
 * @author xumx
 * @date 2018/11/16
 */
public interface FlowProcessor extends BaseProcessor {

    void run() throws IOException;


}
