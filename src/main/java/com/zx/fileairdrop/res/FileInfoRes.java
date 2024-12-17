package com.zx.fileairdrop.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileInfoRes {

    private String  fileName;

    private String  fileSize;

}
