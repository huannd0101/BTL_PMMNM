package com.hit.compiler.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpStatus status;
  private String userMessage;
  private String devMessage;

  public NotFoundException(String userMessage, String devMessage) {
    super(userMessage);
    this.status = HttpStatus.BAD_REQUEST;
    this.userMessage = userMessage;
    this.devMessage = devMessage;
  }

  public NotFoundException(HttpStatus status, String userMessage, String devMessage) {
    super(userMessage);
    this.status = status;
    this.userMessage = userMessage;
    this.devMessage = devMessage;
  }

}
