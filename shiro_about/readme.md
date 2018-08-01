shiro-spring 知识点整理

----------

> 简单的配置就不说了，就记录一下shiro相关技术的大致实现，具体看源码

# Shiro认证，授权，自定义Realm
 
 1. web.xml中，添加DelegatingFilterProxy 的过滤器.
 2. spring 配置文件中添加 上述过滤器对应的bean. shiroFilter
 3. 在上述bean中，添加的securityManager
 4. 在securityManager中添加realm对象（这里自定义，自己写认证授权的具体方法）
    1. 自定义realm 继承 AuthorizingRealm 
    2. 实现 doGetAuthenticationInfo（认证） 和 doGetAuthorizationInfo（授权）方法   从数据库中读取相关数据

# shiroFilter的相关配置
```xml

<!--roles 和 perms 为默认的过滤器 -->
<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean" >
        <property name="securityManager" ref="securityManager"/>
        <property name="loginUrl" value="login.jsp"/>
        <property name="unauthorizedUrl" value="403.html"/>
        <property name="filterChainDefinitions" >
            <value>
                /login.jsp = anon
                /subLogin = anon
                /testRoles1 = roles["admin","user"]
                /testPermission = perms["user:delete"]
                /testPermission1 = perms["user:delete","user:add"]
                /* = authc
            </value>
        </property>
    </bean>
```

# 自定义过滤器

```java
    public class PersonalFilter extends AuthorizationFilter { 
    //重写这个方法即可
    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
      
      }
    }
    /////最后别忘了注入到spring 配置文件中（如果使用的话）
```

# shiro 会话管理
使用默认的sessionmanager或者自定义sessionmanager
    
    1.  默认sessionmanager
```
<!--sessionDao 为自定义的操作数据库的类-->
<bean id="sessionManager" class="org.apache.shiro.web.session.mgt.DefaultWebSessionManager" >
<property name="sessionDAO" ref="sessionDao" />
</bean>
```

```
//实现相关接口即可
public class MySessionDao  extends AbstractSessionDAO {}
```

    2. 自定义sessionManager
```
//从写 retrieveSession 方法，修改读取session的次数
//这里是把读取到的session方法request中，之后在读去session直接从request中读取即可
public class PersonalS
essionManager  extends DefaultWebSessionManager {}
```

# shiro 缓存和自动登陆

## 缓存
```java
/**     1.实现CacheManager接口          */
public class CacheManager implements org.apache.shiro.cache.CacheManager {

    //CachePojo 为自定义的缓存处理的类，下面讲
    @Autowired
    private CachePojo cachePojo;

    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return cachePojo;
    }

//----------------------------------------

/** 这里具体对缓存进行处理，即设置缓存和读取缓存  
    我是直接用一个map来进行缓存数据
*/
public class CachePojo<K,V> implements Cache<K,V> {

}

```

## 自动登陆
> 直接将相关配置设置的securityManager中即可
```xml
 <!--设置自动登陆的remeberme 的相关 bean-->
        <!--设置 shiro 的 cookieRemenberMeManager -->
    <bean class="org.apache.shiro.web.mgt.CookieRememberMeManager" id="rememberMeManager" >
        <property name="cookie" ref="simpleCookie" />
     </bean>
        <!--设置 shiro 的 SimpleCookie 对象 -->
    <bean id="simpleCookie" class="org.apache.shiro.web.servlet.SimpleCookie" >
        <constructor-arg value="rememberMe" />
        <property name="maxAge" value="420000" />
    </bean>
```