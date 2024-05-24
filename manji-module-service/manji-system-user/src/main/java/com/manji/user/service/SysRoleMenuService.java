package com.manji.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.entity.BaseCondition;
import com.manji.user.dto.RoleDTO;
import com.manji.user.entity.SysRole;
import com.manji.user.entity.SysRoleMenu;
import com.manji.user.mapper.SysRoleMapper;
import com.manji.user.mapper.SysRoleMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {

}
