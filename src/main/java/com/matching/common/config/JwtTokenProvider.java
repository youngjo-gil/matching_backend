package com.matching.common.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.matching.member.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "bearer ";
    private static final String KEY_ROLES = "auth";

    private final Key key;

    public JwtTokenProvider(
        @Value("${jwt.access-token-expire-time}") long accessTime,
        @Value("${jwt.refresh-token-expire-time}") long refreshTime,
        @Value("${jwt.secret}") String secretKey
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
    }

    public String generateToken(Long id, List<String> roles, long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put(KEY_ROLES, roles);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(Long id, List<String> roles) {
        return this.generateToken(id, roles, ACCESS_TOKEN_EXPIRE_TIME);
    }


    public String createRefreshToken(Long id, List<String> roles) {
        return this.generateToken(id, roles, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String getMemberEmailByToken(String token) {
        return this.parseClaims(token).getSubject();
    }

    public TokenDto createTokenDto(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(BEARER_TYPE)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if(claims.get(KEY_ROLES) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(
                principal,
                "",
                authorities
        );
    }

    public Long getExpiration(String accessToken) {
        Date expiration = parseClaims(accessToken).getExpiration();
        long now = new Date().getTime();

        return (expiration.getTime() - now);
    }

    public int validateToken(String token) {
        if(!StringUtils.hasText(token)) return -1;

        try {
            Claims claims = parseClaims(token);
//            if(refreshTokenRepository.hasKeyBlackList(token)) {
//                // redis에 등록된 블랙리스트 access key가 존재하는지 확인
//                return -1;
//            }
            return 1;
        } catch (ExpiredJwtException e){
//            throw new RuntimeException("만료된 토큰입니다.");
            return 2;
        } catch (UnsupportedJwtException e){
//            throw new RuntimeException("토큰이 잘못되었습니다.");
            return -1;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(token) && token.startsWith(BEARER_TYPE)) {
            return token.substring(7);
        }

        return null;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody();
    }

    public void setHeaderToken(HttpServletResponse response, String token) {
        response.setHeader("refeshToken" , token);
    }

}
