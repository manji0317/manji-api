package com.manji.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.entity.SysUserDetails;
import com.manji.base.mapper.SysUserMapper;
import com.manji.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserDetailsService {

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

}
