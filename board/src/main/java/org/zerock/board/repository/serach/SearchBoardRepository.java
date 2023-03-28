package org.zerock.board.repository.serach;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.board.entity.Board;


/***
 *
 * Spring Data JPA의 Repository를 확장하기 위해서는 다음과 같은 단계로 처리
 * 1. 쿼리 메서드나 @Query등으로 처리할 수 없는 기능은 별도의 인터페이스로 설계
 * 2. 별도의 인터페이스에 대한 구현 클래스를 작성한다. 이때 QuerydslRepositorySupport 클래스를 부모 클래스로 사용
 * 3. 구현 클래스에 인터페이스ㅡ이 기능을 Q도메인 클래스와 JPQLQuery를 이용해서 구현
 *
 * 추가된 패키지에는 확장하고 싶은 기능을 인터페이스로 구현한다.
 * 이때 인터페이스의 메서드 이름은 가능하면 쿼리 메소드와 구별이 가능하도록 선언하고,
 * SearchBoardRepositoryImpl 클래스를 선언한다.
 * 이때 주의할 점은 구현 클래스의 이름은 반드시 인터페이스의 이름 + Impl로 작성한다.
 */


public interface SearchBoardRepository {
    Board search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
