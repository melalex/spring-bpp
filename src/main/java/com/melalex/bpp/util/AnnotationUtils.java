package com.melalex.bpp.util;

import java.lang.annotation.Annotation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AnnotationUtils {

  public static <A extends Annotation> A getAnnotation(
      final Object object,
      final Class<A> annotationType
  ) {
    final var result = org.springframework.core.annotation.AnnotationUtils
        .getAnnotation(object.getClass(), annotationType);

    if (result == null) {
      throw new IllegalStateException(
          "Can't find annotation [ " + annotationType + " ] on class [ " + object.getClass()
              + " ]");
    }

    return result;
  }
}
