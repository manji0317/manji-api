package com.manji.base.controller;

import com.manji.base.service.CommonService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 通用请求、工具请求
 *
 * @author Bqd
 * @since 2024/5/31 2:46
 */
@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    @Resource
    private CommonService service;

    /**
     * 校验表中字段是否唯一
     *
     * @param field 字段名
     * @param value 字段值
     * @param table 表名
     */
    @GetMapping("/fieldUnique")
    public ResponseEntity<?> fieldUnique(
            @RequestParam("field") String field,
            @RequestParam("value") String value,
            @RequestParam("table") String table
    ) {
        boolean isUnique = service.fieldUnique(field, value, table);
        if (isUnique) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(10002);
        }
    }


}
