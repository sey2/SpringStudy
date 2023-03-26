package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    // 100개의 새로운 Memo 객체를 생성하고 memoRepository를 이용하여 insert
    /*
    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Memo memo = Memo.builder().memoText("Sample ... " + i).build();
            memoRepository.save(memo);
        });
    }
     */

//    @Test
//    public void testSelect(){
//        // 데이터베이스에 존재하는 mno
//        Long mno = 100L;
//
//        Optional<Memo> result = memoRepository.findById(mno);
//
//        System.out.println("===============================");
//
//        if(result.isPresent()){
//            System.out.println(result);
//        }
//    }

//    /* 특정 엔티티 객체가 존재하는지 확인하는 select가 먼저 실행되고 해당 @Id를 가진 엔티티 객체가 있다면
//       update, 그렇지 않으면 insert 실행*/
//    @Test
//    public void testUpdate(){
//        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
//
//        System.out.println(memoRepository.save(memo));
//    }

//    /* deleteById()의 리턴 타입은 void이며 해당 데이터가 존재하지 않으면 Exception 발생 */
//    @Test
//    public void testDelete(){
//        Long mno = 100L;
//
//        memoRepository.deleteById(mno);
//    }


//    @Test
//    public void testPageDefault(){
//        Pageable pageable =  PageRequest.of(0, 10);
//        Page<Memo> result = memoRepository.findAll((org.springframework.data.domain.Pageable) pageable);
//        System.out.println(result);
//    }
}
