package com.manji.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.entity.BaseCondition;
import com.manji.user.entity.SysRole;
import com.manji.user.mapper.SysRoleMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-05-24
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    /**
     * 查询全部角色数据
     */
    public ResponseEntity<?> getRoleList(BaseCondition condition) {
        Page<SysRole> page = new Page<>(condition.getPage(), condition.getItemPrePage());
        Page<SysRole> sysRolePage = this.baseMapper.selectPage(page, new LambdaQueryWrapper<SysRole>()
                .orderByDesc(BaseEntity::getCreateTime));
        return ResponseEntity.ok(sysRolePage);
    }
}
