package com.mmall2.service.impl;

import com.mmall2.common.Const;
import com.mmall2.common.ServerResponse;
import com.mmall2.common.TokenCache;
import com.mmall2.dao.UserMapper;
import com.mmall2.pojo.User;
import com.mmall2.service.IUserService;
import com.mmall2.util.MD5Util;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/18
 * @Content: 要把service注入到controler中供controler使用；注入前先声明 @Service("iUserService")
 */

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    // 用户登录
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户不存在！");
        }
        //todo 密码登录 MD5
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功！", user);
    }

    // 用户注册
    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkvalid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()){// todo 疑惑点
            return validResponse;
        }
        validResponse = this.checkvalid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()){// todo 疑惑点
            return validResponse;
        }


//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount>0){
//            return ServerResponse.createByErrorMessage("用户名已经存在");
//        };
//        resultCount = userMapper.checkEmail(user.getEmail());
//        if(resultCount>0){
//            return ServerResponse.createByErrorMessage("email已存在");
//        };

        // 若走到这里，说明用户要注册了
        user.setRole(Const.Role.ROLE_CUSTOMER);
            // 密码加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkvalid(String str, String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("用户名已经存在");
                };
            };
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("email已存在");
                };
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        // 为啥这里还要return一下呢
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse selectQuestion(String username){
        ServerResponse validResponse = this.checkvalid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccessMessage(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponse checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);

        if(resultCount > 0){
            // 说明问题以及问题答案是这个用户的，且正确
            String forgetToken = UUID.randomUUID().toString();
            // 把forgetToken放到本地cache中，并设置有效期
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkvalid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(forgetToken, token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("重置密码成功");
            }
        }else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    // 登录状态的重置密码
    public ServerResponse<String> resetPassword( String passwordOld, String passwordNew, User user){
        // 防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户，
        // 因为我们会查询一个count(1)，如果不指定一个id那么就会出问题
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if( resultCount == 0 ){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        // 进到这里就说嘛可以设置新密码了
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createBySuccessMessage("密码更新失败");
    }

    // 更新个人用户信息
    public ServerResponse<User> updateInformation(User user){
        // username是不能被更新的
        // email也要进行一个校验，校验email是不是存在别的用户里
        int emailCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(emailCount > 0){
            return ServerResponse.createByErrorMessage("email已经存在，请更换email");
        }
        // todo 疑问
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount >0){
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);

        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    // backend

    // 校驗是不是管理員
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMINE){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}




















