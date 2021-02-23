package com.mmall2.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/20
 * @Content: 常量类
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;// 一般客户
        int ROLE_ADMINE = 1;// 管理员
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc"); // desc:降序 asc:升序
    }

    public interface Cart{
        int CHECKED = 1; // 购物车中选中状态
        int UN_CHECKED = 0;// 购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }


    public enum ProductStatusEnum{
        ON_SALE(1, "在线");

        private String value;
        private int code;
        ProductStatusEnum(int code, String value){
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
