package com.manji.user.mapper;

import com.manji.user.dto.RoleDTO;
import com.manji.user.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 系统角色 Mapper 接口
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    RoleDTO getRoleById(@Param("roleId") String roleId);
}
