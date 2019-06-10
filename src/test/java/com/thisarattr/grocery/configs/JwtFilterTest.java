package com.thisarattr.grocery.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtFilterTest {


    private JwtFilter filter;

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private FilterChain mockFilterChain;
    private String secretKey = "eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIiL";

    @Before
    public void setUp() throws Exception {
        filter = new JwtFilter(secretKey);
    }

    @Test
    public void shouldSendOkWhenHttpMethodIsOption() throws Exception {
        when(mockRequest.getMethod()).thenReturn("OPTIONS");
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldSendUnAuthorisedWhenTokenIsNotProvided() throws Exception {
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
    }

    @Test
    public void shouldSendUnAuthorisedWhenTokenNotInCorrectFormat() throws Exception {
        when(mockRequest.getHeader("authorization")).thenReturn("invalidToken");
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
    }

    @Test
    public void shouldSendUnauthorisedWhenJwtExpired() throws Exception {
        Date issuedAt = new Date();
        String token = createJwt("user", "role", issuedAt, new Date(issuedAt.getTime() - 60000));
        when(mockRequest.getHeader("authorization")).thenReturn("Bearer " + token);
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
    }

    @Test
    public void shouldSendOkWhenJwtIsValidAndCurrent() throws Exception  {
        Date issuedAt = new Date();
        String token = createJwt("user", "role", issuedAt, new Date(issuedAt.getTime() + 60000));
        when(mockRequest.getHeader("authorization")).thenReturn("Bearer " + token);
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockRequest).setAttribute(anyString(), any(Claims.class));
    }

    @Test
    public void shouldSendUnauthorisedWhenMalformedToken() throws Exception  {
        String token = "123";
        when(mockRequest.getHeader("authorization")).thenReturn("Bearer " + token);
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Invalid");
    }

    private String createJwt(String username, String role, Date issuedAt, Date expiry) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("username", username)
                .setId("jti" + RandomStringUtils.random(7, true, true))
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuer("abc grocery")
                .compact();
    }

}