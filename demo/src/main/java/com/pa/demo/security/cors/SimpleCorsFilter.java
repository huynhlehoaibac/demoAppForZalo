package com.pa.demo.security.cors;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter makes sure spring handle option preflight request properly from frontend <br>
 * <a
 * src=https://stackoverflow.com/questions/30632200/standalone-spring-oauth2-jwt-authorization-server-cors?lq=1>
 * https://stackoverflow.com/questions/30632200/standalone-spring-oauth2-jwt-authorization-server-cors?lq=1</a>
 */
@Profile("dev")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;
    response.addHeader("Access-Control-Allow-Credentials", "true");
    response.addHeader("Access-Control-Expose-Headers",
        "Accept-Ranges, Content-Encoding, Content-Length, Content-Range, X-Requested-With, "
            + "Authorization, Content-Type, Set-Cookie, Accept-Language");
    response.setHeader("Access-Control-Allow-Origin", "https://localhost:4200");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "W-Requested-With, Authorization, Content-Type, Set-Cookie, Accept-Language");
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, res);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {
    // no init needed
  }

  @Override
  public void destroy() {
    // no destroy needed
  }
}
