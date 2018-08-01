package cn.kkcoder.realm;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();

        // 从数据库或者缓存中获取角色数据
//        Set<String> roles = getRolesByUserName(username);

        // 从数据库或者缓存中获取权限数据
        Set<String> permissions = getPermissionsByUserName(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String username) {
        Set<String> sets = new HashSet<String>();
        sets.add("user:add");
        sets.add("user:delete");
        sets.add("user:post");
        sets.add("user:query");
        return sets;
    }


    public static void main(String[] args) {
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}