package org.zerock.sb.security.filter;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.sb.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class TokenCheckFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public TokenCheckFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        log.info("-------------------TokenCheckFilter-------------------");

        String path = request.getRequestURI();
        log.info(path);

        if(path.startsWith("/api222/")) {
            //check token
            String authToken = request.getHeader("Authorization");

            // 없는경우와 토큰 실패한 경우까지 체크해야함
            if(authToken == null) {
                log.info("authToken is null...............................");

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;
            }

            //jwt검사
            jwtUtil.validateToken(authToken);
            filterChain.doFilter(request, response);
        }else {
            log.info("==========================TokenCheckFilter============================");
            // 다음 단계로 진행
            filterChain.doFilter(request, response);
        }


    }
}