package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/customLogin") // 실질적인 처리는 로그인이 한다.
    public void loginInput(){

        log.info("custom Login Page....");
    }
}
