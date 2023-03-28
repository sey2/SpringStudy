package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyService {

    // 댓글 등록
    Long register(ReplyDTO replyDTO);

    // 특정 게시물의 댓글 목록
    List<ReplyDTO> getList(Long bno);

    // 댓글 수정
    void modify(ReplyDTO replyDTO);

    // 댓글 삭제
    void remove(Long rno);


    //  ReplyDTO를 Reply 객체로 변환 Board 객체의 처리가 수반됨
    default Reply dtoToEntity(ReplyDTO replyDTO){
        Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replayer(replyDTO.getReplayer())
                .board(board)
                .build();

        return reply;
    }

    // Reply객체를 ReplyDTO로 변환 Board 객체가 필요하지 않으므로 게시물 번호만
    default ReplyDTO entityToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getReplayer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();

        return dto;
    }
}
