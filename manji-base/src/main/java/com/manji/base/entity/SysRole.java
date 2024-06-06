package com.manji.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

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
@Builder
public class SysRole extends BaseEntity<SysRole> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

}
