package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    // 로그인을 하지 않은 사용자도 접글할 수 있는 경로
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll(){
        log.info("exAll.............");
    }

    // 로그인한 사용자만 접근할 수 있는 경로

    /**
     *
     * Controller에서 로그인 사용자 정보를 확인하는 방법은 크게 2가지가 있음
     * 1. SecurityContextHolder 객체를 사용하는 방법
     * 2. 직접 파라미터와 어노테이션을 사용하는 방식이 있음
     * 3. 스프링 시큐리티의 Principal타입은 getPrincipal() 메서드를 통해서 Object 타입의 반환 타입이 있어서
     * @AuthenticationPrincipal은 별도의 캐스팅 작업 없이 실제 ClubAuthMemberDTO 타입을 사용할 수 있어서 좀더 판하게 사용 가능
     */
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){
        log.info("exMember............");

        log.info("-------------------");
        log.info(clubAuthMember);
    }

    // 관리자만 접근할 수 있는 경로
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin...........");
    }


    // 게시판의 수정이나 삭제 작업시에 해당 게시물의 작성자만이 해당 URL을 처리하게 하고 싶을 때
    // #과 같은 특별한 기호나 authentication 내장 변수를 이ㅛㅇ할 수 있음
    // /sample/exOnly 에 관리자라 해도 접근 못함
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user95@zerock.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){
        log.info("exMemberOnly...............");
        log.info(clubAuthMember);

        return "/sample/admin";
    }

}
