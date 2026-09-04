package com.ecommerce.infrastructure.security;

import com.ecommerce.application.ports.output.TokenServicePort;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter implements TokenServicePort {

  private final String secret;
  private final long expirationMs;

  public JwtTokenAdapter(
      @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}") String secret,
      @Value("${jwt.expiration-ms:86400000}") long expirationMs
  ) {
    this.secret = secret;
    this.expirationMs = expirationMs;
  }

  @Override
  public String generateToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);
    String roleName = user.getRole() != null ? user.getRole().name() : UserRole.ROLE_CLIENT.name();

    return Jwts.builder()
        .subject(user.getEmail())
        .claim("userId", user.getId())
        .claim("name", user.getName())
        .claim("role", roleName)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  @Override
  public String extractEmail(String token) {
    return extractAllClaims(token).getSubject();
  }

  public String extractRole(String token) {
    return extractAllClaims(token).get("role", String.class);
  }

  @Override
  public boolean validateToken(String token, String email) {
    try {
      Claims claims = extractAllClaims(token);
      String tokenEmail = claims.getSubject();
      boolean notExpired = claims.getExpiration().after(new Date());
      return email != null && email.equalsIgnoreCase(tokenEmail) && notExpired;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
