package com.manji.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.base.basic.Const;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.condition.UserListCondition;
import com.manji.base.dto.PasswordDTO;
import com.manji.base.dto.UserDTO;
import com.manji.base.entity.SysUser;
import com.manji.base.entity.SysUserDetails;
import com.manji.base.entity.SysUserMenu;
import com.manji.base.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    @Resource
    private SysUserMenuService sysUserMenuService;

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
     * 新增用户
     *
     * @param userDTO 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> saveUser(UserDTO userDTO) {
        log.info("新增用户开始");
        SysUser sysUser = new SysUser();
        sysUser.setUsername(userDTO.getUsername());
        sysUser.setNickname(userDTO.getNickname());
        sysUser.setEmail(userDTO.getEmail());
        sysUser.setPhone(userDTO.getPhone());
        sysUser.setGender(userDTO.getGender());
        sysUser.setBirthday(userDTO.getBirthday());
        sysUser.setStatus(userDTO.getStatus());
        sysUser.setPassword(new BCryptPasswordEncoder().encode(Const.SYSTEM_USER_INIT_PASSWORD));

        boolean save = this.save(sysUser);
        if (!save) {
            log.error("新增用户失败");
            return ResponseEntity.badRequest().build();
        }

        log.info("新增用户成功, 开始存储菜单数据。userId:{}", sysUser.getId());
        List<String> menus = userDTO.getMenus();

        if (menus.isEmpty()) {
            log.info("用户未选择菜单数据，跳过处理。");
            return ResponseEntity.ok().build();
        }

        log.info("开始存储菜单数据");
        List<SysUserMenu> sysUserMenus = new ArrayList<>();
        menus.forEach(menu -> sysUserMenus.add(new SysUserMenu(sysUser.getId(), menu)));
        sysUserMenuService.saveBatch(sysUserMenus);

        return ResponseEntity.ok().build();
    }

    /**
     * 根据用户ID查询用户信息（基本信息、菜单信息）
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public ResponseEntity<?> getUserInfo(String userId) {
        log.info("根据username查询用户信息：userId：{}", userId);
        UserDTO userInfo = this.baseMapper.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
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
    public ResponseEntity<?> deleteUserById(String userId) {
        log.info("根据ID删除用户开始，用户ID：{}", userId);
        this.baseMapper.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据用户ID更新用户信息
     *
     * @param userId  用户ID
     * @param userDTO 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateUserById(String userId, UserDTO userDTO) {
        log.info("根据用户ID更新用户信息开始，用户ID：{}", userId);

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BaseEntity::getId, userId)
                .set(SysUser::getUsername, userDTO.getUsername())
                .set(SysUser::getNickname, userDTO.getNickname())
                .set(SysUser::getEmail, userDTO.getEmail())
                .set(SysUser::getPhone, userDTO.getPhone())
                .set(SysUser::getGender, userDTO.getGender())
                .set(SysUser::getBirthday, userDTO.getBirthday())
                .set(SysUser::getStatus, userDTO.getStatus());

        boolean update = this.update(updateWrapper);

        if (!update) {
            log.info("更新用户失败");
            return ResponseEntity.badRequest().body(10004);
        }

        log.info("更新用户成功，开始处理菜单更新");
        List<String> menus = userDTO.getMenus();
        sysUserMenuService.remove(new LambdaQueryWrapper<SysUserMenu>().eq(SysUserMenu::getUserId, userId));
        menus.forEach(menu -> sysUserMenuService.save(new SysUserMenu(userId, menu)));

        return ResponseEntity.ok().build();
    }

    /**
     * 修改用户密码
     *
     * @param userId      用户ID
     * @param passwordDTO 密码实体类
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updatePassword(String userId, PasswordDTO passwordDTO) {
        log.info("修改用户密码开始，UserId: {}", userId);

        Optional<SysUser> sysUserOptional = Optional.ofNullable(this.baseMapper.selectById(userId));

        return sysUserOptional
                .map(sysUser -> {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    if (passwordEncoder.matches(passwordDTO.getPassword(), sysUser.getPassword())) {
                        this.baseMapper.update(new LambdaUpdateWrapper<SysUser>()
                                .set(SysUser::getPassword, passwordEncoder.encode(passwordDTO.getNewPassword()))
                                .eq(BaseEntity::getId, userId));
                        return ResponseEntity.ok().build();
                    } else {
                        log.info("密码校验不通过");
                        return ResponseEntity.badRequest().body(10001);
                    }
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(10000));
    }

}
