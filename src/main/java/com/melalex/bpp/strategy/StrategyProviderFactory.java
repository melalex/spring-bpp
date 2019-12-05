package com.melalex.bpp.strategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.melalex.bpp.util.ArrayUtil;

@Component
public class StrategyProviderFactory {

  public <T> T createProviderForInterface(
      Class<T> interfaceClass,
      StrategyResolver strategyResolver
  ) {
    return interfaceClass.cast(Proxy.newProxyInstance(
        StrategyProviderFactory.class.getClassLoader(),
        new Class[]{interfaceClass},
        new StrategyProviderInvocationHandler(strategyResolver)
    ));
  }

  @RequiredArgsConstructor
  private static final class StrategyProviderInvocationHandler implements InvocationHandler {

    @NonNull
    private final StrategyResolver strategyResolver;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      final var strategy = strategyResolver.resolve(method, args);

      final var argClasses = ArrayUtil.getClasses(args);
      final var strategyMethod = ReflectionUtils.findMethod(
          strategy.getClass(),
          method.getName(),
          argClasses
      );

      if (strategyMethod == null) {
        throw new IllegalStateException(
            "Class [ " + strategy.getClass() + " ] doesn't have method [ " + method.getName()
                + " ] and parameter types [ " + Arrays.toString(argClasses) + " ]"
        );
      }

      return ReflectionUtils.invokeMethod(strategyMethod, strategy, args);
    }
  }
}
