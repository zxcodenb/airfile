package com.zx.fileairdrop.service;

import org.springframework.web.multipart.MultipartFile;

public interface ShareService {


    String upload(MultipartFile file, String pwd, String  expire, Integer maxGetCount, String type);

}
