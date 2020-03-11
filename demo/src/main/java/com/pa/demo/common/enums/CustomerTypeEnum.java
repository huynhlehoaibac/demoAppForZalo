package com.pa.demo.common.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum CustomerTypeEnum {

  VIP("A"), REGULAR("B"), STANDARD("C"), NEW("D");

  @Getter
  String code;

  CustomerTypeEnum(String code) {
    this.code = code;
  }

  private static Map<String, CustomerTypeEnum> map = new HashMap<>();

  static {
    for (CustomerTypeEnum type : CustomerTypeEnum.values()) {
      map.put(type.code, type);
    }
  }

  public static CustomerTypeEnum getEnum(String code) {
    return map.get(code);
  }
}
