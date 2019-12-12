package com.melalex.bpp.annotation.support.db;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import com.melalex.bpp.annotation.support.AbstractStrategyClassifier;
import com.melalex.bpp.web.session.UserContext;

@Component
@AllArgsConstructor
public class CassandraStrategyClassifier extends
    AbstractStrategyClassifier<Boolean, CassandraSwitch> {

  private final UserContext userContext;

  @Override
  public Boolean classifyKey(final Method method, final Object[] args) {
    return this.userContext.isCassandra();
  }

  @Override
  public Boolean classifyMatcher(final CassandraSwitch annotation) {
    return annotation.match();
  }
}
