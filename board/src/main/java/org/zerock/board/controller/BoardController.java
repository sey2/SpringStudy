package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list................" + pageRequestDTO);

        // html로 DTO 전송
        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }

    /***
     *  그냥 register 페이지에 접속하면 get 요청
     */
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }


    /***
     * @param dto
     * @param redirectAttributes
     * @return redirect:/board/list
     *
     * 게시물 등록 처리
     * GET 방식으로 동작하는 링크와 실제 처리하는 메서드
     * 정상적 후 다시 목록 페이지로 이동함
     *
     * member 테이블에 존재하는 이메일 주소를 지정해야함
     *
     * get 요청으로 들어온 register에서 게시글 수정하고 submit 누르면 html 코드에서
     *  /register 링크로 post 요청 보내져서 해당 메소드가 실행 됨
     */
    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto ..." + dto);

        // 새로 추가된 엔티티 번호
        Long bno = boardService.register(dto);

        log.info("BNO: " + bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";

    }

    @GetMapping({"/read", "modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
        log.info("bno: " + bno);

        BoardDTO boardDTO = boardService.get(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("post modify ..................................");
        log.info("dto: " + dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());

        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }


}
