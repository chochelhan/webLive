package com.riverflow.livegoapp.Jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Repository.MemberRepository;
import com.riverflow.livegoapp.Service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @JsonIgnore
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization"); // 헤더 파싱
        String username = "", token = "";

        if (authorization != null && authorization.startsWith("Bearer ")) { // Bearer 토큰 파싱
            token = authorization.substring(7); // jwt token 파싱
            username = jwtUtil.getUsernameFromToken(token); // username 얻어오기
        } else {
            filterChain.doFilter(request, response);
        }
        // 현재 SecurityContextHolder 에 인증객체가 있는지 확인
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.isValidToken(token, userDetails)) {
                Boolean flag = false;
                String url = request.getRequestURI();
                if (url.contains("/api/admin/controller/")) {
                    Member findMember = memberRepository.findByUid(username);
                    if (findMember.getRole().equals("admin")) {
                        flag = true;
                    }

                } else {
                    Member findMember = memberRepository.findByUid(username);
                    if (findMember != null) {
                        flag = true;
                    }

                }
                if (flag) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }
            filterChain.doFilter(request, response);
        }

    }
}
