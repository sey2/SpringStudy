package org.zerock.mreview.mreview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/***
 @MappedSuperclass <- 어노테이션이 적용된 클래스는 테이블로 생성되지 않는다.
 (실제 테이블은 BaseEntity 클래스를 상속한 엔티티의 클래스로 데이터베이스 테이블이 생성된다.)
 JPA 내부에서 엔티티 객체가  생성/변경되는 것을 감지하는 역할은 AuditingEntityListener로 이루어진다.
 이를 통해서 regDate, modDate에 적절한 값이 지정 됨
 ***/

/***
 @CreateDate는 JPA에서 엔티티의 생성 시간을 처리하고,
 @LastAModifyDate는 최종 수정 시간을 자동으로 처리하는 용도로 사용함
 속성으로 insertable, updateable 등이 있는데 아래 코드에서는 false로 지정함
 이를 통해서 해당 엔티티 객체를 데이터베이스에 반영할 때 regdate 칼럼 값은 변경되지 않는다.
 ***/


@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;

}