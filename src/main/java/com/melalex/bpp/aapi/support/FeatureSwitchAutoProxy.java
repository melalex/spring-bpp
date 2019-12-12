package com.melalex.bpp.aapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
public class FeatureSwitchAutoProxy extends AbstractAutoProxyCreator {

  // FIXME : Make target package more precise
  private static final String TARGET_PACKAGE = "com.melalex.bpp";
  private static final long serialVersionUID = -8468425142906303094L;

  private final List<Class<?>> scannedInterfaces = new ArrayList<>();

  public FeatureSwitchAutoProxy() {
    this.setInterceptorNames(FeatureSwitchAdvisor.class.getAnnotation(Component.class).value());
  }

  @Override
  protected Object[] getAdvicesAndAdvisorsForBean(
      final Class<?> beanClass,
      final String beanName,
      final TargetSource customTargetSource) throws BeansException {

    if (this.isBeanInTargetPackage(beanClass) && !beanClass.isInterface()) {
      final Class<?>[] interfaces;
      if (ClassUtils.isCglibProxyClass(beanClass)) {
        interfaces = beanClass.getSuperclass().getInterfaces();
      } else {
        interfaces = beanClass.getInterfaces();
      }

      if (interfaces != null) {
        for (final Class<?> beanInterface : interfaces) {
          final Object[] result = this.findAnnotation(beanClass, beanInterface);

          if (result != null) {
            return result;
          }
        }
      }
    }

    return DO_NOT_PROXY;
  }


  private Object[] findAnnotation(final Class<?> beanClass, final Class<?> beanInterface) {
    Object[] result = null;

    if (this.isBeanInTargetPackage(beanInterface) && !this.scannedInterfaces.contains(beanInterface)
        && beanInterface.isAnnotationPresent(Switch.class)) {
      final Class<?> switchClass = beanInterface.getAnnotation(Switch.class).beanClass();
      if (!switchClass.equals(beanClass)) {
        this.scannedInterfaces.add(beanInterface);
        result = PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
      }
    }

    return result;
  }

  private boolean isBeanInTargetPackage(final Class<?> beanClass) {
    return beanClass.getCanonicalName() != null && beanClass.getCanonicalName()
        .startsWith(TARGET_PACKAGE);
  }
}
