package com.melalex.bpp.strategy;

import java.lang.reflect.Method;
import java.util.List;

public interface StrategyResolver {

  Object resolve(Method method, Object[] args);

  void setStrategies(List<Object> strategies);
}
