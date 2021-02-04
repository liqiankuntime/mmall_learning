package com.mmall2.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/18
 * @Content:
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)// 序列化时如果值是null时， key也会消失
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;


    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore // 忽略序列化
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return this.status;
    }
    public T getData(){
        return this.data;
    }
    public String getMsg(){
        return this.msg;
    }

    // 生成 成功信息对象 静态方法
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }
    // VS public static String ServerResponse<String> createBySuccess(){}
    // 泛型，即“参数化 类型”。
    //<T> ServerResponse<T>对于这个想了很久，也看了老师的解释，个人理解为：
    //public static ServerResponse<T> createBySuccess(){}不加第一个是声明了一个方法，一个有泛型作为返回值的函数方法，
    //然后加上第一个<T>是说声明此方法持有一个类型T，也可以理解成，将此方法声明为泛型方法，第一个记住是持有的意思，如果不持有，
    //后面的Serv<T>会报错
    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    // 生成错误信息对象
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode, errorMessage);
    }
}
