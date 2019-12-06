package com.melalex.bpp.service.cassandra;

import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Setter;

import com.melalex.bpp.strategy.StrategyResolver;
import com.melalex.bpp.web.session.UserContext;

@Component
public class CassandraStrategyResolver implements StrategyResolver {

  private Map<Boolean, Object> registry;

  @Setter(onMethod = @__(@Autowired))
  private UserContext userContext;

  @Override
  public Object resolve(final Method method, final Object[] args) {
    final var strategy = registry.get(userContext.isCassandra());

    if (strategy == null) {
      throw new IllegalArgumentException(
          "No strategy for UserContext [ " + userContext + " ] in registry [ " + registry + " ]"
      );
    }

    return strategy;
  }

  @Override
  public void setStrategies(final List<?> strategies) {
    registry = strategies.stream()
        .map(o -> entry(getMatcher(o), o))
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
  }

  @Override
  public Collection<Object> availableStrategies() {
    return registry.values();
  }

  private boolean getMatcher(final Object o) {
    return requireNonNull(getAnnotation(o.getClass(), CassandraSwitch.class)).match();
  }
}
