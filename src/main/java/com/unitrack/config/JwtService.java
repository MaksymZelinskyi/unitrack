package com.unitrack.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static com.unitrack.util.Constants.WORKSPACE_ID;

@Service
public class JwtService {

    private final long EXPIRATION = 24 * 60 * 60 * 1000 ;
    private final String SECRET = "I'm not innocent but responsible for everything";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public String generateToken(String username, Long workspaceId) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .setClaims(Map.of(WORKSPACE_ID, workspaceId))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractWorkspaceId(String token) {
        return extractClaim(token, x -> x.get(WORKSPACE_ID, Long.class));
    }

    public boolean hasExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        if(hasExpired(token)) return false;
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    public <T> T extractClaim(String token, Function<Claims, T> function) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return function.apply(claims);
    }
}
