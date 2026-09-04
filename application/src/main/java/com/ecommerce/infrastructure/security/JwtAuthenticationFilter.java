package com.ecommerce.infrastructure.security;

import com.ecommerce.application.ports.output.TokenServicePort;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenServicePort tokenServicePort;
  private final UserRepositoryPort userRepositoryPort;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7).trim();
      try {
        String email = tokenServicePort.extractEmail(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          if (tokenServicePort.validateToken(token, email)) {
            Optional<User> userOptional = userRepositoryPort.findByEmail(email);
            if (userOptional.isPresent()) {
              User user = userOptional.get();
              String roleName = user.getRole() != null ? user.getRole().name() : "ROLE_CLIENT";
              List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));
              UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(user, null, authorities);
              authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authentication);
            }
          }
        }
      } catch (Exception ignored) {
        // Token parsing or validation failed; SecurityContext remains unauthenticated.
      }
    }

    filterChain.doFilter(request, response);
  }
}
