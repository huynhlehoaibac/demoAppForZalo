package com.pa.demo.security.config;

import java.util.Arrays;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAccessTokenResponseClient
    implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

  private CustomOAuth2AuthorizationCodeGrantRequestEntityConverter requestEntityConverter =
      new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter();
  private DefaultAuthorizationCodeTokenResponseClient client =
      new DefaultAuthorizationCodeTokenResponseClient();
  private RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(),
      new OAuth2AccessTokenResponseHttpMessageConverter(),
      new CustomOAuth2AccessTokenResponseHttpMessageConverter()));

  @Override
  public OAuth2AccessTokenResponse getTokenResponse(
      OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {

    client.setRestOperations(restTemplate);
    client.setRequestEntityConverter(requestEntityConverter);
    return client.getTokenResponse(authorizationCodeGrantRequest);
  }
}
