package cn.kkcoder;

import cn.kkcoder.realm.PersonalReam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 测试 认证 以及 授权的测试方法
 */
public class TestPersonalRealm {
    /**
     * 测试realm 的认证方法
     */
    @Test
    public void testAuthenticationInfo(){

        //1.创建自定义的realm
        PersonalReam realm = new PersonalReam();

        //2.创建securityManager，并设置realm   这里用defaultSecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager(realm);

        //3.主体设置securitymanager 并且 获取 主体对象实例
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //4.主体设置认证信息（UsernamePasswordToken ），并登录login
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("mkk","123456");
        subject.login(usernamePasswordToken);

        System.out.println("是否成功登陆： " + subject.isAuthenticated());

        System.out.println("下面验证用户授权是否错误！");
        // 验证授权方法是否正确
        subject.checkRole("admin");//验证是否有admin角色数据
        subject.checkPermission("user:delete");//验证是否a有user：delete权限数据
    }

    @Test
    public void getAfterMD5(){
        Md5Hash md5Hash = new Md5Hash("123456","mkk");
        System.out.println(md5Hash.toString());
    }

}
