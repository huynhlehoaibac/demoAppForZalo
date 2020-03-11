package com.pa.demo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.pa.demo.security.authentication.ExchangeTokenAuthenticationProvider;
import com.pa.demo.security.authentication.ExchangeTokenExtractor;
import com.pa.demo.security.authentication.JwtAuthenticationProvider;
import com.pa.demo.security.authentication.JwtHeaderTokenExtractor;
import com.pa.demo.security.authentication.RefreshTokenAuthenticationProvider;
import com.pa.demo.security.authentication.RefreshTokenExtractor;
import com.pa.demo.security.oauth2.client.endpoint.CustomAccessTokenResponseClient;
import com.pa.demo.security.oauth2.client.userinfo.CustomOAuth2UserService;
import com.pa.demo.security.oauth2.client.web.CustomOAuth2AuthorizationRequestResolver;
import com.pa.demo.security.web.DefaultAuthenticationEntryPoint;
import com.pa.demo.security.web.authentication.DefaultAuthenticationFailureHandler;
import com.pa.demo.security.web.authentication.DefaultAuthenticationSuccessHandler;
import com.pa.demo.security.web.authentication.ExchangeTokenAuthenticationProcessingFilter;
import com.pa.demo.security.web.authentication.ExchangeTokenAuthenticationSuccessHandler;
import com.pa.demo.security.web.authentication.JwtTokenAuthenticationProcessingFilter;
import com.pa.demo.security.web.authentication.OAuth2AuthenticationSuccessHandler;
import com.pa.demo.security.web.authentication.RefreshTokenAuthenticationProcessingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

  public static final String TOKEN_REFRESH_ENTRY_POINT = "/auth/token";
  public static final String EXCHANGE_TOKEN_ENTRY_POINT = "/auth/exchange-token";
  public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

  @Autowired
  private CustomAccessTokenResponseClient customAccessTokenResponseClient;
  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  @Autowired
  private ClientRegistrationRepository clientRegistrationRepository;
  @Autowired
  private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

  @Autowired
  private DefaultAuthenticationEntryPoint authenticationEntryPoint;
  @Autowired
  private DefaultAuthenticationSuccessHandler defaultSuccessHandler;
  @Autowired
  private DefaultAuthenticationFailureHandler defaultFailureHandler;

  @Autowired
  private RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;
  @Autowired
  private ExchangeTokenAuthenticationProvider exchangeTokenAuthenticationProvider;
  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Autowired
  private ExchangeTokenAuthenticationSuccessHandler exchangeTokenSuccessHandler;

  @Autowired
  private RefreshTokenExtractor refreshTokenExtractor;
  @Autowired
  private ExchangeTokenExtractor exchangeTokenExtractor;
  @Autowired
  private JwtHeaderTokenExtractor jwtHeaderTokenExtractor;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Bean
  public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customAuthorizationRequestRepository() {
    return new HttpSessionOAuth2AuthorizationRequestRepository();
  }

  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  protected AbstractAuthenticationProcessingFilter buildRefreshTokenAuthenticationProcessingFilter() {
    AbstractAuthenticationProcessingFilter filter =
        new RefreshTokenAuthenticationProcessingFilter(defaultSuccessHandler, defaultFailureHandler,
            refreshTokenExtractor, TOKEN_REFRESH_ENTRY_POINT);
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  protected AbstractAuthenticationProcessingFilter buildExchangeTokenAuthenticationProcessingFilter() {
    AbstractAuthenticationProcessingFilter filter =
        new ExchangeTokenAuthenticationProcessingFilter(exchangeTokenSuccessHandler,
            defaultFailureHandler, exchangeTokenExtractor, EXCHANGE_TOKEN_ENTRY_POINT);
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  protected AbstractAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() {
    AbstractAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(
        defaultFailureHandler, jwtHeaderTokenExtractor, TOKEN_BASED_AUTH_ENTRY_POINT);
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // @formatter:off
    auth
      .authenticationProvider(refreshTokenAuthenticationProvider)
      .authenticationProvider(exchangeTokenAuthenticationProvider)
      .authenticationProvider(jwtAuthenticationProvider);
    // @formatter:on
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .headers()
        .frameOptions().sameOrigin() // H2 Console Dash-board - only for testing

    .and()
      .csrf().disable()

      .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)

    .and()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        
    .and()
      .authorizeRequests()
        .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()

    .and()
      .oauth2Login()
        .redirectionEndpoint()
          .baseUri(("/oauth2/callback/*"))
      .and()
        .authorizationEndpoint()
          .authorizationRequestResolver(new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository))

      .and()
        .tokenEndpoint()
          .accessTokenResponseClient(customAccessTokenResponseClient)
      .and()
        .userInfoEndpoint()
          .userService(customOAuth2UserService)
      .and()
        .successHandler(oauth2AuthenticationSuccessHandler)

    .and()
      .addFilterBefore(buildRefreshTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(buildExchangeTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
    ;
    // @formatter:on
  }
}
