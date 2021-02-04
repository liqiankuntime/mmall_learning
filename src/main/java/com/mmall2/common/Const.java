package com.mmall2.common;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/20
 * @Content:
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;// 一般客户
        int ROLE_ADMINE = 1;// 管理员
    }
}
