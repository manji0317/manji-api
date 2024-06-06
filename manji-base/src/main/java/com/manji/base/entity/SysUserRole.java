package com.manji.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role")
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole extends Model<SysUserRole> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

}
