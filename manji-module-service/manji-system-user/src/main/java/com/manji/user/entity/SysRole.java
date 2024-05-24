package com.manji.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 系统角色
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole extends BaseEntity<SysRole> {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
