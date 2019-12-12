package com.melalex.bpp.annotation.support.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import lombok.AllArgsConstructor;

import com.melalex.bpp.annotation.support.StrategyClassifier;
import com.melalex.bpp.annotation.support.reflection.MethodInvoker;

@AllArgsConstructor
final class StrategyProviderInvocationHandler<K, A extends Annotation> implements
    InvocationHandler {

  private final Map<K, MethodInvoker> registry;
  private final StrategyClassifier<K, A> strategyClassifier;

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) {
    final var key = this.strategyClassifier.classifyKey(method, args);
    final var strategy = this.registry.get(key);

    if (strategy == null) {
      throw new IllegalArgumentException(
          "No strategy for key [ " + key + " ] in registry [ " + this.registry + " ]"
      );
    }

    return strategy.invoke(method.getName(), args);
  }
}
