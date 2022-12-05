package com.hit.compiler.exception;

import com.hit.compiler.base.RestData;
import com.hit.compiler.base.VsResponseUtil;
import com.hit.compiler.constant.UserMessageConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerConfig {

  private static final Log LOG = LogFactory.getLog(ExceptionHandlerConfig.class);

  private final MessageSource messageSource;

  public ExceptionHandlerConfig(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(value = {InvalidException.class})
  protected ResponseEntity<RestData<?>> handleVsException(InvalidException ex) {
    LOG.error(ex.getMessage(), ex);

    String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, ex.getDevMessage());
  }

  @ExceptionHandler(value = {VsException.class})
  protected ResponseEntity<RestData<?>> handleVsException(VsException ex) {
    LOG.error(ex.getMessage(), ex);

    String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getDevMessage());
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  protected ResponseEntity<RestData<?>> handleAccessDeniedException(AccessDeniedException ex) {
    LOG.error(ex.getMessage(), ex);

    String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    return VsResponseUtil.error(HttpStatus.FORBIDDEN, message, ex.getDevMessage());
  }

  @ExceptionHandler(value = {NotFoundException.class})
  protected ResponseEntity<RestData<?>> handleNotFoundException(NotFoundException ex) {
    LOG.error(ex.getMessage(), ex);

    String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    return VsResponseUtil.error(HttpStatus.NOT_FOUND, message, ex.getDevMessage());
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<RestData<?>> handleConstraintViolationException(ConstraintViolationException ex) {
    LOG.error(ex.getMessage(), ex);

    String message;
    try {
      message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    } catch (Exception e) {
      message = ex.getMessage();
    }
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, ex.getCause() != null ?
        ex.getCause().getMessage() : ex.getMessage());
  }

  @ExceptionHandler(value = {BindException.class})
  protected ResponseEntity<RestData<?>> handleBindException(BindException ex) {
    LOG.error(ex.getMessage(), ex);

    String message = "";
    if (ex.getFieldError() != null) {
      try {
        message = messageSource.getMessage(Objects.requireNonNull(ex.getFieldError().getDefaultMessage()), null,
            LocaleContextHolder.getLocale()
        );
      } catch (Exception e) {
        message = messageSource.getMessage("invalid.general.required", null,
            LocaleContextHolder.getLocale()
        );
      }
    }
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, ex.getCause() != null ?
        ex.getCause().getMessage() : ex.getMessage());
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<RestData<?>> handleException(Exception ex) {
    LOG.error(ex.getMessage(), ex);

    if (ex.getCause() instanceof VsException) {
      VsException vsException = (VsException) ex.getCause();
      String message = messageSource.getMessage(vsException.getUserMessage(), null, LocaleContextHolder.getLocale());
      return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, vsException.getDevMessage());
    }
    if (ex.getCause() instanceof AccessDeniedException) {
      String message = messageSource.getMessage(UserMessageConstant.ACCESS_DENIED, null,
          LocaleContextHolder.getLocale());
      return VsResponseUtil.error(HttpStatus.FORBIDDEN, message, "access denied");
    }
    if (ex.getCause() instanceof NotFoundException) {
      String message = messageSource.getMessage(UserMessageConstant.ERR_EXCEPTION_GENERAL, null,
          LocaleContextHolder.getLocale());
      return VsResponseUtil.error(HttpStatus.NOT_FOUND, message, "access denied");
    }
    if (ex.getCause() instanceof InvalidException) {
      InvalidException invalidException = (InvalidException) ex.getCause();
      String message = messageSource.getMessage(invalidException.getUserMessage(), null,
          LocaleContextHolder.getLocale()
      );
      return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, invalidException.getDevMessage());
    }
    String message = messageSource.getMessage("exception.general", null, LocaleContextHolder.getLocale());
    return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getCause() != null ?
        ex.getCause().getMessage() : ex.getMessage());
  }

}
