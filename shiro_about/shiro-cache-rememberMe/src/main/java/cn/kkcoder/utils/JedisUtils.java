package cn.kkcoder.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * redis 访问的工具包
 */
@Component
public class JedisUtils {

    private JedisPool jedisPool;


    public JedisUtils() {
    }

    public JedisUtils(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }

    /**
     * 存储信息到redis
     * @param key
     * @param value
     */
    public void save(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    /**
     * 设置reids中key值对应的超时时间
     * @param key
     * @param expire_time
     */
    public void expire(byte[] key, int expire_time) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire(key,expire_time);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    /**
     * 根据key获取redis中的value值
     * @param key
     * @return
     */
    public byte[] getByKey(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        byte[] value = null;
        try {
            value = jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return value;
    }

    /**
     * 根据id删除value
     * @param key
     */
    public void delete(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    /**
     * 根据session 的key的前缀，获取所有key的集合
     * @param session_id_prefix
     * @return
     */
    public Set<byte[]> sessionKeys(String session_id_prefix) {
        Jedis jedis = jedisPool.getResource();
        Set<byte[]> keys = null;
        try {
            keys = jedis.keys((session_id_prefix + "*").getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return keys;
    }
}
