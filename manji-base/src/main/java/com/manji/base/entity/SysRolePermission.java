package com.manji.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * <p>
 * 角色与操作权限关联表
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_permission")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission extends Model<SysRolePermission> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID或者操作权限标识
     */
    private String menuId;

    /**
     * 操作标识ID
     */
    private String permissionId;

}
