package com.manji.user.controller;

import com.manji.base.entity.BaseCondition;
import com.manji.user.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色处理控制层
 *
 * @author Bqd
 * @since 2024/5/24 2:40
 */
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Resource
    private SysRoleService service;

    /**
     * 查询角色列表数据
     */
    @GetMapping("/getRoleList")
    public ResponseEntity<?> getRoleList(@ModelAttribute BaseCondition condition) {
        return service.getRoleList(condition);
    }

}
