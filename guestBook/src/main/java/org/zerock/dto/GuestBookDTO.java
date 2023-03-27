package org.zerock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/***
 *     DTO는 엔티티 객체와 달리 각 계층끼리 주고 받는 우편물이나 상자의 개념이다.
 *     순수하게 데이터를 담고 있다는 점에서 엔티티 객체와 유사하지만, 목적 자체가 데이터의 전달이므로
 *     읽고 쓰는것이 모두 허용되는 점이 가능하고 일회성으로 사용되는 성격이 강하다.
 *
 *     JPA를 이용하게 되면 엔티티 객체는 단순히 데이터를 담는 객체가 아니라 실제 데이터베이스와 관련이 있고
 *     내부적으로 엔티티 매니저가 관리하는 객체이다.
 *     DTO가 일회성으로 데이터를 주고 받는 용도로 사용되는 것과 달리 생명주기도 전혀 다르기 때문에 분리해서 처리하는 것을 권장
 *
 *     서비스 계층에서는 GuestBookDTO를 이용해 필요한 내용을 전달 받고, 반환하도록 처리하는데
 *     GuestBookService interface와, GuestBookImpl 클래스를 작성
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestBookDTO {

    private Long gno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate, modDate;
}
