package com.thisarattr.grocery.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtFilter extends GenericFilterBean {

    private static final String HEADER_AUTHORIZATION = "authorization";
    private static final String HTTP_OPTIONS = "OPTIONS";
    private static final String AUTH_PREFIX = "Bearer ";
    private static final String ATTRIBUTE_CLAIMS = "claims";
    private String secretKey;

    public JwtFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (HTTP_OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith(AUTH_PREFIX)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }

            final String token = authHeader.substring(7);

            try {
                final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
                String username = claims.get("username", String.class);
                //TODO implement token reissue when its about to expire

                request.setAttribute(ATTRIBUTE_CLAIMS, claims);
            } catch (SignatureException | MalformedJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Invalid");
                return;
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            }

            chain.doFilter(req, res);
        }
    }
}
