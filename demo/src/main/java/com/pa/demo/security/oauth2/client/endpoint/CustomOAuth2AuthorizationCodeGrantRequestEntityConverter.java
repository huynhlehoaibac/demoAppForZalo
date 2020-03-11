package com.pa.demo.security.oauth2.client.endpoint;

import java.net.URI;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomOAuth2AuthorizationCodeGrantRequestEntityConverter
    implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
  private static final String APP_ID = "app_id";
  private static final String APP_SECRET = "app_secret";

  /**
   * Returns the {@link RequestEntity} used for the Access Token Request.
   *
   * @param authorizationCodeGrantRequest the authorization code grant request
   * @return the {@link RequestEntity} used for the Access Token Request
   */
  public RequestEntity<?> convert(
      OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
    ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();

    // HttpHeaders headers =
    // OAuth2AuthorizationGrantRequestEntityUtils.getTokenRequestHeaders(clientRegistration);
    MultiValueMap<String, String> formParameters =
        this.buildFormParameters(authorizationCodeGrantRequest);
    URI uri =
        UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getTokenUri())
            .queryParams(formParameters).build().toUri();

    // return new RequestEntity<>(formParameters, headers, HttpMethod.POST, uri);
    return new RequestEntity<>(HttpMethod.GET, uri);
  }

  private MultiValueMap<String, String> buildFormParameters(
      OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
    ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
    OAuth2AuthorizationExchange authorizationExchange =
        authorizationCodeGrantRequest.getAuthorizationExchange();

    MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
    formParameters.add(OAuth2ParameterNames.GRANT_TYPE,
        authorizationCodeGrantRequest.getGrantType().getValue());
    formParameters.add(OAuth2ParameterNames.CODE,
        authorizationExchange.getAuthorizationResponse().getCode());
    String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
    String codeVerifier = authorizationExchange.getAuthorizationRequest()
        .getAttribute(PkceParameterNames.CODE_VERIFIER);
    if (redirectUri != null) {
      formParameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
    }
    // if (!ClientAuthenticationMethod.BASIC
    // .equals(clientRegistration.getClientAuthenticationMethod())) {
    // formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
    // }
    formParameters.add(APP_ID, clientRegistration.getClientId());
    // if (ClientAuthenticationMethod.POST
    // .equals(clientRegistration.getClientAuthenticationMethod())) {
    // formParameters.add(OAuth2ParameterNames.CLIENT_SECRET,
    // clientRegistration.getClientSecret());
    // }
    formParameters.add(APP_SECRET, clientRegistration.getClientSecret());
    if (codeVerifier != null) {
      formParameters.add(PkceParameterNames.CODE_VERIFIER, codeVerifier);
    }

    return formParameters;
  }
}
