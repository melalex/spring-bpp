package com.melalex.bpp.strategy;

import static java.util.Map.entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;

abstract public class AbstractStrategyResolver<K, A extends Annotation> implements
    StrategyResolver<A> {

  private Map<K, Object> registry;

  private final Class<A> annotationType;

  @SuppressWarnings("unchecked")
  protected AbstractStrategyResolver() {
    this.annotationType = (Class<A>) GenericTypeResolver
        .resolveTypeArgument(this.getClass(), StrategyResolver.class);
  }

  @Override
  public Object resolve(final Method method, final Object[] args) {
    final var key = this.provideKey(method, args);
    final var strategy = this.registry.get(key);

    if (strategy == null) {
      throw new IllegalArgumentException(
          "No strategy for key [ " + key + " ] in registry [ " + this.registry + " ]"
      );
    }

    return strategy;
  }

  @Override
  public void setStrategies(final List<Object> strategies) {
    this.registry = strategies.stream()
        .map(o -> entry(this.provideMatcher(this.getAnnotation(o)), o))
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
  }

  @Override
  public Collection<Object> availableStrategies() {
    return this.registry.values();
  }

  @Override
  public Class<A> getAnnotationType() {
    return this.annotationType;
  }

  private A getAnnotation(final Object object) {
    final var result = AnnotationUtils.getAnnotation(object.getClass(), this.annotationType);

    if (result == null) {
      throw new IllegalStateException(
          "Can't find annotation [ " + this.annotationType + " ] on class [ " + object.getClass()
              + " ]");
    }

    return result;
  }

  protected abstract K provideKey(Method method, Object[] args);

  protected abstract K provideMatcher(A annotation);
}
