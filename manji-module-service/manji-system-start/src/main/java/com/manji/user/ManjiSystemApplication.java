package com.manji.user;

import com.manji.base.BaseModuleScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.manji.base.mapper", "com.manji.user.mapper"})
@SpringBootApplication(scanBasePackageClasses = {UserModuleScan.class, BaseModuleScan.class})
@EnableTransactionManagement
public class ManjiSystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ManjiSystemApplication.class, args);
    }
}

