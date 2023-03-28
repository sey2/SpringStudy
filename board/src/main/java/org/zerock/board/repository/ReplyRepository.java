package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
    // JPQL을 이용해서 update, delete를 실행하기 위해서는 @modifying 어노테이션을 추가해 주어야함
    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);

    // 게시물로 댓글 목록 가져오기
    // Board 객체를 파라미터로 받고 모든 댓글을 순번대로 가져온다.
    // 테스트 코드는 100번 게시물의 댓글을 순차적으로 조회하는 내용으로 작성
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
