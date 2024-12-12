package com.zx.fileairdrop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.zx.fileairdrop.entity.FileInfo;
import com.zx.fileairdrop.entity.FileUploadResult;
import com.zx.fileairdrop.service.UploadService;
import com.zx.fileairdrop.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final MinioUtil minioUtil;

    private final MongoTemplate mongoTemplate;

    public UploadServiceImpl(MinioUtil minioUtil, MongoTemplate mongoTemplate) {
        this.minioUtil = minioUtil;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public String upload(MultipartFile file, String pwd, String expire, Integer maxGetCount) {

        Assert.notNull(file, "文件不能为空");
        log.info("start file upload");

        try {
            FileUploadResult fileUploadResult = minioUtil.putFile(file);


            final FileInfo fileInfo = FileInfo.builder()
                    .bucketName(fileUploadResult.getBucketName())
                    .contentType(file.getContentType())
                    .fileName(file.getOriginalFilename())
                    .uploadName(fileUploadResult.getUploadName())
                    .fileSize(file.getSize())
                    .takeCode(RandomUtil.randomString(4))
                    .expire(expire)
                    // TODO 获取ip
//                    .ip("")
                    .maxGetCount(maxGetCount)
                    .createTime(DateUtil.date())
                    .isDel(false)
                    .build();
            mongoTemplate.save(fileInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "";
    }


}
