package com.manji.user.controller;

import com.manji.base.basic.entity.TokenEntity;
import com.manji.base.entity.SysUserDetails;
import com.manji.base.service.JwtService;
import com.manji.base.service.UserDetailServiceImpl;
import com.manji.base.dto.CheckLoginDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class LoginController {

    @Resource
    private JwtService jwtService;

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailServiceImpl userService;

    @GetMapping("/captcha")
    public ResponseEntity<?> captcha() {
        return ResponseEntity.ok().build();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> checkLogin(@RequestBody CheckLoginDTO checkLoginDTO) {
        // 模拟报错
        String username = checkLoginDTO.getUsername();
        String password = checkLoginDTO.getPassword();

        // 校验用户，如果校验失败会在自定义的错误处理中返回给前台。
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SysUserDetails sysUserDetails = (SysUserDetails) authenticate.getPrincipal();

        // 生成Token
        String accessToken = jwtService.createAccessToken(sysUserDetails);
        String refreshToken = jwtService.createRefreshToken(sysUserDetails);

        return ResponseEntity.ok(TokenEntity.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenEntity tokenEntity) {
        try {
            String refreshToken = tokenEntity.getRefreshToken();
            String username = jwtService.verifyToken(refreshToken);
            // 刷新token
            SysUserDetails sysUserDetails = userService.loadUserByUsername(username);
            String accessToken = jwtService.createAccessToken(sysUserDetails);
            return ResponseEntity.ok(TokenEntity.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());
        } catch (Exception e) {
            log.error("Token刷新失败", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
