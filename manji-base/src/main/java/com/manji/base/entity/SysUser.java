package com.manji.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-04-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends BaseEntity<SysUser> {

    /**
     * 登录账号
     */
    private String username;

    /**
     * 昵称、姓名
     */
    private String nickname;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 性别：1-男 2-女
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户状态
     */
    private Integer status;
}
