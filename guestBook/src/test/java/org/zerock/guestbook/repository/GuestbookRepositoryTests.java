package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.entity.GuestBook;
import org.zerock.entity.QGuestBook;
import org.zerock.repository.GuestBookRepository;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestBookRepository guestBookRepository;

    /* GuestBook 테이블에 객체 추가 */
    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1, 300).forEach(i ->{
            GuestBook guestBook = GuestBook.builder()
                    .title("Title ...." + i)
                    .content("Content ...." + i)
                    .writer("user" + (i%10))
                    .build();
            System.out.println(guestBookRepository.save(guestBook));
        });
    }

    /* 기본키가 300인 값 변경 후 테스트 코드 */
    @Test
    public void updateTest(){
        // 존재하는 번호로 테스트
        Optional<GuestBook> result = guestBookRepository.findById(300L);

        if(result.isPresent()){

            GuestBook guestBook = result.get();

            guestBook.changeTitle("Change Title ...");
            guestBook.changeContent("Changed Content ...");

            guestBookRepository.save(guestBook);
        }
    }

    // 제목(title)에 1이라는 글자가 있는 엔티티들을 검색하는 테스트 코드
    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // 가장 먼저 동적으로 처리하기 위해서 Q도메인 클래스를 얻어옴
        // Q도메인 클래스를 이용하면 엔티티 클래스에 선언된 title, content 같은 필드들을 변수로 활용 가능
        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = "1";

        // BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너라고 생각하자
        BooleanBuilder builder = new BooleanBuilder();

        // 원하는 조건은 필드 값과 같이 결합해서 생성한다.
        // BooleanBuilder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야 한다.
        // Java안에 있는 Predicate 타입이 아니므로 주의
        BooleanExpression expression = qGuestBook.title.contains(keyword);

        // 만들어진 조건은 where문에 and나 or같은 키워드와 결합시킨다.
        builder.and(expression);

        // BooleanBuilder는 GuestBookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용할 수 있다.
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    /***
     *  다중 항목 검색 테스트
     *  복합 조건은 여러 조건이 결합된 형태이다.
     *  예를 들어 제목(title) 혹은 내용(content)에 특정한 키워드가(keyword) 있고 gno가 0보다 크다와 같은 조건을 처리
     */
    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestBook.title.contains(keyword);

        BooleanExpression exContent = qGuestBook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        // gno가 0보다 크면 great then
        builder.and(qGuestBook.gno.gt(0L));

        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });


    }
}
