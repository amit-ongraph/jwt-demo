package com.demo.jwtsecurity.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

import static java.util.Collections.emptyList;

@Component
public class TokenAuthenticationService {


    static final Long EXPIRATIONTIME = 10 * 24 * 60 * 60 * 1000L; // 10 days

    @Value("${JWT_SECRET}")
    private String jwt_secret;

    @Value("${JWT_TOKEN_PREFIX}")
    private String jwt_prefix;

    static final String HEADER_STRING = "Authorization";

    void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.forName("HS256"), jwt_secret)
                .compact();
        res.addHeader(HEADER_STRING, jwt_prefix + " " + JWT);
    }

    Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(jwt_secret)
                    .parseClaimsJws(token.replace(jwt_prefix, ""))
                    .getBody()
                    .getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                    null;
        }
        return null;
    }
}
