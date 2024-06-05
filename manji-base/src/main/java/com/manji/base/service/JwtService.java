package com.manji.base.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.manji.base.basic.Const;
import com.manji.base.entity.SysUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

/**
 * JWT工具类
 *
 * @author BaiQingDong
 */
@Component
@Slf4j
public class JwtService {
    /**
     * JWT私钥
     */
    @Value("${security.jwt.private-key}")
    private String privateKey;
    /**
     * JWT公钥
     */
    @Value("${security.jwt.public-key}")
    private String publicKey;
    @Value("${security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private String buildToken(SysUserDetails sysUserDetails, long expiresAt) {
        RSAPublicKey rsaPublicKey = getPublicKey().orElseThrow();
        RSAPrivateKey rsaPrivateKey = getPrivateKey().orElseThrow();

        return JWT.create()
                .withClaim(Const.TOKEN_LOGIN_NAME, sysUserDetails.getUsername())
                .withSubject(sysUserDetails.getUserId())
                .withExpiresAt(Instant.now().plusSeconds(expiresAt))
                .sign(Algorithm.RSA256(rsaPublicKey, rsaPrivateKey));
    }

    /**
     * 创建Access Token
     */
    public String createAccessToken(SysUserDetails sysUserDetails) {
        return buildToken(sysUserDetails, jwtExpiration);
    }

    /**
     * 创建刷新 Token
     */
    public String createRefreshToken(SysUserDetails sysUserDetails) {
        return buildToken(sysUserDetails, refreshExpiration);
    }

    /**
     * 校验Token是否可用，
     *
     * @param token 被校验的token
     * @return 解析出来的username
     */
    public String verifyToken(String token) {
        RSAPublicKey rsaPublicKey = getPublicKey().orElseThrow();
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(rsaPublicKey, null))
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim(Const.TOKEN_LOGIN_NAME).asString();
    }

    private Optional<RSAPrivateKey> getPrivateKey() {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return Optional.ofNullable((RSAPrivateKey) keyFactory.generatePrivate(keySpec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("获取JWT私钥异常", e);
            return Optional.empty();
        }
    }

    private Optional<RSAPublicKey> getPublicKey() {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return Optional.ofNullable((RSAPublicKey) keyFactory.generatePublic(keySpec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("获取JWT公钥异常", e);
            return Optional.empty();
        }
    }
}