package com.melalex.bpp.annotation.support;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

import lombok.Value;

@Value(staticConstructor = "of")
public class MethodInvocation {

  private final Object target;
  private final Method method;

  public Object invoke(final Object... args) {
    return ReflectionUtils.invokeMethod(this.method, this.target, args);
  }
}
