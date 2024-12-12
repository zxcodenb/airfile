package com.zx.fileairdrop.controller;

import com.zx.fileairdrop.enums.UploadType;
import com.zx.fileairdrop.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("share")
public class ShareController {

    @Autowired
    private  ShareService shareService;


    @PostMapping
    public String shareFile(
            @RequestParam(value = "file" , required = true) MultipartFile file,
            @RequestParam(value = "pwd", required = true) String pwd,
            @RequestParam(value = "expire", required = false) String expire,
            @RequestParam(value = "maxGetCount", required = true) Integer maxGetCount
    ) {
        return shareService.upload(file, pwd, expire, maxGetCount, UploadType.FILE.name());
    }

}
