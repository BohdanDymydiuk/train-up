package com.example.trainup.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Bearer ";
    private final ApplicationContext applicationContext;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getToken(request);
        if (token != null && jwtUtil.isValidToken(token)) {
            String username = jwtUtil.getUsername(token);
            List<String> roles = jwtUtil.getRolesFromToken(token);
            UserDetails userDetails = null;

            if (roles.contains("ROLE_ADMIN")) {
                AdminDetailsService adminDetailsService = applicationContext
                        .getBean(AdminDetailsService.class);
                userDetails = adminDetailsService.loadUserByUsername(username);
            } else if (roles.contains("ROLE_ATHLETE")) {
                AthleteDetailsService athleteDetailsService = applicationContext
                        .getBean(AthleteDetailsService.class);
                userDetails = athleteDetailsService.loadUserByUsername(username);
            }

            if (userDetails != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_HEADER)) {
            return bearerToken.substring(TOKEN_HEADER.length());
        }
        return null;
    }
}
