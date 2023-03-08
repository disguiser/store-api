package com.snow.storeapi.security;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.storeapi.entity.JwtCheckResult;
import com.snow.storeapi.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtComponent jwtComponent;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StrUtil.isEmpty(header) && header.startsWith("Bearer ")) {
            final String token = header.split(" ")[1].trim();
            if (!StrUtil.isEmpty(token)) {
                JwtCheckResult jwtCheckResult = jwtComponent.validateGetSubject(token);
                if (jwtCheckResult.isSuccess()) {
                    User user = objectMapper.readValue(jwtCheckResult.getSubject(), User.class);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null , user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request,response);
                    return;
                } else {
                    response.setContentType("text/plain;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().print(jwtCheckResult.getErrMsg());
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
