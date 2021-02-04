package com.mmall2.controller.backend;


// 后台管理员用户的Controller


import com.mmall2.common.Const;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.User;
import com.mmall2.service.IUserService;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/26
 * @Content:
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    public IUserService iUserService;


    @RequestMapping(value = "ManageLogin.do", method = RequestMethod.POST) // 请求注解
    @ResponseBody // 返回的注解
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMINE){
                // 说明登录的是管理员；
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            }else{
                // 不是管理员；
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }
        }
        return response;
    }
}
