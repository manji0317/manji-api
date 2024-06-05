package com.manji.file.controller;

import com.manji.base.dto.UserImgDTO;
import com.manji.file.service.FileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用的 文件上传控制器
 *
 * @author Bqd
 * @since 2024/6/4 0:22
 */
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @Resource
    private FileService service;

    /**
     * 上传用户头像或背景图片
     */
    @PatchMapping("/updateUserImg/{userId}")
    public ResponseEntity<?> updateUserImg(@PathVariable("userId") String userId, UserImgDTO userImgDTO) {
        return service.updateUserImg(userId, userImgDTO);
    }

}
