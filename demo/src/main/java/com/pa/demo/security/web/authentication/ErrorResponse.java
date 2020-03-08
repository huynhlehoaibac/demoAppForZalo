package com.pa.demo.security.web.authentication;

import java.util.Date;
import java.util.Map;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Builder.Default;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ErrorResponse {
  @Default
  private Date timestamp = new Date();
  private HttpStatus status;
  private String error;
  private Map<String, Object> errors;
  private String message;
  private String path;

  public Date getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status.value();
  }

  public String getError() {
    return error;
  }

  public Map<String, Object> getErrors() {
    return errors;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }
}
