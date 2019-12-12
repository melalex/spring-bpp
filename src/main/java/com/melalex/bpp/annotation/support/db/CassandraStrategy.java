package com.melalex.bpp.annotation.support.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.melalex.bpp.annotation.support.DefaultBaseInterface;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CassandraSwitch(match = true)
public @interface CassandraStrategy {

  @AliasFor(annotation = CassandraSwitch.class, attribute = "baseInterface")
  Class<?> value() default DefaultBaseInterface.class;
}
