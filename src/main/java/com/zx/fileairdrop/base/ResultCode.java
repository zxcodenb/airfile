package com.zx.fileairdrop.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(1, "成功"),
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETED(1004, "参数缺失"),
    FILE_NOT_EXIST(1005, "文件不存在"),
    PWD_ERROR(1006, "密码错误"),
    USER_NOT_HAS_AUTH(2006, "没有访问权限"),
    KNOWN_ERROR(3001, "未知异常，请联系管理员"),
    FILE_IO_ERROR(4001, "文件io异常"),
    MINIO_SERVER_ERROR(4002, "文件服务器异常"),
    MINIO_INSUFFICIENT_DATA(4003, "文件服务器资源不足"),
    ;

    private Integer code;

    private String message;


    public static ResultCode getByCode(Integer code) {
        for (ResultCode value : ResultCode.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}