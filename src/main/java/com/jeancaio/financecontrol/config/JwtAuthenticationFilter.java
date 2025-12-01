package com.jeancaio.financecontrol.config;

import com.jeancaio.financecontrol.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // Extrair token JWT do header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            try {
                email = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                System.out.println("Erro ao extrair email do token: " + e.getMessage());
            }
        }

        // Autenticação + inserção do userId na request
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Verificar token
            if (jwtUtil.validateToken(jwt, email)) {

                // PEGAR o userId DIRETO DO TOKEN
                Long userId = jwtUtil.extractUserId(jwt);

                // Carregar usuário para autenticação Spring Security
                UserDetails userDetails = userService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ➤ Adicionar userId na requisição (controller usa isso!)
                request.setAttribute("userId", userId);

                // Registrar autenticação
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
