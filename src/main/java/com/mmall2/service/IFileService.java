package com.mmall2.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/4
 * @Content:
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
