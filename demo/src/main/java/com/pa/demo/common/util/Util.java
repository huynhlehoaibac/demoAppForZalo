package com.pa.demo.common.util;

public class Util {

  private Util() {
    throw new UnsupportedOperationException(
        "Cannot instantiate single object using constructor. Use its #getInstance() method");
  }

  /**
   * Construct a full like expression for searching
   *
   * @param value value to be wrapped
   * @param caseSensitive if caseSensitive is set to true, value will automatically convert to lower
   *        case
   * @return like expression
   */
  public static String simpleFullLikeExpression(String value, boolean caseSensitive) {
    return String.format("%%%s%%", caseSensitive ? value : value.toLowerCase());
  }
}
