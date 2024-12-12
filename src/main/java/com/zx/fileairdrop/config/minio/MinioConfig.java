package com.zx.fileairdrop.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {


    @Autowired
    private MinioPropertie minioPropertie;

    //单例下的 MinioClient 是线程安全的，可以多个请求使用同一个客户端来调用
    @Bean
    public MinioClient getMinioClient()
    {
        return MinioClient.builder()
                .endpoint(minioPropertie.getEndpoint())
                .credentials(minioPropertie.getAccessKey(), minioPropertie.getSecretKey())
                .build();
    }
}
