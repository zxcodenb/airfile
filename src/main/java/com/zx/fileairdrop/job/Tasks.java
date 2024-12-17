package com.zx.fileairdrop.job;


import com.zx.fileairdrop.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Tasks {

    private final ShareService shareService;

    public Tasks(ShareService shareService) {
        this.shareService = shareService;
    }


    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void deleteExpireFile(){

        log.info("文件清理 ========= start");
        shareService.deleteExpireFile();
        log.info("文件清理 ========= end");

    }


}
