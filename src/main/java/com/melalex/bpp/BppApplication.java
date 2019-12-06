package com.melalex.bpp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.melalex.bpp.strategy.StrategyAnnotationProcessor;
import com.melalex.bpp.strategy.StrategyProviderFactory;

@SpringBootApplication
public class BppApplication {

  public static void main(final String[] args) {
    SpringApplication.run(BppApplication.class, args);
  }

  @Bean
  public StrategyAnnotationProcessor strategyAnnotationProcessor() {
    return new StrategyAnnotationProcessor(new StrategyProviderFactory());
  }
}
