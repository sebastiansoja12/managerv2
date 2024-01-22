package com.warehouse.softwareconfiguration.configuration;

import java.io.IOException;

import com.warehouse.softwareconfiguration.configuration.exception.ApplicationAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		OBJECT_MAPPER.writeValue(response.getOutputStream(),
				new ApplicationAccessException(401, authException.getLocalizedMessage()));
    }
}
