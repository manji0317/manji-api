package com.manji.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.condition.UserListCondition;
import com.manji.base.entity.SysUser;
import com.manji.base.entity.SysUserDetails;
import com.manji.base.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2024-04-28
 */
@Service
@Slf4j
public class UserDetailServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserDetailsService {

    @Override
    public SysUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 数据库查询用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            log.error("Query returned no results for user '{}'", username);
            throw new UsernameNotFoundException(username);
        } else {
            // 2. 设置权限集合
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("role");
            // 3. 返回UserDetails类型用户
            return SysUserDetails.builder()
                    .password(user.getPassword())
                    .username(user.getUsername())
                    .authorities(new HashSet<>(authorityList))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(user.getStatus() == 1)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .roleList(new ArrayList<>())
                    .build();
        }
    }

    /**
     * 根据用户名查询用户信息（基本信息、菜单信息）
     *
     * @param username 用户名
     * @return 用户信息
     */
    public ResponseEntity<?> getUserInfo(String username) {
//        UserDTO userDTO = this.baseMapper.getUserInfo(username);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户列表
     */
    public ResponseEntity<?> getUserList(UserListCondition condition) {
        Integer pageNum = condition.getPage();
        Integer itemPrePage = condition.getItemPrePage();

        IPage<SysUser> page = new Page<>(pageNum, itemPrePage);

        Page<SysUser> sysUsers = this.baseMapper.getUserList(condition, page);

        return ResponseEntity.ok(sysUsers);
    }

    /**
     * 根据用户ID删除用户
     *
     * @param userId 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteUserById(Integer userId) {
        log.info("根据ID删除用户开始，用户ID：{}", userId);
        this.baseMapper.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据用户ID更新用户信息
     *
     * @param userId  用户ID
     * @param sysUser 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateUserById(Integer userId, SysUser sysUser) {
        log.info("根据用户ID更新用户信息开始，用户ID：{}", userId);
        this.baseMapper.update(sysUser, new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId));
        return ResponseEntity.ok().build();
    }
}
