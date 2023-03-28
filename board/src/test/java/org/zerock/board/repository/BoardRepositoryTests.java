package org.zerock.board.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Member member = Member.builder().email("user" + i + "@aaa.com").build();

            Board board = Board.builder().title("Title..."+i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });
    }

    /***
     *
     * Member를 @ManyToOne으로 참고하고 있는 Board를 조회하는 테스트코드
     * 쿼리가 내부적으로 Board와 Member를 left outer join 처리해줌
     * 이와 같이 연관관계를 가진 모든 엔티티를 같이 로딩하는 것을 Eager Loding이라고 한다.
     * Eager는 열렬한, 적극적인 이라는 뜻으로 즉시 로딩이라는 용어로 표현
     * 즉시 로딩은 한번에 연관 관계가 있는 모든 엔티티를 가져온다는 장점이 있지만
     * 여러 연관관계를 맺고 있거나 연관 관계가 복잡할 수록 조인으로 인한 성능 저하를 피할 수없다
     * JPA에서 연관 관계의 데이터를 어떻게 가져올 것인가를 fetch라고 하는데 연관 관계의 어노테이션 속성으로 fetch모드를 지정한다.
     *
     * 즉시 로딩은 불필요한 조인까지 처리해야하는 경우가 많기 때문에 가능하면 사용하지 않고
     * 그와 반대되는 개념으로 Lazy Loading으로 처리해야한다.
     *
     * 연관 관계에서는 @ToString()을 주의 해야함
     * 해당 클래스의 모든 멤버 변수를 출력하게 되므로 Board 객체의 writer 변수로 선언된 Member 객체 역시 출력해야한다.
     * Member객체를 출력하기 위해서는 Member 객체의 toString()이 호출 되어야 하고 이 때 데이터베이스 연결이 필요하게 된다.
     * exclude 속성을 사용하게 되면 지정 변수는 toString() 에서 제외하게 된다.
     */

    @Transactional  // 이거를 사용하지 않고 lazy loading하게 되면 no Session 에러가 발생 (트랜 잭션 단위로 처리해라 라는 뜻 )
    @Test
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L);

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    // 지연 로딩을 사용할 때 Board와 Member객체를 같이 조회해야하는 상황일 때
    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        System.out.println("------------------");
        System.out.println(Arrays.toString(arr));
    }

    // Board가 Reply를 참조하지 않기 때문에 둘의 관계에 연관 관계가 없다.
    // 따라서 left join 사용할 때 on을 이용해야함
    // 특정 게시물과 해당 게시물에 속한 댓글들을 조회해야 할 때
    @Test
    public void testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    // (1페이지 데이터를 처리한다고 가정) 페이지 번호는 0으로 지정하고 10개를 조회한다.
    @Test
    public void testWithReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row ->{
            Object[] arr = (Object[]) row;

            System.out.println(Arrays.toString(arr));
        });
    }


    // 해당 게시글에 댓글이 몇개 있는지
    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

    // JPA 레포지토리 확장한거 테스트하는 테스트 코드
    @Test
    public void testSearch1(){
        boardRepository.search1();
    }

    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);
    }
}
