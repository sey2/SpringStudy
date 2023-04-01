package org.zerock.mreview.mreview.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.mreview.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("r" + i + "@zerock.org")
                    .pw("1111")
                    .nickname("reviewer" + i).build();

            memberRepository.save(member);
        });
    }

    // Member와 review는 M:N관계 이기 때문에 Member를 삭제하려면 review도 전부 삭제해 주어야 함
    // FK를 가지는 Review 를 먼저 삭제해주어야 함
    @Commit
    @Transactional
    @Test
    void testDeleteMember(){
        // Member의 mid
        Long mid = 1L;

        Member member = Member.builder().mid(mid).build();

        // 이렇게 하면 에러남
//        memberRepository.deleteById(mid);
//        reviewRepository.deleteByMember(member);

        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }
}
