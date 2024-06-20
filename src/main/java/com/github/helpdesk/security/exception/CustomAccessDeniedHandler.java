package com.github.helpdesk.security.exception;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.helpdesk.resource.exception.StandardError;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		StandardError error = new StandardError(new Date().getTime(), HttpServletResponse.SC_FORBIDDEN,
				"Não autorizado", "Você não tem permissão para acessar esse recurso", request.getServletPath());
		String jsonError = new ObjectMapper().writeValueAsString(error);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.getWriter().append(jsonError);
	}

}
