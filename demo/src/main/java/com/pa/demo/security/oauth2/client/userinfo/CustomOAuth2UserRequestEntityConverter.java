package com.pa.demo.security.oauth2.client.userinfo;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import java.net.URI;
import java.util.Collections;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomOAuth2UserRequestEntityConverter
    implements Converter<OAuth2UserRequest, RequestEntity<?>> {
  private static final MediaType DEFAULT_CONTENT_TYPE =
      MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

  /**
   * Returns the {@link RequestEntity} used for the UserInfo Request.
   *
   * @param userRequest the user request
   * @return the {@link RequestEntity} used for the UserInfo Request
   */
  @Override
  public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
    ClientRegistration clientRegistration = userRequest.getClientRegistration();

    HttpMethod httpMethod = HttpMethod.GET;
    if (AuthenticationMethod.FORM.equals(
        clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod())) {
      httpMethod = HttpMethod.POST;
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.valueOf("text/json")));

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("access_token", userRequest.getAccessToken().getTokenValue());
    queryParams.add("fields", "id,birthday,name,gender");

    URI uri = UriComponentsBuilder
        .fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
        .queryParams(queryParams).build().toUri();

    RequestEntity<?> request;
    // if (HttpMethod.POST.equals(httpMethod)) {
    // headers.setContentType(DEFAULT_CONTENT_TYPE);
    // MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
    // formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN,
    // userRequest.getAccessToken().getTokenValue());
    // request = new RequestEntity<>(formParameters, headers, httpMethod, uri);
    // } else {
    // headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());

    // request = new RequestEntity<>(headers, httpMethod, uri);
    // }

    request = new RequestEntity<>(headers, httpMethod, uri);

    return request;
  }
}
