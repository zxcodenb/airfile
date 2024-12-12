package com.zx.fileairdrop.factory;

import com.zx.fileairdrop.base.ResultCode;
import com.zx.fileairdrop.base.exception.BusinessException;
import com.zx.fileairdrop.enums.UploadType;
import com.zx.fileairdrop.service.UploadService;
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

    public static UploadService getProvider(String type) {
        final UploadType uploadType = UploadType.valueOf(type);
        return switch (uploadType) {
            case FILE -> context.getBean("fileUploadService", UploadService.class);
            case STRING -> context.getBean("contentUploadService", UploadService.class);
            case DIR -> throw BusinessException.newBusinessException(ResultCode.PARAM_IS_INVALID.getCode());
        };
    }
}

