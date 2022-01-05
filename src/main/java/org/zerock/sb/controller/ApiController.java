package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.repository.search.BoardSearch;
import org.zerock.sb.service.BoardService;

@RestController // JSON 목적이므로
@RequestMapping("/api") // 모든 api가 해당 컨트롤러를 탄다
@RequiredArgsConstructor
@Log4j2
public class ApiController {

    private final BoardService boardService; // 연산, 조합, 가공

    @GetMapping("/board/list")
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO){

        log.info("pageRequestDTO"+ pageRequestDTO);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return boardService.getList(pageRequestDTO);
    }
}
