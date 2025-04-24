package com.manji.user;

import com.manji.bar.BarScan;
import com.manji.base.BaseModuleScan;
import com.manji.file.FileScan;
import com.manji.websocket.WebsocketRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.manji.base.mapper", "com.manji.bar.mapper"})
@SpringBootApplication(scanBasePackageClasses = {UserModuleScan.class, BaseModuleScan.class, WebsocketRunner.class, FileScan.class, BarScan.class})
@EnableTransactionManagement
public class ManjiSystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ManjiSystemApplication.class, args);
    }
}

