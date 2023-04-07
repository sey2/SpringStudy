package org.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.club.security.util.JWTUtil;

import java.io.IOException;
import java.io.PrintWriter;

/***
 *
 * 기존 웹 어플리케이션의 쿠키나 세션을 사용하는 경우 동일한 사이트에서만 동작하기 때문에 API 서버럼 외부에서
 * 자유롭게 데이터를 주고 받을 떄는 유용하지 못한다.
 * 외부에서 API를 호출할 때는 주로 인증 정보나 인증 키를 같이 전송한다.
 * 이러한 키를 토큰이라는 용어로 사용하는데 JWT(JSON Web Token)이라는 것을 이용한다.
 * 외부에서는 특정한 API를 호출할 때 반드시 인증에 사용할 토큰을 같이 전송하고, 서버에서는 이를 검증한다.
 * 이 과정에서 필요한 것은 특정한 URL을 호출할 떄 전달된 토큰을 검사하는 필터이다.
 * 스프링 시큐리티는 원하는 필터를 사용자가 작성하고, 이를 설정에서 시큐리티 동작의 일부로 추가할 숭 ㅣㅆ다.
 *
 */

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;

    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }


    // 크롬 확장 프로그램으로 GET, POST 보내서 정상으로 응답하는지 확인
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        log.info("REQUEST URI: " + request.getRequestURI());
        log.info(antPathMatcher.match(pattern, request.getRequestURI()));


        if(antPathMatcher.match(pattern, request.getRequestURI())){
            log.info("ApiCheckFilter.................................");
            log.info("ApiCheckFilter.................................");
            log.info("ApiCheckFilter.................................");

            boolean checkHeader = checkAuthHeader(request);

            if(checkHeader) // Authorization 헤더가 성공 적으로 받아지면
                filterChain.doFilter(request, response);
            else{       // 헤더 받기에 실패 했으면
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                // json 리턴 및 한글 깨짐 수정.
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();

                String message = "FAIL CHECK API TOKEN";
                json.put("code","403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }

            return;

        }

        filterChain.doFilter(request, response);
    }

    // Authorization이라는 헤더의 값을 확인하고 boolean 타입의 결과를 반환한다.

    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + email);
                checkResult =  email.length() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return checkResult;
    }

}
