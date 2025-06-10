package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthTokenFilter implements Filter {

    private static final String SECRET = "secret123";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest original = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        String authHeader = original.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        String userId = jwt.getClaim("employeenumber").asString();
        String givenName = jwt.getClaim("given_name").asString();
        String familyName = jwt.getClaim("family_name").asString();
        String patronymic = jwt.getClaim("patronymic").asString();

        HttpServletRequest wrapped = new HttpServletRequestWrapper(original) {
            @Override
            public Object getAttribute(String name) {
                return switch (name) {
                    case "employeenumber" -> userId;
                    case "given_name" -> givenName;
                    case "family_name" -> familyName;
                    case "patronymic" -> patronymic;
                    default -> super.getAttribute(name);
                };
            }
        };

        chain.doFilter(wrapped, httpResp);
    }
}

