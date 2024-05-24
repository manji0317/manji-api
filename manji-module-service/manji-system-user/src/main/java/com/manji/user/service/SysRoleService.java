package com.manji.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.entity.BaseCondition;
import com.manji.user.dto.RoleDTO;
import com.manji.user.entity.SysRole;
import com.manji.user.entity.SysRoleMenu;
import com.manji.user.mapper.SysRoleMapper;
import com.manji.user.mapper.SysRoleMenuMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Slf4j
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 查询全部角色数据
     */
    public ResponseEntity<?> getRoleList(BaseCondition condition) {
        Page<SysRole> page = new Page<>(condition.getPage(), condition.getItemPrePage());
        Page<SysRole> sysRolePage = this.baseMapper.selectPage(page, new LambdaQueryWrapper<SysRole>()
                .orderByDesc(BaseEntity::getCreateTime));
        return ResponseEntity.ok(sysRolePage);
    }


    /**
     * 创建角色信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createRole(RoleDTO roleDTO) {
        log.info("创建角色开始，角色名称：{}", roleDTO.getRoleName());
        // 获取菜单信息
        List<String> menus = roleDTO.getMenus();
        SysRole role = SysRole.builder()
                .roleName(roleDTO.getRoleName())
                .description(roleDTO.getDescription())
                .build();
        this.baseMapper.insert(role);
        log.info("角色信息存储完成，开始处理菜单数据：{}", menus);
        if (!menus.isEmpty()) {
            String roleId = role.getId();
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            menus.forEach(menu -> sysRoleMenus.add(new SysRoleMenu(roleId, menu)));
            sysRoleMenuService.saveBatch(sysRoleMenus);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 修改角色信息
     * @param roleId 角色ID
     * @param roleDTO 要修改的信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateRole(Integer roleId, RoleDTO roleDTO) {
        return null;
    }


    /**
     * 根据ID删除角色信息
     * @param roleId 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteRole(Integer roleId) {
        log.info("删除角色信息开始，iD：{}", roleId);
        this.baseMapper.deleteById(roleId);

        log.info("角色信息删除成功，开始删除绑定的菜单信息");
        sysRoleMenuMapper.delete(new LambdaUpdateWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));

        return ResponseEntity.ok().build();
    }


    /**
     * 根据角色ID获取角色信息
     * @param roleId 角色ID
     */
    public ResponseEntity<?> getRoleById(String roleId) {
        log.info("根据角色ID获取角色信息, ID:{}", roleId);
        RoleDTO role = this.baseMapper.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }
}
