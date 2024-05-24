package com.manji.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manji.base.condition.UserListCondition;
import com.manji.base.dto.UserDTO;
import com.manji.base.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-04-28
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据查询条件，查询数据库中符合条件的用户信息
     */
    UserDTO getUserInfo(@Param("username") String username);

    Page<SysUser> getUserList(@Param("condition") UserListCondition condition, @Param("page") IPage<SysUser> page);
}
