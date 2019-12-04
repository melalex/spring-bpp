package com.melalex.bpp.service.cassandra;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.melalex.bpp.strategy.Strategy;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Strategy(resolver = CassandraStrategyResolver.class)
public @interface CassandraSwitch {

  @AliasFor(annotation = Strategy.class)
  Class<?> baseInterface();

  boolean match();
}
