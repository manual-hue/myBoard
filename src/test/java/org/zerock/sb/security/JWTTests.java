package org.zerock.sb.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.util.JWTUtil;

@SpringBootTest
@Log4j2
public class JWTTests {
    @Autowired
    JWTUtil jwtUtil;

    @Test
    public void testGenerate() {
        String jwtStr = jwtUtil.generateToken("user11");
        log.info(jwtStr);
    }

    @Test
    public void testValidate() {
        String str = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE2MzQ4NjkwNzMsImV4cCI6MTYzNDg3MjY3M30.pyv2SfJ2-0ZjpRDThBQ6cZjtzZo5-GTYcO7-YcqyJSA";
        jwtUtil.validateToken(str);

        try{
            jwtUtil.validateToken(str);
        }catch (ExpiredJwtException ex) {
            log.error("expired............");
            log.error(ex.getMessage());
        }

        catch (JwtException ex) {
            log.info(ex.getMessage());
        }
    }
}