package com.manji.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 修改密码接收实体类
 *
 * @author Bqd
 * @since 2024/5/30 3:48
 */
@Getter
@Setter
public class PasswordDTO {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
