package com.mmall2.service.impl;

import com.google.common.collect.Lists;
import com.mmall2.service.IFileService;
import com.mmall2.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/4
 * @Content:
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path){
        // 上传后的 文件名 返回
        String fileName = file.getOriginalFilename();// 上传文件的原始文件名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);// 文件的扩展名
        // 上传文件的名字
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;

        logger.info("开始上传文件,上传文件的文件名:{}, 上传的路径:{}, 新文件名:{}", fileName, path, uploadFileName);

        // 首先 目录file
        File fileDir = new File(path);// 文件夹
        if(!fileDir.exists()){
            fileDir.setWritable(true);// 给这个目录设置可写 权限
            fileDir.mkdirs();

        }
        // 创建文件
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);// 文件上传成功
            // 将targetFile上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));


            // 上传之后删除upload下的文件

            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }


        return targetFile.getName();
    }

    public static void main(String[] args) {
        String fileName = "abc.fda.jpg";
        System.out.println(fileName.lastIndexOf("."));
        System.out.println(fileName.substring(fileName.lastIndexOf(".")));

    }
}
