package cn.kkcoder.filter;


import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 这里是自定义的shiro filter
 *  用于验证角色  满足权限之一即可访问
 */
public class PersonalFilter extends AuthorizationFilter {

    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        //这里的object o 就是配置中的角色的数组
        String[] roles = (String[])o;
        if(roles == null || roles.length == 0){
            //说明没有角色要求
            return true;
        }
        for(String s : roles){
            //有角色要求，判断subject角色是否符合配置中要求
            if (subject.hasRole(s)){
                return  true;
            }
        }
        return false;
    }
}
