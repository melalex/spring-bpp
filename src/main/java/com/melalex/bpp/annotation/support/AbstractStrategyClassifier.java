package com.melalex.bpp.annotation.support;

import java.lang.annotation.Annotation;

import org.springframework.core.GenericTypeResolver;

public abstract class AbstractStrategyClassifier<K, A extends Annotation> implements
    StrategyClassifier<K, A> {

  private final Class<A> annotationType;

  public AbstractStrategyClassifier() {
    final var generics = GenericTypeResolver
        .resolveTypeArguments(this.getClass(), StrategyClassifier.class);

    if (generics == null || generics.length < 1) {
      throw new IllegalStateException(
          "Can't resolve generics of [ " + this.getClass() + " ]");
    }

    //noinspection unchecked
    this.annotationType = (Class<A>) generics[1];
  }

  @Override
  public Class<A> getAnnotationType() {
    return this.annotationType;
  }
}
