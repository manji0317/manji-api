package com.manji.user.controller;

import com.manji.base.entity.BaseCondition;
import com.manji.user.dto.RoleDTO;
import com.manji.user.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 角色处理控制层
 *
 * @author Bqd
 * @since 2024/5/24 2:40
 */
@RestController
@RequestMapping("/api/v1")
public class RoleController {

    @Resource
    private SysRoleService service;

    /**
     * 查询角色列表数据（分页查询）
     */
    @GetMapping("/role/getRolePageList")
    public ResponseEntity<?> getRolePageList(@ModelAttribute BaseCondition condition) {
        return service.getRolePageList(condition);
    }

    /**
     * 查询角色列表数据
     */
    @GetMapping("/role/getRoleList")
    public ResponseEntity<?> getRoleList() {
        return service.getRoleList();
    }

    /**
     * 根据ID获取角色信息
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable("roleId") String roleId) {
        return service.getRoleById(roleId);
    }

    /**
     * 新增角色
     */
    @PostMapping("/role")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        return service.createRole(roleDTO);
    }

    /**
     * 修改角色信息
     */
    @PatchMapping("/role/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") String roleId, @RequestBody RoleDTO roleDTO) {
        return service.updateRole(roleId, roleDTO);
    }

    /**
     * 删除角色信息
     */
    @DeleteMapping("/role/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") String roleId) {
        return service.deleteRole(roleId);
    }

}
