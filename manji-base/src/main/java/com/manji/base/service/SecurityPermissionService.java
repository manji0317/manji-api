package com.manji.base.service;

import com.manji.base.entity.SysUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 方法鉴权
 *
 * @author Bqd
 * @since 2024/6/5 3:52
 */
@Component("sps")
@Slf4j
public class SecurityPermissionService {

    public boolean hasPermission(String permission) {
        try {
            SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return true;
        } catch (Exception e) {
            log.error("方法鉴权失败，鉴权不通过。", e);
            return false;
        }
    }

}
