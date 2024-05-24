package com.manji.base.basic;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目中代码生成
 */
public class CodeGenerator {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/manji?characterEncoding=UTF-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String AUTHOR = "BaiQingDong";
    private static final String PROJECT_PATH = "D:\\develop\\work\\manji-boot\\manji-api";

    /**
     * 执行 run
     */
    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig((global) -> global.author(AUTHOR))

                .packageConfig(pck -> pck.parent("com.manji")
                        .moduleName("user")
                        .pathInfo(generatorPathInfo()))

                .strategyConfig(strategy -> strategy.entityBuilder()
                        .enableActiveRecord()
                        .enableLombok()
                        .enableChainModel()
                        .enableFileOverride()
                        .logicDeleteColumnName("del_flag")

                        .controllerBuilder()
                        .enableRestStyle()
                        .enableFileOverride()

                        .serviceBuilder()
                        .enableFileOverride()

                        .mapperBuilder()
                        .enableFileOverride()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }


    private static Map<OutputFile, String> generatorPathInfo() {
        Map<OutputFile, String> pathInfo = new HashMap<>();
        pathInfo.put(OutputFile.xml, "D:\\develop\\work\\manji-boot\\manji-api\\manji-base\\src\\main\\java\\com\\manji\\user\\mapper\\xml");
        pathInfo.put(OutputFile.entity, "D:\\develop\\work\\manji-boot\\manji-api\\manji-base\\src\\main\\java\\com\\manji\\user\\entity");
        pathInfo.put(OutputFile.mapper, "D:\\develop\\work\\manji-boot\\manji-api\\manji-base\\src\\main\\java\\com\\manji\\user\\mapper");
        return pathInfo;
    }
}
