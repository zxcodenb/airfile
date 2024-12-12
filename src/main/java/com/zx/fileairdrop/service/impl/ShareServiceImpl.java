package com.zx.fileairdrop.service.impl;

import com.zx.fileairdrop.factory.UploadProviderFactory;
import com.zx.fileairdrop.service.ShareService;
import com.zx.fileairdrop.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ShareServiceImpl implements ShareService {


    @Override
    public String upload(MultipartFile file, String pwd, String expire, Integer maxGetCount, String type) {

        UploadService uploadService = UploadProviderFactory.getProvider(type);
        return uploadService.upload(file, pwd, expire, maxGetCount);
    }
}
