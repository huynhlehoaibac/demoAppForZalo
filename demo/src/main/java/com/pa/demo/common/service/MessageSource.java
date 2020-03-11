package com.pa.demo.common.service;

public interface MessageSource {

  String getMessage(String code);

  String getMessage(String code, Object[] args);

  String getMessage(String code, Object[] args, String defaultMessage);

  String getMessage(String code, String defaultMessage);
}
