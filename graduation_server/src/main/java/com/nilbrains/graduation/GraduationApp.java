package com.nilbrains.graduation;

import com.nilbrains.graduation.utils.IdWorker;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GraduationApp {
    //    APP 启动类
    public static void main(String[] args) {
        // SpringApplication.run(GraduationApp.class,args);
        SpringApplication springApplication = new SpringApplication(GraduationApp.class);
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        springApplication.run(args);
    }

    @Bean
    public IdWorker createIdWorker() {
        return new IdWorker(0, 0);
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
