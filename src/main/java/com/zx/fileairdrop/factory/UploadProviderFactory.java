package com.zx.fileairdrop.factory;

import com.zx.fileairdrop.base.ResultCode;
import com.zx.fileairdrop.base.exception.BusinessException;
import com.zx.fileairdrop.enums.UploadType;
import com.zx.fileairdrop.service.FileService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UploadProviderFactory implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static FileService getProvider(String type) {
        final UploadType uploadType = UploadType.valueOf(type);
        return switch (uploadType) {
            case FILE -> context.getBean("fileService", FileService.class);
            case STRING -> context.getBean("contentUploadService", FileService.class);
            case DIR -> throw BusinessException.newBusinessException(ResultCode.PARAM_IS_INVALID.getCode());
        };
    }
}

