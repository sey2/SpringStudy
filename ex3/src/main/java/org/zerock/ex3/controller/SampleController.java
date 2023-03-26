package org.zerock.ex3.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.ex3.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {

    @GetMapping("/ex1")
    public void ex1(){
        log.info("ex1 ...........");
    }


    /* Sample DTO 객체를 만들어서 ex2.html li 태그에서 사용 */
    @GetMapping({"/ex2", "/exLink"})
    public void exModel(@NotNull Model model){
        List<SampleDTO> list = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i ->{
            SampleDTO dto = SampleDTO.builder().
                    sno(i).
                    first("First ..." + i).
                    last("Last ..." + i).
                    regTime(LocalDateTime.now()).
                    build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes){
        log.info("exInline ...........");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First...100")
                .last("Last ... 100")
                .regTime(LocalDateTime.now())
                .build();

        redirectAttributes.addFlashAttribute("result", "sucess");
        redirectAttributes.addFlashAttribute("dto", dto);

        // exInline으로 접속하면 ex3 페이지로 리다이렉트
        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3(){
         log.info("ex3");
    }

    // th:replace = 기존의 내용을 완전히 대체 {th:replace="~{/fragment/fragment2}} 파일 전체를 가져옴 (:: 생략시)
    // th:insert =기존의 내용의 바깥쪽 태그는 그대로 유지하면서 추가되는 방식 <html 개발자 도구 열어보면 <div태그 내에 다시 div 태그가 생성됨>
    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "/exSidebar"})
    public void exLayout1(){
        log.info("exLayout .......");
    }


}
