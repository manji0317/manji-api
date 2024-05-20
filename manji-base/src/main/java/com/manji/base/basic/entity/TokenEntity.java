package com.manji.base.basic.entity;

import lombok.Builder;
import lombok.Data;

/**
 * token 实例
 *
 * @author BaiQingDong
 * @since 2023/8/25 9:12
 */
@Data
@Builder
public class TokenEntity {
    // Token
    String accessToken;
    // 刷新Token
    String refreshToken;
}
