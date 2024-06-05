package com.manji.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户菜单关联
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-04-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_menu")
@AllArgsConstructor
@NoArgsConstructor
public class SysUserMenu extends Model<SysUserMenu> {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 菜单ID
     */
    private String menuId;
}
