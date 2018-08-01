package cn.kkcoder.session;

import cn.kkcoder.utils.JedisUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 使用 shiro 的session管理器来进行管理session
 *      从写session 的方法
 */
@Component
public class MySessionDao  extends AbstractSessionDAO {

    /**
     * 超时时间 s
     */
    private  final int EXPIRE_TIME = 600;

    //使用 jedisUtils来进行redis数据库的访问
    @Autowired
    private JedisUtils jedisUtils;

    /**
     * key 的前缀
     */
    private final  String SESSION_ID_PREFIX = "cn-kkcoder:";

    /**
     * 创建 session
     */
    protected Serializable doCreate(Session session) {
        //1.从session中获取序列化的session  ID
        Serializable serializable = generateSessionId(session);//用于返回返回值
        assignSessionId(session,serializable);
        //2.以session 的 ID 和 自定义的前缀 结合，作为key 以byte[]形式 存储到redis数据库中
        byte[] key = getKey(session.getId().toString());
        //3.以整个session的经过序列化后的byte[] 的形式 作为value ， 存储到redis 数组中
        byte[] value = SerializationUtils.serialize(session);

        //存储到redis中
        jedisUtils.save(key,value);
        //设置超时时间
        jedisUtils.expire(key,EXPIRE_TIME);

        //4.返回Serializable类型的sessionid
        return serializable;
    }

    /**
     * 返回key的byte[]
     */
    private byte[] getKey(String s) {
        return (SESSION_ID_PREFIX + s ).getBytes();
    }


    /**
     * 根据序列化的session id 获取session
     * @param serializable
     * @return
     */
    protected Session doReadSession(Serializable serializable) {
        System.out.println("read session");
        if (serializable == null){
            return null;
        }
        byte[] key = getKey(serializable.toString());
        byte[] value = jedisUtils.getByKey(key);
        if (value == null) {
            return null;
        }
        return (Session) SerializationUtils.deserialize(value);
    }

    /**
     * 更新session
     * @param session
     * @throws UnknownSessionException
     */
    public void update(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null){
            byte[] key = getKey(session.getId().toString());
            byte[] value  = SerializationUtils.serialize(session);
            jedisUtils.save(key,value);
        }
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null){
            byte[] key = getKey(session.getId().toString());
            jedisUtils.delete(key);
        }
    }


    /**
     * 获得到指定的存活的session
     * @return
     */
    public Collection<Session> getActiveSessions() {
        //1.获取reids所有的session相关的的key（利用session 的key的前缀进行获取）
        Set<byte[]> keys = jedisUtils.sessionKeys(SESSION_ID_PREFIX);

        //2.循环遍历keys，获取value，并添加到session集合中
        Set<Session> value = new HashSet<Session>();
        if(CollectionUtils.isEmpty(keys)){
            return null;
        }

        for(byte[] b : keys){
            byte[] valueBbyte = jedisUtils.getByKey(b);
            value.add((Session) SerializationUtils.deserialize(valueBbyte));
        }
        return value;
    }
}
