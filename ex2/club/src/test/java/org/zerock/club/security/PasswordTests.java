package org.zerock.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //BCryptPasswordEncoder 동작 테스트용
    @Test
    public void testEncode(){
        String password = "1111";
        String enPw = passwordEncoder.encode(password);

        System.out.println("enPw: " + enPw);

        boolean mathResult = passwordEncoder.matches(password, enPw);

        System.out.println("matchResult: " + mathResult);
    }
}
