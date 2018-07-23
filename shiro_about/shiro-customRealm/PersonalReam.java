package cn.kkcoder.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 定制的realm
 */
public class PersonalReam  extends AuthorizingRealm {

    Map<String,String> userMap = new HashMap<String,String>(16);
    {
        userMap.put("mkk","123456");
        super.setName("PersonRealm");
    }

    //这个是用来做授权的方法
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String)principalCollection.getPrimaryPrincipal();

        // 模拟 通过用户名从数据库或者缓存中获取角色数据 和 权限数据
        Set<String> roles = getRolesByUsername();
        Set<String> permissions = getPerssionsByUsername();

        //把用户数据和权限数据放到SimpleAuthorizationInfo中，用于授权验证的工作
        SimpleAuthorizationInfo simpleAuthorizationInfo  = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 模拟从数据库中获取权限数据
     * @return 获取到的权限数据的set集合
     */
    private Set<String> getPerssionsByUsername() {
        Set<String> permissions = new HashSet<String>();
        permissions.add("user:add");
        permissions.add("user:delete");
        return permissions;
    }

    /**
     * 模拟从数据库中获取角色数据
     * @return 获取到的角色数据的set集合
     */
    private Set<String> getRolesByUsername() {
        Set<String> roles = new HashSet<String>();
        roles.add("admin");
        roles.add("user");
        return roles;
    }

    //这个是用来做认证的方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // authenticationToken 是主体传过来的用户信息 getPrincipal() 获取用户名信息
        String username = (String)authenticationToken.getPrincipal();

        // 根据用户名，从数据库或者缓存中获取密码
        String password = getPasswordByUserName(username);
        if( password == null ){
            return null;
        }

        // 这里说明用户名和密码都存在，所以创建一个AuthenticationInfo对象来用于返回，并设置好用户名和密码
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,password,"PersonRealm");
        return authenticationInfo;
    }

    /**
     * 模拟数据库获取密码
     * @param username
     * @return
     */
    private String getPasswordByUserName(String username) {
        return userMap.get(username);
    }
}
