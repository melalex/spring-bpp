package com.melalex.bpp.service.cassandra;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import com.melalex.bpp.strategy.AbstractStrategyResolver;
import com.melalex.bpp.web.session.UserContext;

@Component
@AllArgsConstructor
public class CassandraStrategyResolver extends
    AbstractStrategyResolver<Boolean, CassandraSwitch> {

  private final UserContext userContext;

  @Override
  protected Boolean provideKey(final Method method, final Object[] args) {
    return this.userContext.isCassandra();
  }

  @Override
  protected Boolean provideMatcher(final CassandraSwitch annotation) {
    return annotation.match();
  }
}
