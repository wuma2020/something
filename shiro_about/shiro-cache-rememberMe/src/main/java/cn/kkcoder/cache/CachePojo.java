package cn.kkcoder.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  shiro cache接口的实现类  用于被shiro cachemanager 调用
 *
 *  这里存放cache的缓存数据
 *
 * @param <K>
 * @param <V>
 */
@Component
public class CachePojo<K,V> implements Cache<K,V> {

    /**
     * 用于存放缓存数据的map集合
     */
    Map<Object,Object> cacheMap = new HashMap<Object, Object>(32);

    public V get(K k) throws CacheException {
        System.out.println("从cache中获取数据");
        return (V)cacheMap.get(k);
    }

    public V put(K k, V v) throws CacheException {
        cacheMap.put(k,v);
        return v;
    }

    public V remove(K k) throws CacheException {
        return (V)cacheMap.remove(k);
    }

    public void clear() throws CacheException {
        cacheMap.clear();
    }

    public int size() {
        return cacheMap.size();
    }

    public Set<K> keys() {
        return (Set<K>)cacheMap.keySet();
    }

    public Collection<V> values() {
        return (Collection<V>) cacheMap.values();
    }
}
