package org.zerock.mreview.mreview.repository;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.mreview.entity.Member;
import org.zerock.mreview.mreview.entity.Movie;
import org.zerock.mreview.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /***
     * 영화를 조회하는 화면에 들어가면 당연히 영화에 대한 영화 리뷰를 조회할 수 있고,
     * 자신이 영화에 대한 영화 리뷰를 등록하거나 수정/삭제할 수 있어야 한다.
     * 이를 위해서 우선적으로 필요한 데이터는 특정한 영화 번호를 이용해서 해당 영화를 리뷰한 정보이다.
     *
     *  @EntityGraph를 사용하지 않으면 NoSession 에러 발생
     *  Member에 대한 Fetch 방식이 Lazy이기 때문에 한 번에 Review객체와 Member 객체를 조회할 수 없기 때문에 발생
     *
     * @Transactional을 적용한다 해도 Review 객체의 getMember().getEmail()을 처리할 때마다 Member 객체를 로딩해야하는
     * 문제가 있음
     * 위 문제를 해결하려면 Query를 이용해서 조인 처리하거나 EntityGraph를 이용해서 Review 객체를 가져올 떄 Memeber 객체를 로딩하는 방법이 있음
     */

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // 아래 두개의 어노테이션을 사용하지 않는다면 review 테이블에서 3번 반복적으로 delete가 실행 된 후에 member를 삭제함
    // 위와 같은 문제를 해결하기 위해 Query를 사용해 where절을 지정해주는게 더 나은 방법
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);
}
