package com.pa.demo.security.web.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.demo.security.authentication.JwtToken;
import com.pa.demo.security.authentication.JwtTokenFactory;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

@Component
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  private final ObjectMapper mapper;
  private final JwtTokenFactory tokenFactory;

  @Autowired
  public DefaultAuthenticationSuccessHandler(final ObjectMapper mapper,
      final JwtTokenFactory tokenFactory) {
    this.mapper = mapper;
    this.tokenFactory = tokenFactory;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    AuthenticationUser authenticationUser = (AuthenticationUser) authentication.getPrincipal();

    JwtToken accessToken = tokenFactory.createAccessJwtToken(authenticationUser);
    JwtToken refreshToken = tokenFactory.createRefreshToken(authenticationUser);

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("accessToken", accessToken.getToken());
    tokenMap.put("refreshToken", refreshToken.getToken());

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    mapper.writeValue(response.getWriter(), tokenMap);
  }
}
