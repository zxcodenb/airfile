package com.zx.fileairdrop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.zx.fileairdrop.base.ResultCode;
import com.zx.fileairdrop.base.exception.BusinessException;
import com.zx.fileairdrop.entity.FileInfo;
import com.zx.fileairdrop.entity.FileUploadResult;
import com.zx.fileairdrop.res.FileInfoRes;
import com.zx.fileairdrop.service.FileService;
import com.zx.fileairdrop.utils.MinioUtil;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;


@Service("fileService")
@Slf4j
public class FileServiceImpl implements FileService {

    private final MinioUtil minioUtil;

    private final MongoTemplate mongoTemplate;

    public FileServiceImpl(MinioUtil minioUtil, MongoTemplate mongoTemplate) {
        this.minioUtil = minioUtil;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile file, String pwd, Integer expire, Integer maxGetCount) {

        Assert.notNull(file, "文件不能为空");
        log.info("start file upload");
        final  String takeCode = RandomUtil.randomString(4);
        final Date now = DateUtil.date();

        try {
            FileUploadResult fileUploadResult = minioUtil.putFile(file);

            final FileInfo fileInfo = FileInfo.builder()
                    .bucketName(fileUploadResult.getBucketName())
                    .contentType(file.getContentType())
                    .fileName(file.getOriginalFilename())
                    .uploadName(fileUploadResult.getUploadName())
                    .fileSize(file.getSize())
                    .takeCode(takeCode)
                    .expire(expire)
                    .pwd(pwd)
                    // TODO 获取ip
//                    .ip("")
                    .maxGetCount(maxGetCount)
                    .createTime(DateUtil.date())
                    .lastDownloadTime(DateUtil.offsetHour(now, expire))
                    .isDel(false)
                    .build();
            mongoTemplate.save(fileInfo);
        }catch (IOException e) {
            log.error("file upload error.", e);
            throw BusinessException.newBusinessException(ResultCode.FILE_IO_ERROR.getCode());
        } catch (ServerException e) {
            log.error("minio server error.", e);
            throw BusinessException.newBusinessException(ResultCode.MINIO_SERVER_ERROR.getCode());
        } catch (InsufficientDataException e) {
            log.error("insufficient data throw exception", e);
            throw BusinessException.newBusinessException(ResultCode.MINIO_INSUFFICIENT_DATA.getCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.newBusinessException(ResultCode.KNOWN_ERROR.getCode());
        }
        log.info("end file upload");
        return takeCode;
    }

    @Override
    public FileInfoRes getInfo(String takeCode,  String pwd) {

        Query query = new Query(Criteria.where("takeCode").is(takeCode)); //.and("isDel").is(false));
        final FileInfo fileInfo = mongoTemplate.findOne(query, FileInfo.class);
        Assert.notNull(fileInfo, "文件不存在");

        // 验证密码
//        if (fileInfo != null && !pwd.equals(fileInfo.getPwd())) {
//            throw BusinessException.newBusinessException(ResultCode.PWD_ERROR.getCode());
//        }

        //构建返回对象
        return FileInfoRes.builder()
                .fileName(fileInfo.getFileName())
                .fileSize(setSize(fileInfo.getFileSize()))
                .build();
    }

    public String setSize(Long size) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

}
