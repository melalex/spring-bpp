package com.melalex.bpp.annotation.support.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import lombok.Value;

import com.melalex.bpp.annotation.support.reflection.wrapers.CommonMethodWrapper;
import com.melalex.bpp.annotation.support.reflection.wrapers.MethodWrapper;
import com.melalex.bpp.annotation.support.reflection.wrapers.OverloadedMethodWrapper;
import com.melalex.bpp.util.ClassUtil;

@Value
public class MethodRegistry {

  private final Map<String, MethodWrapper> registry;

  public Method getMethod(final String methodName, final Object[] args) {
    final Method method;

    final var wrapper = this.registry.get(methodName);

    if (wrapper instanceof CommonMethodWrapper) {
      method = ((CommonMethodWrapper) wrapper).getMethod();
    } else if (wrapper instanceof OverloadedMethodWrapper) {
      method = ((OverloadedMethodWrapper) wrapper).getMethod(ClassUtil.getClasses(args));
    } else {
      throw new IllegalStateException(
          "There is no method [ " + methodName + " ] in [ " + this.registry + " ] registry");
    }

    if (method == null) {
      throw new IllegalStateException(
          "There is no method [ " + methodName + " ] in [ " + this.registry + " ] registry with [ "
              + Arrays
              .toString(args) + " ] args");
    }

    return method;
  }
}
