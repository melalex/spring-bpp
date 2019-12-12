package com.melalex.bpp.annotation.support.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.melalex.bpp.annotation.support.DefaultBaseInterface;
import com.melalex.bpp.annotation.support.Strategy;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Strategy
public @interface CassandraSwitch {

  @AliasFor(annotation = Strategy.class)
  Class<?> baseInterface() default DefaultBaseInterface.class;

  boolean match();
}
