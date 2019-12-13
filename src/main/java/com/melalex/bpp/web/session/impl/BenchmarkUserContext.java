package com.melalex.bpp.web.session.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.melalex.bpp.web.session.UserContext;

@Primary
@Component
@Profile("benchmark")
public class BenchmarkUserContext implements UserContext {

  private boolean cassandra;

  @Override
  public boolean isCassandra() {
    return this.cassandra;
  }

  @Override
  public void setCassandra(final boolean cassandra) {
    this.cassandra = cassandra;
  }
}
