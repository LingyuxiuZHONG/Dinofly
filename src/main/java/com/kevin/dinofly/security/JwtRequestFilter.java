package com.kevin.dinofly.security;

import cn.hutool.json.JSONUtil;
import com.kevin.dinofly.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization input:{}",authorizationHeader);
        String jwt = null;
        Long id = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            id = jwtUtil.extractId(jwt);
        }

        if(id != null && SecurityContextHolder.getContext().getAuthentication() == null){
            CustomUserDetails userDetails = userDetailsService.loadUserById(id);

            if(jwtUtil.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                log.info("Authorization user:{}", JSONUtil.toJsonStr(userDetails));
            }
        }
        filterChain.doFilter(request,response);
    }
}
