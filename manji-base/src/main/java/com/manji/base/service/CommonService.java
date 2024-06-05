package com.manji.base.service;

import com.manji.base.mapper.CommonMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通用请求、工具请求
 *
 * @author Bqd
 * @since 2024/5/31 2:48
 */
@Service
@Slf4j
public class CommonService {

    @Resource
    private CommonMapper mapper;

    /**
     * 校验表中字段是否唯一
     *
     * @param field 字段名
     * @param value 字段值
     * @param table 表名
     */
    public boolean fieldUnique(String field, String value, String table) {
        log.info("校验表中字段是否唯一, 字段名：{} | 字段值：{} | 表明： {}", field, value, table);
        int count = mapper.isFieldUnique(field, value, table);
        return count == 0;
    }
}
