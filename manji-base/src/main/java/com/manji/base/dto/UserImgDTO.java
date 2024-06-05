package com.manji.base.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 接收前端传递的用户图片数据
 *
 * @author Bqd
 * @since 2024/6/4 3:11
 */
@Getter
@Setter
public class UserImgDTO {
    private String userId;
    private String uploadType;
    private MultipartFile file;
}
