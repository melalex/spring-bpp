package com.melalex.bpp.strategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
public class StrategyProviderFactory {

  <T> T createProviderForInterface(
      final Class<T> interfaceClass,
      final StrategyResolver strategyResolver
  ) {
    return interfaceClass.cast(Proxy.newProxyInstance(
        StrategyProviderFactory.class.getClassLoader(),
        new Class[]{interfaceClass},
        StrategyProviderInvocationHandler.of(strategyResolver)
    ));
  }

  @AllArgsConstructor
  private static final class StrategyProviderInvocationHandler implements InvocationHandler {

    private final StrategyResolver strategyResolver;
    private final MethodRegistry methodRegistry;

    static StrategyProviderInvocationHandler of(final StrategyResolver strategyResolver) {
      return new StrategyProviderInvocationHandler(
          strategyResolver,
          MethodRegistry.forObjects(strategyResolver.availableStrategies())
      );
    }


    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
      final var strategy = strategyResolver.resolve(method, args);
      final var strategyMethod = methodRegistry
          .getMethod(strategy.getClass(), method.getName(), args);

      return strategyMethod.invoke(strategy, args);
    }
  }
}
