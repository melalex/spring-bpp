package com.melalex.bpp.annotation.support.reflection;

import org.springframework.util.ReflectionUtils;

import lombok.Value;

@Value
public class MethodInvoker {

  private Object target;
  private MethodRegistry methodRegistry;

  public Object invoke(final String methodName, final Object... args) {
    return ReflectionUtils
        .invokeMethod(this.methodRegistry.getMethod(methodName, args), this.target, args);
  }
}
