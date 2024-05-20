package com.manji.user;

import com.manji.base.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = ManjiSystemApplication.class)
public class CreateTokenTest {
    @Autowired
    private JwtService jwtService;
    @Test
    public void testCreateJwt() throws NoSuchAlgorithmException {
    }
}
