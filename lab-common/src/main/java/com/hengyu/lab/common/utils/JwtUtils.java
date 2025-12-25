package com.hengyu.lab.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "lab.jwt")
public class JwtUtils {

  private String secret;
  private Long expiration;
  private String tokenHeader;
  private String tokenPrefix;

  public String createToken(String subject, Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date())
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parseToken(String token) {
    if (token.startsWith(tokenPrefix)) {
      token = token.substring(tokenPrefix.length());
    }

    return Jwts.parserBuilder()
        .setSigningKey(getKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

  }

  private Key getKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

}
