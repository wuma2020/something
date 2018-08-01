package cn.kkcoder.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * 自定义 shiro 的 SessionManager
 *         减少从reids 中读取session 的操作. 减小 redis 的压力
 *         即，把第一次读取到的session对象放到request中，下次读取直接从request读取即可
 *
 *         注意这里继承的是DefaultWebSessionManager      Web
 *                    不是DefaultSesionManager
 */
public class PersonalSessionManager  extends DefaultWebSessionManager {

    /**
     * 调用sessionDao从redis中读取session的方法
     *      改写此方法，从而达到从request中读取session的效果
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;

        if(sessionKey instanceof WebSessionKey){//如果sessionKey是 WebSessionKey 的类型，从中获取request
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null){//从request中获取session
             Session session = (Session)request.getAttribute("sessionId");
             if(session != null){//如果request中有此session，就返回此session
                 return  session;
             }
        }
        //如果request中没有sessionid 对应的 session。则从redis中获取,并设置到request中
        Session session = super.retrieveSession(sessionKey);
        if(request != null && sessionId != null ){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }
}
