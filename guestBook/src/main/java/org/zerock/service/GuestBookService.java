package org.zerock.service;

import org.zerock.dto.GuestBookDTO;
import org.zerock.dto.PageRequestDTO;
import org.zerock.dto.PageResultDTO;
import org.zerock.entity.GuestBook;

/***
 *      서비스 계층에서는 파라미터를 DTO 타입으로 받기 때문에 이를 JPA로 처리하기 위해서는 엔티티 타입의 객체로
 *      변환해야 하는 작업이 반드시 필요하다.
 *      이 기능을 DTO 클래스에 적용하거나 ModelMapper 라이브러리나 MapStruct등을 이용하기도 하는데
 *      여기서는 직접 처리하는 방식으로 작성함
 *
 */
public interface GuestBookService {
    Long register(GuestBookDTO dto);

    GuestBookDTO read(Long gno);

    void remove(Long dto);

    void modify(GuestBookDTO dto);

    PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO);


    /***
     * @param dto
     * @return GuestBook Entity
     *
     * default 기능을 활용해서 구현 클래스에서 동작 할 수 있는 doToEntity()를 구성한다.
     * GuestBookServiceImpl 클래스에서는 이를 활용해서 파라미터로 전달되는 GuestBookDTO를 변환해 보도록 하자.
     */
    default GuestBook dtoToEntity(GuestBookDTO dto){
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getContent())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return entity;
    }

    /***
     * @param entity
     * @return
     *
     * 서비스 계층에서는 엔티티 객체를 DTO 객체로 변환하는 entity to dto()를 정의해야한다
     */
    default GuestBookDTO entityToDto(GuestBook entity){
        GuestBookDTO dto = GuestBookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getMoDate())
                .build();

        return dto;
    }
}
