package org.zerock.club.repository;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    // 회원 데이터 조회
    // ClubMember 조회 시 이메일을 기준으로 조회하고, 일반 로그인 사용자와 뒤에 추가되는 소셜 로그인 사용자를
    // 구분하기 위해서 ClubMemberRepository에 별도의 메서드 처리
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.fromSocial = :social and m.email = :email")
    Optional<ClubMember> findByEmail(String email, boolean social);
}
