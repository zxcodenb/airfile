package com.zx.fileairdrop.service;

import com.zx.fileairdrop.res.FileInfoRes;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ShareService {


    String upload(MultipartFile file, String pwd, Integer  expire, Integer maxGetCount, String type);

    void deleteExpireFile();

    FileInfoRes getInfo(String takeCode, String pwd ,  String type);

}
