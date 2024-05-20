package com.manji.base.filter;

import com.manji.base.basic.Const;
import com.manji.base.service.JwtService;
import com.manji.base.service.SysUserServiceImpl;
import com.manji.base.utils.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 校验Token过滤器
 *
 * @author BaiQingDong
 * @since 2023/7/7 15:08
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final SysUserServiceImpl userService;
    private final JwtService jwtService;
    private final RequestMatcher whitelistMatcher;

    public JwtAuthenticationFilter(SysUserServiceImpl userService, JwtService jwtService, @Value("${security.whitelist-paths}") List<String> whitelistPaths) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.whitelistMatcher = new OrRequestMatcher(
                whitelistPaths.stream()
                        .map(AntPathRequestMatcher::new)
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 如果请求是白名单则，不进行校验。
        if (whitelistMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader(Const.TOKEN_HEADER);

        String username = null;
        String token;

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(Const.TOKEN_BEARER)) {
            token = authorizationHeader.substring(Const.TOKEN_BEARER.length());
            try {
                username = jwtService.verifyToken(token);
            } catch (Exception e) {
                ResponseUtils.buildResponse(response, ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(), HttpStatus.UNAUTHORIZED);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
