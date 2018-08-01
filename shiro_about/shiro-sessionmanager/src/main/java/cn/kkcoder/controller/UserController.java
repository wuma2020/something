package cn.kkcoder.controller;

import cn.kkcoder.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户登录的controller
 */
@Controller
public class UserController {

    /**
     *  produces = "application/json;charset=utf-8" 设置编码格式为utf-8
     */
    @RequestMapping(value = "/subLogin" , method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody()
    public String subLogin(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try {
            //这里认证错误会报错，所以try catch 一下
            subject.login(usernamePasswordToken);
        }catch (Exception e){
           e.printStackTrace();
            return "登录失败";
        }
        Boolean isAdmin =  subject.hasRole("admin");
        return "登录成功  :  " + isAdmin + "admin权限";
    }


    //-------------测试角色和权限方法------------------
    @RequestMapping("/testRoles1")
    @ResponseBody
    public String testRoles1(){
        return "testRoles1 成功 ！";
    }

    @RequestMapping("/testRoles")
    @ResponseBody
    public String testRoles(){
        return "testRoles 成功 ！";
    }

    @RequestMapping("/testPermission1")
    @ResponseBody
    public String testPermission1(){
        return "testPermission1 成功 ！";
    }

    @RequestMapping("/testPermission")
    @ResponseBody
    public String testPermission(){
        return "testPermission 成功 ！";
    }

}
