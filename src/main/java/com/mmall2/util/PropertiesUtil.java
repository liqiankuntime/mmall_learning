package com.mmall2.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by geely
 * 读取配置文件
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;


    // 静态块的执行顺序是优于普通的代码块； 普通代码块执行顺序优于构造代码块
    // 静态代码块 在类被加载时执行，且只会执行一次，一般用来初始化静态变量到虚拟机里
    // 当这个类被java的 class loader加载进去时，就会先执行 static静态块
    static {
        // tomcat 启动时就要读取到这里的配置，所以用到静态块
        String fileName = "mmall2.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){

        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }



}
