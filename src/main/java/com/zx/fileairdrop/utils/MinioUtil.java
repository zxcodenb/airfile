package com.zx.fileairdrop.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.zx.fileairdrop.config.minio.MinioPropertie;
import com.zx.fileairdrop.entity.FileUploadResult;
import io.minio.*;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Component
@Slf4j
public class MinioUtil  {



    @Autowired
    private MinioClient client;

    @Autowired
    private MinioPropertie minioPropertie;




    /**
     * 检查文件是否存在
     * @param fileName
     * @return true:存在, false:不存在
     * @throws Exception
     */
    public  StatObjectResponse checkFileExist(String fileName) throws Exception {

        return client.statObject(
                StatObjectArgs.builder()
                        .bucket(minioPropertie.getBucketName())
                        .object(fileName)
                        .build()
        );

    }

    /**
     * 上传文件
     * @param multipartFile
     * @throws Exception
     */
    public FileUploadResult putFile(MultipartFile multipartFile) throws Exception {

        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(minioPropertie.getBucketName()).build());
        if (!found) {
            log.info("create bucket: [{}]", minioPropertie.getBucketName());
            client.makeBucket(MakeBucketArgs.builder().bucket(minioPropertie.getBucketName()).build());
        } else {
            log.info("bucket '{}' already exists.", minioPropertie.getBucketName());
        }

        InputStream inputStream = multipartFile.getInputStream();
        // 上传文件的名称
        String uploadName =  UUID.fastUUID().toString(true) + "_" + DateUtil.format(new Date(), "yyyy_MM_dd_HH_mm_ss") + "_" +
                multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));

        client.putObject(
                PutObjectArgs.builder()
                        .bucket(minioPropertie.getBucketName())
                        .object(uploadName)
                        .contentType(multipartFile.getContentType())
                        .stream(inputStream, multipartFile.getSize(), -1)
                        .build()
        );

        return FileUploadResult.builder()
                .bucketName(minioPropertie.getBucketName())
                .fileName(multipartFile.getOriginalFilename())
                .fileSize(multipartFile.getSize())
                .uploadName(uploadName)
                .build();
    }


    /**
     * 获取文件访问地址
     * @param bucketName
     * @param fileName
     * @param expiry
     * @return
     * @throws Exception
     */
    public  String getFileUrl(String bucketName, String fileName, int expiry) throws Exception {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .expiry(expiry)
                        .build()
        );
    }

    /**
     * 文件下载
     * @param response
     * @param fileName
     */
    public void download(HttpServletResponse response, String fileName, String realName) throws Exception {
        InputStream in=null;
        try {
            //获取文件对象
            StatObjectResponse stat =client.statObject(StatObjectArgs.builder().bucket(minioPropertie.getBucketName()).object(fileName).build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-disposition", "attachment;filename="+new String(realName.getBytes("gb2312"), "ISO8859-1" ));
            in =   client.getObject(GetObjectArgs.builder().bucket(minioPropertie.getBucketName()).object(fileName).build());
            IOUtils.copy(in,response.getOutputStream());
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * 列出指定bucket内所有文件
     *
     * @param bucketName
     * @return
     */
    public  Iterable<Result<Item>> listObj(String bucketName) {

        return client.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

    }

    /**
     * 删除指定桶中的文件
     * @param bucketName
     * @param objectName
     */
    public  void removeObject(String bucketName, String objectName) {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.info("删除文件失败{}", e.getMessage());
        }

    }


}
