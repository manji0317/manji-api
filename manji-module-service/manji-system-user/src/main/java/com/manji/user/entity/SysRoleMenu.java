package com.manji.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * <p>
 * 系统角色菜单关联表
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_menu")
@Builder
@AllArgsConstructor
public class SysRoleMenu extends Model<SysRoleMenu> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID
     */
    private String menuId;

}
