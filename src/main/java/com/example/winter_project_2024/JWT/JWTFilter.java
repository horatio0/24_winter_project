package com.example.winter_project_2024.JWT;

import com.example.winter_project_2024.DTO.UserDetailDTO;
import com.example.winter_project_2024.Entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/")){
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization값 가져오기
        String auth = request.getHeader("Authorization");

        //헤더에 없으면 쿼리 파라미터에서 재시도!!
        if(auth==null) {
            if (request.getParameter("token") != null) auth = "Bearer " + request.getParameter("token");
        }

        //토큰이 없거나 구조가 이상하면 이 필터를 건너 뜀
        if(auth==null || !auth.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 추출
        String token = auth.split(" ")[1];
        //소멸시간 검증
        if(jwtUtil.isExpired(token)){
            filterChain.doFilter(request,response);
            return;
        }

        //토큰에서 id 추출
        String id = jwtUtil.getId(token);
        String role = jwtUtil.getRole(token);

        Member member = new Member();
        member.setMemberId(id);
        member.setRole(role);

        UserDetailDTO userDetailDTO = new UserDetailDTO(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetailDTO, null, userDetailDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
