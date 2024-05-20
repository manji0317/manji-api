package com.manji.user.controller;

import com.manji.base.service.SysUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册（新增）、删除、查询等关于用户操作的控制层。
 *
 * @author Bai
 * @since 2024年4月28日 16点44分
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class   UserController {

    private SysUserServiceImpl service;

    /**
     * 获取用户信息数据
     *
     * @param userId 用户ID
     */
    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok().build();
    }

}
