package org.zerock.entity;

import jakarta.persistence.*;
import lombok.*;


/***
 *  BaseEntitiy를 상속 받음으로써 등록 시간과 수정 시간을 자동으로 처리해준다.
 *
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GuestBook extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    /***
     *
     * 엔티티 클래스는 가능하면 setter 관련 기능을 만들지 않는 것이 권장 사항
     *  필요에 따라 수정 기능을 만들기도 하지만 엔티티 객체가 애플리케이션 내부에서 변경되면
     *  JPA를 관리하는 쪽이 복잡해질 우려가 있기 때문에 가능하면 최서한의 수정이 가능하도록 하는 것을 권장한다고 함
     */
    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
