package com.manji.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 接收用户登录录入信息
 */
@Getter
@Setter
public class CheckLoginDTO {
    /**
     * 登录账号
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
}
