package org.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/***
 *
 * DTO 역할을 수행하는 클래스인 동시에 스프링 시큐라티에서 인가/인증 작업을 사용할 수 있음
 * password는 부모클래스를 사용하므로 별도의 멤버 변수로 선언하지 않음
 */
@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {
    public ClubAuthMemberDTO(String username, String password, boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        this(username,password, fromSocial, authorities);
        this.attr = attr;
    }

    private String email;

    private String password;
    private String name;
    private boolean fromSocial;

    private Map<String, Object>attr;

    public ClubAuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities
    ){
        super(username,password,authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
        this.attr = attr;
    }

    @Override
    public Map<String,Object> getAttributes(){
        return this.attr;
    }
}
