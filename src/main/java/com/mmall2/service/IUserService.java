package com.mmall2.service;

import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.User;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/18
 * @Content:
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkvalid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword( String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdminRole(User user);
}
