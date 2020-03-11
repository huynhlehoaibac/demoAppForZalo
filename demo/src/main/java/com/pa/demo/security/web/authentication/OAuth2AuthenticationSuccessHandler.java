package com.pa.demo.security.web.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import com.pa.demo.common.dto.UserDTO;
import com.pa.demo.common.service.UserService;
import com.pa.demo.security.authentication.JwtToken;
import com.pa.demo.security.authentication.JwtTokenFactory;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  @Value("${frontend.login-callback.url:https://localhost:4200/authx/login/callback/uaa}")
  private String frontendAuthenticationSuccessHandlerUrl;

  private final RandomValueStringGenerator randomValueStringGenerator =
      new RandomValueStringGenerator(8);

  private final JwtTokenFactory tokenFactory;
  private final UserDetailsService userDetailsService;
  private final UserService userService;

  @Autowired
  public OAuth2AuthenticationSuccessHandler(final JwtTokenFactory tokenFactory,
      final UserDetailsService userDetailsService, final UserService userService) {
    this.tokenFactory = tokenFactory;
    this.userDetailsService = userDetailsService;
    this.userService = userService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();

    AuthenticationUser authenticationUser = null;
    try {
      authenticationUser =
          (AuthenticationUser) userDetailsService.loadUserByUsername(oauth2User.getName());
    } catch (UsernameNotFoundException e) {
      // store new user to db
      // @formatter:off
      UserDTO userDTO = UserDTO.builder()
          .uid(oauth2User.getName())
          .name(oauth2User.getAttribute("name"))
          .build();
      // @formatter:on

      authenticationUser = userService.addUser(userDTO);
    }

    String jti = randomValueStringGenerator.generate();

    HttpSession session = request.getSession();
    session.setMaxInactiveInterval(300);
    session.setAttribute("jti", jti);

    JwtToken exchangeToken = tokenFactory.createExchangeToken(jti, authenticationUser);

    String redirectionUrl =
        UriComponentsBuilder.fromUriString(frontendAuthenticationSuccessHandlerUrl)
            .queryParam("exchange_token", exchangeToken.getToken()).build().toUriString();

    getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
  }
}
