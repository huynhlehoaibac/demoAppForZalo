package com.pa.demo.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageSourceImpl implements com.pa.demo.common.service.MessageSource {

  @Autowired
  private MessageSource messageSource;

  @Override
  public String getMessage(String code) {
    return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
  }

  @Override
  public String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
  }

  @Override
  public String getMessage(String code, Object[] args, String defaultMessage) {
    return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
  }

  @Override
  public String getMessage(String code, String defaultMessage) {
    return messageSource.getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
  }
}
