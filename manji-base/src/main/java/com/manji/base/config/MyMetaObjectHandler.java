package com.manji.base.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.manji.base.entity.SysUserDetails;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mybatis plus 配置文件
 * 自动填充表中的数据
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时的填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = sysUserDetails.getUserId();
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", now, metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    /**
     * 更新时的填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("updateTime", now, metaObject);
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = sysUserDetails.getUserId();
        this.setFieldValByName("updateBy", userId, metaObject);
    }
}