package cn.kkcoder.controller;

import cn.kkcoder.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户登录的controller
 */
@Controller
public class UserController {

    @RequestMapping(value = "/subLogin" , method = RequestMethod.POST)
    @ResponseBody
    public String subLogin(User user){


        return "登录成功";
    }

}
