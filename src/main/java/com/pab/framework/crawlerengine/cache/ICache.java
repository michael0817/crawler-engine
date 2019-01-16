package com.pab.framework.crawlerengine.cache;

/**
 * @author xumx
 * @date 2019/1/14
 */
public interface ICache<T> {
    public void setCache(String id, T t);

    public void removeCache(String id);

    public T getCache(String id);
}
