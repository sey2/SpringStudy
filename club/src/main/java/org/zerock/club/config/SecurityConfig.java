package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailHandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.service.ClubUserDetailsService;

/***
 *
 * @EnableGlobalMethod는 접근 제한을 자동으로 설정해주는거?
 *
 */
@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private ClubUserDetailsService userDetailsService;


    // BCryptPasswrodEncoder()는 bcrypt 해시 함수를 이용해 패스워드를 암호화 하는 목적으로 설계된 클래스
    // 암호화된 패스워드는 원래되로 복호화가 불가능하며 (매번 암호화된 값도 다르게 나온다)
    // 대신 특정한 문자열이 암호화된 결과인지만을 확인할 수 있기 때문에 원본 내용을 볼 수 없음
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // InMemortUserDetailsManager 타입의 객체를 생성하는데 InMemoryUserDetailsManager는
    // 말 그대로 메모리상에 있는 데이터를 이용하는 인증 매니저를 생성한다.
    // 코드로 작성된 계정은 조금 뒤쪽에서는 사용할 수 없지만 단순 테스트하는 정도의 용도로 적합
    // Club UserDetailsService가 빈으로 등록되면 이를 자동으로 스프링 시큐맅 티에서 UserDetailsService로 인식하기 때문에
    // 기존에 임시로 설정한 InMemoryUserDetailsManager 객체를 더이상 사용하지 않도록 수정해주어야 함
//   @Bean
//    public InMemoryUserDetailsManager userDetailsManager(){
//        UserDetails user = User.builder()
//                .username("user1")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//
//        log.info("userDetailsService..................");
//        log.info(user);
//
//        return new InMemoryUserDetailsManager(user);
//   }

    /***
     *
     * 스프링 시큐리티를 이용해 특정한 리소스 (자원-웹의 경우에는 특정한 URL)에 접근제한을 하는 방식은 2가지가 있다
     * 1.설정을 통해서 패턴을 지정
     * 2.어노테이션을 이용해서 적용하는 방법
     * 아래 방식은 1번 방법
     */
   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

       /** 만약 /sample/admin 페이지에 ROLE_ADMIN 권한을 가진 사용자들만 접근이 가능하게 설정하고 싶다면
           SampleController의 exAdmin() 메서드에 @PreAuthorize("haseRole('ADMIN')) 해주면 됨  **/
        // 접근 제한 설정 @EnableGlobalMethodSecurity 안썼을 때
//        http.authorizeHttpRequests((auth) -> {
//            auth.requestMatchers("/sample/all").permitAll();
//            auth.requestMatchers("/sample/member").hasRole("USER");
//        });

       AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
       authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

       AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
       http.authenticationManager(authenticationManager);

        http.formLogin(); // 인가 인증에 문제시 로그인 화면 띄우기 (유저 권한인데 관리자 페이지에 접속 시도하거나 그럴 때)
        http.csrf().disable();  // CSRF 토큰 발행하지 않음 설정
       //  http.logout();  // 인가 인증에 문제시 로그아웃 화면 띄우기, CSRF 토큰을 사용할 때는 반드시 POST 방식으로만 로그아웃 처리해야함
        http.logout(); // csrf 토큰 비활성화시 GET 방식으로 로그아웃

       // google login
        http.oauth2Login().successHandler(successHandler());

        // 소셜 로그인은 사용하지 못함
        // rememberMe를 사용하면 웹의 인증 방식 중에 쿠키를 사용하는 방식이다
        // 이 기능을 활용하면 한번 로그인한 사용자가 브라우저를 닫은 후 다시 서비스에 접속해도 별도의 로그인 절차 없이 바로 로그인 처리 가능
        http.rememberMe()
                .tokenValiditySeconds(60*60*24*7)   // 얼마나 유지할지 <- 해당 코드는 7일간 유지
                .userDetailsService(userDetailsService);

        // API Check Filter 동작 순서를 UsernamePasswordAuthenticationFilter 이전에 실행
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
   }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
       return new ClubLoginSuccessHandler(passwordEncoder());
   }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
       // notes에서만 Token Check
       return new ApiCheckFilter("/notes/**/*");
   }

   public ApiLoginFilter apiLoginFilter(AuthenticationManager authenticationManager) throws  Exception{
       // 해당 경로로 접근할 때 동작 하도록 지정
       ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
       apiLoginFilter.setAuthenticationManager(authenticationManager);

       apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

       return apiLoginFilter;
   }
}
