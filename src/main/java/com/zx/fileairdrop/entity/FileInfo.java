package com.zx.fileairdrop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Document(collection = "file_info")
public class FileInfo  implements Serializable {

    @Id
    private String id;

    private String contentType;

    private String fileName;

    private String uploadName;

    private Long fileSize;

    private String bucketName;

    private String pwd;

    @Indexed(background = true)
    private String takeCode;

    private Integer expire;

    private String ip;

    private Integer maxGetCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastDownloadTime;


    private Boolean isDel;

}
