package com.melalex.bpp.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtil {

  public static Class<?>[] getClasses(Object[] source) {
    final var classes = new Class<?>[source.length];

    for (int i = 0; i < source.length; i++) {
      classes[i] = source[i].getClass();
    }

    return classes;
  }
}
