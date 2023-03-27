package org.zerock.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.dto.GuestBookDTO;
import org.zerock.dto.PageRequestDTO;
import org.zerock.dto.PageResultDTO;
import org.zerock.entity.GuestBook;
import org.zerock.entity.QGuestBook;
import org.zerock.repository.GuestBookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService{

    // @RequireArgsConstructor 어노테이션을 이용하여 의존성이 자동으로 주입되도록 설정
    private final GuestBookRepository repository;

    @Override
    public Long register(GuestBookDTO dto){
        log.info("DTO ----------------");
        log.info(dto);

        GuestBook entity = dtoToEntity(dto);

        log.info(entity);

        // --- 여기서 부터는 실제 데이터베이스에 저장됨
        repository.save(entity);

        return entity.getGno();
    }

    /***
     *
     * @param requestDTO
     * @return
     *
     *  서비스 계층에서는 PageRequestDTO를 파라미터로, PageReusltDTO를 리턴타입으로 사용하는 getList()를 설계하고
     */

    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<GuestBook> result = repository.findAll(booleanBuilder, pageable);

        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestBookDTO read(Long gno) {

        Optional<GuestBook> result = repository.findById(gno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestBookDTO dto) {

        // 업데이트 항목은 '제목', '내용'
        Optional<GuestBook> result = repository.findById(dto.getGno());

        if(result.isPresent()){

            GuestBook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }


    /***
     * @param requestDTO
     * @return BooleanBuilder
     *
     * PageRequest DTO를 파라미터로 받아 검색 조건(type)이 있는 경우에는 conditionBuilder 변수를
     * 생성해서 각 검색 조건을 or로 연결해서 초기한다. 반면에 검색 조건이 없다면 gno > 0 으로만 생성됨
     *
     * list html searchForm에서 키-값 정하고 js searchFrom.submit하면 PageRequestDTO로 넘어옴
     */
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qGuestBook.gno.gt(0L);

        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0)
            return booleanBuilder;

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t"))
            conditionBuilder.or(qGuestBook.title.contains((keyword)));

        if(type.contains("c"))
            conditionBuilder.or(qGuestBook.content.contains(keyword));

        if(type.contains(""))
            conditionBuilder.or(qGuestBook.content.contains(keyword));

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;

    }
}
