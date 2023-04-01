package org.zerock.mreview.mreview.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO <DTO, EN>{

    // DTO List
    private List<DTO> dtoList;


    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;


    /***
     * @param result
     * @param fn
     *
     * jpa를 이요하는 Repository에서는 페이지 처리 결과를 Page<Entity> 타입으로 변환하게 된다.
     * 따라서 서비스 계층에서는 이를 처리하기 위해서도 별도의 클래스를 만들어서 처리해야한다.
     * 처리하는 클래스는 크게 다음과 같다.
     * Page<Entity>의 엔티티 객체들을 DTO 객체로 변환해서 자료구조로 담아 주어야 한다.
     * 화면 출력에 필요한 페이지 정보들을 구성해주어야 한다.
     *
     * EN = Entity
     */
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        // 페이지가 10개씩 보인다고 가정
        // 1페이지 경우: Math.ceil(0.1) * 10 = 10
        // 10페이지 경우: Math.ceil(1) * 10 =10
        // 11페이지 경우: Math.ceil(1.1) * 10 = 20
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;


        // 위의 계산대로면 마지막 페이지가 33인데 40이 될 수 있으므로
        end = totalPage > tempEnd ? tempEnd: totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}