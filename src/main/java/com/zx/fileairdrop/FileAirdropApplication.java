package com.zx.fileairdrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FileAirdropApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileAirdropApplication.class, args);
    }

}
