package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

/***
 *
 * @Service를 사용해서 자동으로 스프링에서 빈으로 처리할 수 있게 된다.
 *  loadUserByUsername()에서는 별도의 처리 없이 로그를 기록하고 있다.
 *
 *  ClubMemberRepository를 주입 받을 수 있는 구조로 변경하고 @RequireArgsConstructor 처리
 *  username이 실제로는 ClubMember에서는 email을 의미하고 이를 사용해서 ClubMember Repository의 findByEmail()을 호출
 *  사용자가 존재하지 않으면 userNameNotFoundException으로 처ㅏ리
 *  ClubMember를 UserDetails 타입으로 처리하기 위해서 ClubAuthMemberDTO 타입으로 변환
 *  ClubMemberRole은 스프링 시큐리티에서 사용하는 SimpleGrantedAuthortiy로 변환 이떄 'ROLE_'라는 접두어를 추가해서 사용
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.info("ClubUserDetailService load UserByUserName" + username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        // 책에 있는 오탈자 수정
        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();

        log.info("---------------------");
        log.info(clubMember);

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().
                        map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
