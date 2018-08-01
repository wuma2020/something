package cn.kkcoder.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro 的 cachemanager 实例
 */

public class CacheManager implements org.apache.shiro.cache.CacheManager {

    @Autowired
    private CachePojo cachePojo;

    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return cachePojo;
    }
}
