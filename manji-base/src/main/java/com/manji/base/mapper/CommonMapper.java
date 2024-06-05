package com.manji.base.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 通用请求、工具请求
 *
 * @author Bqd
 * @since 2024/5/31 2:52
 */
public interface CommonMapper {
    @Select("SELECT COUNT(*) FROM ${table} where ${field} = #{value}")
    int isFieldUnique(@Param("table") String table, @Param("field") String field, @Param("value") String value);
}
