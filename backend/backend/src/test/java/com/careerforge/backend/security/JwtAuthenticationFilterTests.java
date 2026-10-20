
package com.careerforge.backend.security;
import com.careerforge.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTests {

    private JwtService jwtService;
    private JwtAuthenticationFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        filter = new JwtAuthenticationFilter(jwtService);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipAuthenticationEndpoints() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/auth/login");

        assertTrue(filter.shouldNotFilter(request));
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldNotSkipOtherEndpoints() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/profile");

        assertFalse(filter.shouldNotFilter(request));
    }

    @Test
    void shouldContinueWhenAuthorizationHeaderIsMissing()
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueWhenAuthorizationHeaderIsNotBearer()
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic abc123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid()
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractEmail("valid-token"))
                .thenReturn("test@example.com");

        filter.doFilterInternal(request, response, filterChain);

        var authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals("test@example.com", authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueWhenTokenIsInvalid()
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractEmail("invalid-token"))
                .thenThrow(new RuntimeException("Invalid token"));

        assertDoesNotThrow(
                () -> filter.doFilterInternal(request, response, filterChain)
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotReplaceExistingAuthentication()
            throws ServletException, IOException {
        var existingAuthentication =
                new UsernamePasswordAuthenticationToken(
                        "existing@example.com", null, java.util.Collections.emptyList()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(existingAuthentication);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractEmail("valid-token"))
                .thenReturn("another@example.com");

        filter.doFilterInternal(request, response, filterChain);

        assertSame(
                existingAuthentication,
                SecurityContextHolder.getContext().getAuthentication()
        );
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueWhenTokenContainsNoEmail()
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-without-email");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractEmail("token-without-email")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}