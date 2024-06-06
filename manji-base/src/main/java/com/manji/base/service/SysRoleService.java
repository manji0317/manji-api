package com.manji.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.condition.BaseCondition;
import com.manji.base.dto.RoleDTO;
import com.manji.base.entity.SysRole;
import com.manji.base.entity.SysRolePermission;
import com.manji.base.entity.SysUserRole;
import com.manji.base.mapper.SysRoleMapper;
import com.manji.base.mapper.SysUserRoleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private SysRolePermissionService sysRolePermissionService;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

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
        Map<String, List<String>> permissions = roleDTO.getPermissions();
        this.saveOrUpdateMenus(permissions, role.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * 新增或修改菜单与角色绑定
     *
     * @param roleId      角色ID
     * @param permissions 菜单、权限集合
     */
    private void saveOrUpdateMenus(Map<String, List<String>> permissions, String roleId) {
        if (permissions.isEmpty()) {
            log.info("角色没有配置菜单和权限信息，跳过处理");
            return;
        }
        List<SysRolePermission> roleMenus = new ArrayList<>();
        permissions.forEach((menuId, permissionsList) -> {
            if (permissionsList.isEmpty()) {
                roleMenus.add(SysRolePermission.builder()
                        .roleId(roleId)
                        .menuId(menuId)
                        .build());
            } else {
                permissionsList.forEach(permissionId -> roleMenus.add(SysRolePermission.builder()
                        .roleId(roleId)
                        .menuId(menuId)
                        .permissionId(permissionId)
                        .build()));
            }
        });
        sysRolePermissionService.saveBatch(roleMenus);
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
        sysRolePermissionService
                .remove(new LambdaQueryWrapper<SysRolePermission>()
                        .eq(SysRolePermission::getRoleId, roleId));

        this.saveOrUpdateMenus(roleDTO.getPermissions(), roleId);

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
        sysRolePermissionService.remove(new LambdaUpdateWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId));

        log.info("开始删除角色与用户的绑定信息");
        sysUserRoleMapper.delete(new LambdaUpdateWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));

        return ResponseEntity.ok().build();
    }

    /**
     * 根据角色ID获取角色信息
     *
     * @param roleId 角色ID
     */
    public RoleDTO getRoleById(String roleId) {
        log.info("根据角色ID获取角色信息, ID:{}", roleId);
        SysRole sysRole = this.baseMapper.selectById(roleId);
        Optional<SysRole> optionalSysRole = Optional.ofNullable(sysRole);
        return optionalSysRole.map(role -> {
            Map<String, List<String>> permissions = sysRolePermissionService.getBaseMapper()
                    .selectList(new LambdaQueryWrapper<SysRolePermission>()
                            .eq(SysRolePermission::getRoleId, roleId)
                            .select(SysRolePermission::getMenuId, SysRolePermission::getPermissionId))
                    .stream()
                    .collect(Collectors.groupingBy(SysRolePermission::getMenuId,
                            Collectors.mapping(SysRolePermission::getPermissionId, Collectors.toList())));
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setRoleName(role.getRoleName());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setPermissions(permissions);
            return roleDTO;
        }).orElse(null);
    }
}
