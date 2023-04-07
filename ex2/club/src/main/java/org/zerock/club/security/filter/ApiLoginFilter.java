package org.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.util.JWTUtil;

import java.io.IOException;

/***
 *
 *
 *  특정한 URL로 외부에서 로그인이 가능하도록 하고, 로그인이 성공하면 클라이언트가 Authoriztion 헤더의 값으로
 *  이용할 데이터를 전송한다.
 *  예제에서는 /api/login 이라는 URL을 이용해서 외부의 클라이언트가 자신의 아이디와 패스워드로 로그인한다고 가정
 *
 */

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil){
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }
    public ApiLoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//        throws AuthenticationException, IOException, ServletException{
//        log.info("--------------------ApiLoginFilter---------------------");
//        log.info("attemptAuthentication");
//
//        String email = request.getParameter("email");
//        String pw = "1111";
//
//        if(email == null)
//            throw new BadCredentialsException("email can not be null");
//
//        return null;
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException{
        log.info("----------------------ApiLoginFilter-----------------------");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pw);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException{
        log.info("-------------------------ApiLoginFilter----------------");
        log.info("successfulAuthentication: " + authResult);

        log.info(authResult.getPrincipal());

        String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;

        try{
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
