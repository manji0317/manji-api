package com.manji.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

/**
 * @author BaiQingDong
 * @since 2023/6/30 15:51
 */
@Data
@Builder
@AllArgsConstructor
public class SysUserDetails implements UserDetails , CredentialsContainer  {
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    // 扩展字段
    private final String userId;
    private final String email;
    private final String phone;
    private final List<String> roleList;
    
    // 认证成功后擦除密码
    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
