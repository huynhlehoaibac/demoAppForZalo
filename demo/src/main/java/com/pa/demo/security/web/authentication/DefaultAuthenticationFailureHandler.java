package com.pa.demo.security.web.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
  private final ObjectMapper mapper;

  @Autowired
  public DefaultAuthenticationFailureHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException e) throws IOException, ServletException {
    log.debug("Authentication failed: {}", e.getMessage());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    // @formatter:off
    ErrorResponse errorResponse = ErrorResponse.builder()
      .status(HttpStatus.UNAUTHORIZED)
      .error("Unauthorized")
      .message(e.getMessage())
      .path(request.getRequestURI().substring(request.getContextPath().length()))
      .build();
    // @formatter:on

    mapper.writeValue(response.getWriter(), errorResponse);
  }
}
