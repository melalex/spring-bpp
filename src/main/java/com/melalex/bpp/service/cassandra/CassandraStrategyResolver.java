package com.melalex.bpp.service.cassandra;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;

import com.melalex.bpp.strategy.StrategyResolver;
import com.melalex.bpp.web.session.UserContext;

public class CassandraStrategyResolver implements StrategyResolver {

  private Map<Boolean, Object> registry;

  @Setter(onMethod = @__(@Autowired))
  private UserContext userContext;

  @Override
  public Object resolve(Method method, Object[] args) {
    final var strategy = registry.get(userContext.isCassandra());

    if (strategy == null) {
      throw new IllegalArgumentException(
          "No strategy for UserContext [ " + userContext + " ] in registry [ " + registry + " ]"
      );
    }

    return strategy;
  }

  @Override
  public void setStrategies(List<?> strategies) {
    registry = strategies.stream()
        .map(o -> Map.entry(o.getClass().getAnnotation(CassandraSwitch.class).match(), o))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  @Override
  public Collection<Object> availableStrategies() {
    return registry.values();
  }
}
