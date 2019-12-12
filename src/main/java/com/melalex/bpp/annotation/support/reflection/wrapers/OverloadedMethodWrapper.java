package com.melalex.bpp.annotation.support.reflection.wrapers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import lombok.Value;

@Value
public class OverloadedMethodWrapper implements MethodWrapper {

  private Map<List<Class<?>>, Method> registry;

  public Method getMethod(final List<Class<?>> args) {
    return this.registry.get(args);
  }
}
