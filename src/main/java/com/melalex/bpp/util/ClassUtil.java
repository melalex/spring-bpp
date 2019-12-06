package com.melalex.bpp.util;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClassUtil {

  public static List<Class<?>> getClasses(final Object[] source) {
    final var classes = new ArrayList<Class<?>>(source.length);

    for (var i = 0; i < source.length; i++) {
      classes.set(i, source[i].getClass());
    }

    return classes;
  }
}
