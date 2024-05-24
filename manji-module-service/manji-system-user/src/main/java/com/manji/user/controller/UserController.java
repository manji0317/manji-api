package com.manji.user.controller;

import com.manji.base.condition.UserListCondition;
import com.manji.base.entity.SysUser;
import com.manji.base.service.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册（新增）、删除、查询等关于用户操作的控制层。
 *
 * @author Bai
 * @since 2024年4月28日 16点44分
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserDetailServiceImpl service;

    /**
     * 获取用户信息数据
     *
     * @param username 用户ID
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable("username") String username) {
        return service.getUserInfo(username);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/getUserList")
    public ResponseEntity<?> getUserList(@ModelAttribute UserListCondition condition) {
        return service.getUserList(condition);
    }

    /**
     * 查询用户列表
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") Integer userId) {
        return service.deleteUserById(userId);
    }

    /**
     * 根据用户ID更新用户数据
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") Integer userId, @RequestBody SysUser sysUser) {
        return service.updateUserById(userId, sysUser);
    }
}
