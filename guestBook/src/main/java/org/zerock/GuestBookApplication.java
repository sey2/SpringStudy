package org.zerock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/***
 *  JPA를 이용하면서 AuditingEntityListener를 활성 시키기 위해서는 프로젝트에 @EnableJpaAuditing 설정을 추가해 주어야함
 */
@SpringBootApplication
@EnableJpaAuditing
public class GuestBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuestBookApplication.class, args);
    }

}
