package com.manji.user.controller;

import com.manji.base.condition.UserListCondition;
import com.manji.base.dto.PasswordDTO;
import com.manji.base.dto.UserDTO;
import com.manji.base.dto.UserImgDTO;
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
     * 新增用户
     */
    @PostMapping("/")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        return service.saveUser(userDTO);
    }

    /**
     * 获取用户信息数据
     *
     * @param username 用户ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") String userId) {
        return service.getUserInfo(userId);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/getUserList")
    public ResponseEntity<?> getUserList(@ModelAttribute UserListCondition condition) {
        return service.getUserList(condition);
    }

    /**
     * 根据ID删除用户
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") String userId) {
        return service.deleteUserById(userId);
    }

    /**
     * 根据用户ID更新用户数据
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") String userId, @RequestBody UserDTO userDTO) {
        return service.updateUserById(userId, userDTO);
    }

    /**
     * 根据用户ID更新用户数据
     */
    @PatchMapping("/updatePassword/{userId}")
    public ResponseEntity<?> updatePassword(@PathVariable("userId") String userId, @RequestBody PasswordDTO passwordDTO) {
        return service.updatePassword(userId, passwordDTO);
    }

}
