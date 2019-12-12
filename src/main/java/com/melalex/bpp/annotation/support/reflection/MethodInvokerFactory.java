package com.melalex.bpp.annotation.support.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.util.ReflectionUtils;

import com.melalex.bpp.annotation.support.reflection.wrapers.CommonMethodWrapper;
import com.melalex.bpp.annotation.support.reflection.wrapers.MethodWrapper;
import com.melalex.bpp.annotation.support.reflection.wrapers.OverloadedMethodWrapper;

public class MethodInvokerFactory {

  public MethodRegistry createMethodRegistry(final Object object) {
    final var clazz = object.getClass();

    final var registry = Arrays.stream(ReflectionUtils.getUniqueDeclaredMethods(clazz))
        .collect(Collectors.groupingBy(Method::getName))
        .entrySet()
        .stream()
        .map(e -> {
          final var methods = e.getValue();
          final MethodWrapper methodWrapper;

          if (methods.size() == 1) {
            methodWrapper = new CommonMethodWrapper(methods.get(0));
          } else {
            final var overloadedMethods = methods.stream()
                .map(m -> Map.entry(List.of(m.getParameterTypes()), m))
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));

            methodWrapper = new OverloadedMethodWrapper(overloadedMethods);
          }

          return Map.entry(e.getKey(), methodWrapper);
        })
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));

    return new MethodRegistry(registry);
  }

  public MethodInvoker createMethodInvoker(final Object object) {
    return new MethodInvoker(object, this.createMethodRegistry(object));
  }
}
