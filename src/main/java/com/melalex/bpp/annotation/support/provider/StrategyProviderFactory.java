package com.melalex.bpp.annotation.support.provider;

import static java.util.Map.entry;
import static com.melalex.bpp.util.AnnotationUtils.getAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import com.melalex.bpp.annotation.support.StrategyClassifier;
import com.melalex.bpp.annotation.support.reflection.MethodInvokerFactory;

@AllArgsConstructor
public class StrategyProviderFactory {

  private final MethodInvokerFactory methodInvokerFactory;

  public <T, A extends Annotation> T createProviderForInterface(
      final Class<T> interfaceClass,
      final Collection<Object> strategies,
      final StrategyClassifier<Object, A> classifier
  ) {
    final var annotationType = classifier.getAnnotationType();

    final var registry = strategies.stream()
        .map(o -> entry(
            classifier.classifyMatcher(getAnnotation(o, annotationType)),
            this.methodInvokerFactory.createMethodInvoker(o)
        ))
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));

    return interfaceClass.cast(Proxy.newProxyInstance(
        StrategyProviderFactory.class.getClassLoader(),
        new Class[]{interfaceClass},
        new StrategyProviderInvocationHandler<>(registry, classifier)
    ));
  }
}
