package com.melalex.bpp.aapi.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("switch.advisor")
public class FeatureSwitchAdvisor implements MethodInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(FeatureSwitchAdvisor.class);

  private final ApplicationContext applicationContext;

  private final FeatureSwitchProvider featureSwitchProvider;

  public FeatureSwitchAdvisor(
      final ApplicationContext applicationContext,
      final FeatureSwitchProvider featureSwitchProvider) {
    this.applicationContext = applicationContext;
    this.featureSwitchProvider = featureSwitchProvider;
  }

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    final Class<?> currentClass = invocation.getMethod().getDeclaringClass();
    if (currentClass.isAnnotationPresent(Switch.class)) {
      final Switch annotation = currentClass.getAnnotation(Switch.class);
      final Class<?> alternateBeanClass = annotation.beanClass();
      final String featureName = annotation.featureName();

      if (this.checkFeatureSwitch(featureName) && alternateBeanClass != invocation.getThis()
          .getClass()) {
        return this.invokeAlternateBean(invocation, alternateBeanClass);
      }
    }
    return invocation.proceed();
  }

  private boolean checkFeatureSwitch(final String featureName) {
    return this.featureSwitchProvider.getSwitchFeatureValue(featureName);
  }

  private Object invokeAlternateBean(final MethodInvocation invocation,
      final Class<?> alternateBeanClass)
      throws Throwable {
    final Method method = invocation.getMethod();
    final Object alternateBean = this.applicationContext.getBean(alternateBeanClass);
    try {
      return method.invoke(alternateBean, invocation.getArguments());
    } catch (final IllegalAccessException e) {
      LOG
          .debug(
              "ERROR: FeatureFlipping FAILED on method:{} class:{}",
              method.getName(),
              method.getDeclaringClass().getName());

      throw new IllegalStateException(e);
    } catch (final InvocationTargetException e) {
      throw e.getTargetException();
    }
  }
}
