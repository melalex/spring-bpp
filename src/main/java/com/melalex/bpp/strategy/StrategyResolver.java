package com.melalex.bpp.strategy;

import java.util.List;

public interface StrategyResolver {

  Object resolve();

  void setStrategies(List<Object> strategies);
}
