package com.zx.fileairdrop;

import cn.hutool.core.util.RandomUtil;
import com.zx.fileairdrop.entity.FileUploadResult;
import com.zx.fileairdrop.enums.TimeUnitEnum;
import com.zx.fileairdrop.utils.MinioUtil;
import io.minio.*;
import io.minio.http.Method;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootTest
class FileAirdropApplicationTests {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MongoTemplate  mongoTemplate;

    @Test
    void contextLoads() throws  Exception{

        boolean b = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket("my-bucket")
                        .build());

        if (!b){
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket("my-bucket")
                            .build()
            );
            System.out.println("创建bucket成功");
        }
        else {
            System.out.println("bucket已存在");
        }
    }


    @Test
    void listBucket() throws Exception{

        // 列出所有的bucket
        minioClient.listBuckets().forEach(System.out::println);

        //删除bucket
        minioClient.removeBucket(
                RemoveBucketArgs.builder()
                        .bucket("my-bucket")
                        .build()
        );
    }

    /**
     * 上传文件
     * @throws Exception
     */
    @Test
    void uploadFile() throws Exception{

        //读取文件（/Users/guapi/Pictures/WechatIMG201.jpg
        FileInputStream fileInputStream = new FileInputStream("/Users/guapi/Pictures/WechatIMG201.jpg");
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("my-bucket")
                        .object("WechatIMG201.jpg")
                        .filename("/Users/guapi/Pictures/WechatIMG201.jpg")
                        .build()
        );

    }

    /**
     * 查看文件是否存在
     * @throws Exception
     */
    @Test
    void checkFileExists() throws Exception{
        StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket("my-bucket")
                        .object("WechatIMG201.jpg")
                        .build()
        );
        System.out.println(statObjectResponse);

    }

    @Test
    void getUrl () throws Exception{

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket("my-bucket")
                        .object("WechatIMG201.jpg")
                        .method(Method.GET)
                        .build()
        );
        System.out.println(url);

    }

    @Test
    void downloadFile() throws Exception{


        GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("my-bucket")
                        .object("WechatIMG201.jpg")
                        .build()
        );


        // 文件保存路径
        String filePath = "/Users/guapi/Pictures/download.jpg";

        // 将文件内容从流中读取并写入文件
        try (InputStream inputStream = response;  // GetObjectResponse 是一个流，可以直接使用
             OutputStream outputStream = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[8192];  // 每次读取8KB
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void tet() throws Exception{

        FileUploadResult fileUploadResult = FileUploadResult.builder()
                .fileName("test")
                .uploadName("test")
                .bucketName("test")
                .build();

        mongoTemplate.save(fileUploadResult);

    }




}


