package com.hit.compiler.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpStatus status;
  private String userMessage;
  private String devMessage;

  public AccessDeniedException() {
    super("Access denied");
    this.status = HttpStatus.FORBIDDEN;
    this.userMessage = "exception.access.denied";
    this.devMessage = "Access denied";
  }

}
