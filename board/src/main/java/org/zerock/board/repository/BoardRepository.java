package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.serach.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    // 지연 로딩을 사용할 때 Board와 Member객체를 같이 조회해야하는 상황일 때
    //한개의 로우(Object) 내에 Object[]로 나옴
    @Query("select b, w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);


    // Board가 Reply를 참조하지 않기 때문에 둘의 관계에 연관 관계가 없다.
    // 따라서 left join 사용할 때 on을 이용해야함
    // 특정 게시물과 해당 게시물에 속한 댓글들을 조회해야 할 때
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);


    @Query(value = "SELECT b, w, count(r) " +
                    "FROM Board b " +
                    "LEFT JOIN b.writer w " +
                    "LEFT JOIN Reply r ON r.board = b " +
                    "GROUP BY b",
                    countQuery = "SELECT  count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

    @Query(value = "SELECT b, w, count(r) " +
            "FROM Board b " +
            "LEFT JOIN b.writer w " +
            "LEFT JOIN Reply r ON r.board = b " +
            "WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno); // 해당 게시글에 댓글에 몇개가 있는지


}
