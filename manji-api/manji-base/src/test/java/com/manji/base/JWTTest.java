package com.manji.base;

import com.manji.base.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = BaseModuleScan.class)
public class JWTTest {

    @Autowired
    private JwtService jwtService;
    @Test
    public void testCreateJwt() throws NoSuchAlgorithmException {
//        String accessToken = jwtUtil.createAccessToken(123123);
//        System.out.println("token = " + accessToken);
    }
}
