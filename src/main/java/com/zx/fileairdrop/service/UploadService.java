package com.zx.fileairdrop.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {


    /**
     * 文件上传
     * @param file 文件
     * @param pwd  密码
     * @param expire 有效期
     * @param maxGetCount 最大下载次数
     * @return 分享码
     */
    String upload(MultipartFile file, String pwd, String expire, Integer maxGetCount);
}
