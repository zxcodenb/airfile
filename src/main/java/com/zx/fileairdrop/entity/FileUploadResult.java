package com.zx.fileairdrop.entity;

import lombok.Builder;
import lombok.Data;


/**
 * 文件上传结果
 */
@Data
@Builder
public class FileUploadResult {

    private String fileName;

    private String uploadName;

    private Long fileSize;

    private String bucketName;


}