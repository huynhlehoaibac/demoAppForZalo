package com.pa.demo.security.oauth2.client.userinfo;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private DefaultOAuth2UserService oauth2UserService = new DefaultOAuth2UserService();
  private CustomOAuth2UserRequestEntityConverter requestEntityConverter =
      new CustomOAuth2UserRequestEntityConverter();

  public CustomOAuth2UserService() {
    oauth2UserService.setRequestEntityConverter(requestEntityConverter);
    // oauth2UserService.setRestOperations(restOperations);
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    return oauth2UserService.loadUser(userRequest);
  }
}
