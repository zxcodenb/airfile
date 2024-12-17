package com.zx.fileairdrop.service.impl;

import com.zx.fileairdrop.factory.UploadProviderFactory;
import com.zx.fileairdrop.res.FileInfoRes;
import com.zx.fileairdrop.service.FileService;
import com.zx.fileairdrop.service.ShareService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ShareServiceImpl implements ShareService {


    @Override
    public String upload(MultipartFile file, String pwd, Integer expire, Integer maxGetCount, String type) {

        FileService fileService = UploadProviderFactory.getProvider(type);
        return fileService.upload(file, pwd, expire, maxGetCount);
    }

    @Override
    public FileInfoRes getInfo(String takeCode, String pwd , String type) {

        FileService fileService = UploadProviderFactory.getProvider(type);
        return fileService.getInfo(takeCode, pwd);
    }


    /**
     * 定时任务删除过期文件
     */
    @Override
    public void deleteExpireFile() {


    }
}
