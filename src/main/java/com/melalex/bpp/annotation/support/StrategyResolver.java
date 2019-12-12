package com.melalex.bpp.annotation.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public interface StrategyResolver<A extends Annotation> {

  Object resolve(Class<?> interfaceClass, Method method, Object[] args);

  void setStrategies(Class<?> interfaceClass, List<Object> strategies);

  Collection<Object> availableStrategies();

  Class<A> getAnnotationType();
}
