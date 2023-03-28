package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDateTime;

/***
 *
 * DTO를 구성하는 기준은 기본적으로 화면에 전달하는 데이터이거나, 반대로 화면 쪽에서 전달되는 데이터를
 * 기준으로 하기 때문에 엔티티 클래스의 구성과 일치하지 않는 경우가 많다.
 * 작성하는 BoardDTO의 경우 Member에 대한 참조는 구성하지 않고 작성한다.
 *
 */

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writerEmail;
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount;

}
