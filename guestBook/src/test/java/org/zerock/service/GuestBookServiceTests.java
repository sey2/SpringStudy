package org.zerock.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.zerock.dto.GuestBookDTO;
import org.zerock.dto.PageRequestDTO;
import org.zerock.dto.PageResultDTO;
import org.zerock.entity.GuestBook;

@SpringBootTest
public class GuestBookServiceTests {

    @Autowired
    private GuestBookService service;


    /***
     * 해당 메소드는 실제로 데이터베이스에 저장되지는 않지만 GuestBookDTO를
     * GuestBook 엔티티로 반환한 결과를 확인할 수 있다.
     * 변환 작업에 문제가 없다면 GuestBookImpl 클래스를 수정해서 실제로 데이터베이스에 처리가 완료 되도록 한다.
     */
    @Test
    public void testRegister(){
        GuestBookDTO guestBookDTO = GuestBookDTO.builder()
                .title("Sample Title ...")
                .content("Sample Content ...")
                .writer("user0")
                .build();

        System.out.println(service.register(guestBookDTO));
    }

    /***
     * 목록 처리 테스트
     */
    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("-------------------");

        for(GuestBookDTO guestBookDTO: resultDTO.getDtoList()){
            System.out.println(guestBookDTO);
        }

        System.out.println("====================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")     // t: 제목, c: 내용, w: 작성자, tc: 제목 내용, tcw: 제목 내용 타이틀
                .keyword("한글")
                .build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV:" + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("---------------------------");
        for(GuestBookDTO guestBookDTO : resultDTO.getDtoList()){
            System.out.println(guestBookDTO);
        }

        System.out.println("=====================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

}
