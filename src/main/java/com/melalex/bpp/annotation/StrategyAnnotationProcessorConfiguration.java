package com.melalex.bpp.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.melalex.bpp.annotation.support.StrategyAnnotationProcessor;
import com.melalex.bpp.annotation.support.provider.StrategyProviderFactory;
import com.melalex.bpp.annotation.support.reflection.MethodInvokerFactory;

@Configuration
public class StrategyAnnotationProcessorConfiguration {

  @Bean
  public StrategyAnnotationProcessor strategyAnnotationProcessor() {
    final var methodInvokerFactory = new MethodInvokerFactory();
    final var strategyProviderFactory = new StrategyProviderFactory(methodInvokerFactory);

    return new StrategyAnnotationProcessor(strategyProviderFactory);
  }
}
