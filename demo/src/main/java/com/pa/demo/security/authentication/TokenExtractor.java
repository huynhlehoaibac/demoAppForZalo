package com.pa.demo.security.authentication;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TokenExtractor {
  Authentication extract(HttpServletRequest request);
}
