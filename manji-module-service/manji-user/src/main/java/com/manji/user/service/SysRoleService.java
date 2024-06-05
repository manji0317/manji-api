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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 查询全部角色数据（分页查询）
     */
    public ResponseEntity<?> getRolePageList(BaseCondition condition) {
        Page<SysRole> page = new Page<>(condition.getPage(), condition.getItemPrePage());
        Page<SysRole> sysRolePage = this.baseMapper.selectPage(page, new LambdaQueryWrapper<SysRole>()
                .orderByDesc(BaseEntity::getCreateTime));
        return ResponseEntity.ok(sysRolePage);
    }

    /**
     * 查询全部角色数据
     */
    public ResponseEntity<?> getRoleList() {
        List<SysRole> sysRoles = this.baseMapper.selectList(null);
        return ResponseEntity.ok(sysRoles);
    }


    /**
     * 创建角色信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createRole(RoleDTO roleDTO) {
        log.info("创建角色开始，角色名称：{}", roleDTO.getRoleName());
        // 获取菜单信息
        SysRole role = SysRole.builder()
                .roleName(roleDTO.getRoleName())
                .description(roleDTO.getDescription())
                .build();
        this.baseMapper.insert(role);
        log.info("角色信息存储完成，开始处理菜单数据");
        this.saveOrUpdateMenus(role.getId(), roleDTO.getMenus());
        return ResponseEntity.ok().build();
    }

    /**
     * 新增或修改菜单与角色绑定
     *
     * @param roleId 角色ID
     * @param menus  菜单集合
     */
    private void saveOrUpdateMenus(String roleId, List<String> menus) {
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        menus.forEach(menu -> sysRoleMenus.add(new SysRoleMenu(roleId, menu)));
        sysRoleMenuService.saveBatch(sysRoleMenus);
    }

    /**
     * 修改角色信息
     *
     * @param roleId  角色ID
     * @param roleDTO 要修改的信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateRole(String roleId, RoleDTO roleDTO) {
        log.info("修改角色信息开始，ID:{}", roleId);
        this.baseMapper.update(SysRole.builder()
                        .roleName(roleDTO.getRoleName())
                        .description(roleDTO.getDescription())
                        .build(),
                new LambdaUpdateWrapper<SysRole>()
                        .eq(BaseEntity::getId, roleId));
        log.info("角色信息修改完毕，开始更新绑定的菜单信息");
        sysRoleMenuService
                .remove(new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, roleId));
        this.saveOrUpdateMenus(roleDTO.getId(), roleDTO.getMenus());

        return ResponseEntity.ok().build();
    }


    /**
     * 根据ID删除角色信息
     *
     * @param roleId 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteRole(String roleId) {
        log.info("删除角色信息开始，iD：{}", roleId);
        this.baseMapper.deleteById(roleId);

        log.info("角色信息删除成功，开始删除绑定的菜单信息");
        sysRoleMenuService.remove(new LambdaUpdateWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));

        return ResponseEntity.ok().build();
    }


    /**
     * 根据角色ID获取角色信息
     *
     * @param roleId 角色ID
     */
    public ResponseEntity<?> getRoleById(String roleId) {
        log.info("根据角色ID获取角色信息, ID:{}", roleId);
        RoleDTO role = this.baseMapper.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    /**
     * 根据角色ID集合查询菜单信息
     */
    public ResponseEntity<?> getMenusByRoleIds(List<String> roleIds) {
        log.info("根据角色ID集合查询菜单信息, ID集合:{}", roleIds);

        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.getBaseMapper()
                .selectList(new LambdaQueryWrapper<SysRoleMenu>()
                        .in(SysRoleMenu::getRoleId, roleIds)
                        .select(SysRoleMenu::getMenuId));
        // 去重菜单ID
        Set<String> menuIds = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        return ResponseEntity.ok(menuIds);
    }
}
