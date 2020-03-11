package com.pa.demo.common.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum GenderEnum {

  MALE("M"), FEMALE("F");

  @Getter
  String code;

  GenderEnum(String code) {
    this.code = code;
  }

  private static Map<String, GenderEnum> map = new HashMap<>();

  static {
    for (GenderEnum type : GenderEnum.values()) {
      map.put(type.code, type);
    }
  }

  public static GenderEnum getEnum(String code) {
    return map.get(code);
  }
}
