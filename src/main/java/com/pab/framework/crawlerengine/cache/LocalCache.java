package com.pab.framework.crawlerengine.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xumx
 * @date 2019/1/14
 */
@Component
public class LocalCache implements ICache {

    private ConcurrentHashMap<String,Object> localCache = new ConcurrentHashMap();

    @Override
    public void setCache(String id, Object o) {
        localCache.put(id,o);
    }

    @Override
    public void removeCache(String id) {
        localCache.remove(id);
    }

    @Override
    public Object getCache(String id) {
        return localCache.get(id);
    }
}
