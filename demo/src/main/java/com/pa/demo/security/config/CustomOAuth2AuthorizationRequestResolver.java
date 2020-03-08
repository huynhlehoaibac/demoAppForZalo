package com.pa.demo.security.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;

public class CustomOAuth2AuthorizationRequestResolver
    implements OAuth2AuthorizationRequestResolver {
  private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;
  private static final String APP_ID = "app_id";

  public CustomOAuth2AuthorizationRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository) {

    this.defaultAuthorizationRequestResolver =
        new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
            OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest =
        this.defaultAuthorizationRequestResolver.resolve(request);

    return authorizationRequest != null ? customAuthorizationRequest(request, authorizationRequest)
        : null;
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request,
      String clientRegistrationId) {

    OAuth2AuthorizationRequest authorizationRequest =
        this.defaultAuthorizationRequestResolver.resolve(request, clientRegistrationId);

    return authorizationRequest != null ? customAuthorizationRequest(request, authorizationRequest)
        : null;
  }

  private OAuth2AuthorizationRequest customAuthorizationRequest(HttpServletRequest request,
      OAuth2AuthorizationRequest authorizationRequest) {

    Map<String, Object> parameters = new HashMap<>();

    String appId = authorizationRequest.getClientId();
    if (!StringUtils.isEmpty(appId)) {
      parameters.put(APP_ID, appId);
    }

    Map<String, Object> additionalParameters =
        new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
    additionalParameters.putAll(parameters);

    return OAuth2AuthorizationRequest.from(authorizationRequest)
        .additionalParameters(additionalParameters).build();
  }
}
