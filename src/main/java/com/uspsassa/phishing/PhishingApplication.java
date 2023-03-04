package com.uspsassa.phishing;

import cn.hutool.extra.spring.SpringUtil;
import com.uspsassa.phishing.service.FishService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhishingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhishingApplication.class, args);
        init();
    }
    //初始化处理队列 删除处理队列
    public static void  init(){
        FishService bean = SpringUtil.getBean(FishService.class);
        //初始化处理队列 removeAll
        bean.removeFish();
    }

}

