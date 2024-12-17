package com.zx.fileairdrop.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpResponse;
import com.zx.fileairdrop.enums.UploadType;
import com.zx.fileairdrop.res.FileInfoRes;
import com.zx.fileairdrop.service.ShareService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("share")
public class ShareController {

    @Autowired
    private  ShareService shareService;


    @PostMapping
    public String shareFile(
            @RequestParam(value = "file" , required = true) MultipartFile file,
            @RequestParam(value = "pwd", required = false) String pwd,
            @RequestParam(value = "expire", required = false) Integer expire,
            @RequestParam(value = "maxGetCount", required = true) Integer maxGetCount
    ) {
        return shareService.upload(file, pwd, expire, maxGetCount, UploadType.FILE.name());
    }


    @GetMapping
    public FileInfoRes getInfo(
            @RequestParam(value = "takeCode", required = true) String takeCode,
            @RequestParam(value = "pwd", required = false) String pwd
    ){
        return shareService.getInfo(takeCode, pwd, UploadType.FILE.name());
    };











}
