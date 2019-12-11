package com.melalex.bpp.strategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public interface StrategyResolver<A extends Annotation> {

  Object resolve(Method method, Object[] args);

  void setStrategies(List<Object> strategies);

  Collection<Object> availableStrategies();

  Class<A> getAnnotationType();
}
