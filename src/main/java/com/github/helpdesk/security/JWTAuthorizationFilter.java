package com.github.helpdesk.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.helpdesk.resource.exception.StandardError;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String header = request.getHeader("Authorization");
			if (header != null && header.startsWith("Bearer ")) {
				UsernamePasswordAuthenticationToken authToken = getAuthentication(header.substring(7));
				if (authToken != null) {
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setContentType("application/json");
			response.getWriter()
					.write(new ObjectMapper().writeValueAsString(new StandardError(System.currentTimeMillis(),
							HttpStatus.FORBIDDEN.value(), e.getMessage(), "Token Expirado", request.getRequestURI())));
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.validateToken(token)) {
			String username = jwtUtil.extractUsername(token);
			UserDetails details = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
		}
		return null;
	}

}
