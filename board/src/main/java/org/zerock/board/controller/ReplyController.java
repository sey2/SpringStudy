package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Reply;
import org.zerock.board.service.ReplyService;

import java.util.List;

/***
 *
 * ReplyController의 경우 모든 메서드의 리턴 타입은 기본으로 JSON을 사용한다.
 * 메서드의 반환 타입은 ResponseEntity라는 객체를 이용하는데 이를 이용하면 HTTP의 상태 코드 등을 같이 전달할 수 있다.
 * @GetMapping() 에는 URL의 일부를 {}로 묶은 변수를 이용하는데 이는 메서드 내에서 @PathVariable이라는 것으로 처리한다.
 * 이를 활용하면 브라우저에서는 '/replies/board/100'과 같이 특정 게시물 번호로 조회할 떄 100이라는 데이터를 변수로 처리하는 것이 가능해짐
 */
@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    // 자동 주입을 위해 final
    private final ReplyService replyService;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){
        log.info("bno: " + bno);

        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }


    // 댓글 등록은 Ajax를 이용해 POST 방식으로 처리하고 ReplyController에서 처리
    // @RequestBody는 JSON으로 들어오는 데이터를 자동으로 해당 타입의 객체를 매핑해줌
    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
        log.info(replyDTO);

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO){
        log.info(replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
