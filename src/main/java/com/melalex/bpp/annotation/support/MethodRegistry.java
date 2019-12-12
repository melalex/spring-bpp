package com.melalex.bpp.annotation.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.util.ReflectionUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import com.melalex.bpp.util.ClassUtil;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodRegistry {

  private final Map<Class<?>, ClassMethods> registry;

  static MethodRegistry forObjects(final Collection<Object> objects) {
    final Map<Class<?>, ClassMethods> registry = objects.stream()
        .map(o -> {
          final var clazz = o.getClass();

          final var map = Arrays.stream(ReflectionUtils.getUniqueDeclaredMethods(clazz))
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

          return Map.entry(clazz, new ClassMethods(map));
        })
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));

    return new MethodRegistry(registry);
  }

  Method getMethod(final Class<?> clazz, final String method, final Object[] args) {
    final Method result;

    final var classMethods = this.registry.get(clazz);

    if (classMethods == null) {
      throw new IllegalStateException(
          "Class [ " + clazz + " ] isn't in registry [ " + this.registry + " ]");
    }

    final var wrapper = classMethods.getByName(method);

    if (wrapper instanceof CommonMethodWrapper) {
      result = ((CommonMethodWrapper) wrapper).getMethod();
    } else if (wrapper instanceof OverloadedMethodWrapper) {
      result = ((OverloadedMethodWrapper) wrapper).getMethod(ClassUtil.getClasses(args));
    } else {
      throw new IllegalStateException(
          "There is no method [ " + method + " ] in [ " + clazz + " ] class");
    }

    if (result == null) {
      throw new IllegalStateException(
          "There is no method [ " + method + " ] in [ " + clazz + " ] class with [ " + Arrays
              .toString(args) + " ] args");
    }

    return result;
  }

  private interface MethodWrapper {

  }

  @Value
  private static class ClassMethods {

    private Map<String, MethodWrapper> registry;

    MethodWrapper getByName(final String name) {
      return this.registry.get(name);
    }
  }


  @Value
  private static class CommonMethodWrapper implements MethodWrapper {

    private Method method;
  }

  @Value
  private static class OverloadedMethodWrapper implements MethodWrapper {

    private Map<List<Class<?>>, Method> registry;

    Method getMethod(final List<Class<?>> args) {
      return this.registry.get(args);
    }
  }
}
