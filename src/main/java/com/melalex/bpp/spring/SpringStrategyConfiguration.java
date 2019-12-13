package com.melalex.bpp.spring;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.melalex.bpp.spring.service.SpringPingService;
import com.melalex.bpp.spring.service.impl.SpringPingCassandraService;
import com.melalex.bpp.spring.service.impl.SpringPingJdbcService;
import com.melalex.bpp.web.session.UserContext;

@Configuration
public class SpringStrategyConfiguration {

  @Autowired
  private SpringPingJdbcService springPingJdbcService;

  @Autowired
  private SpringPingCassandraService springPingCassandraService;

  @Autowired
  private UserContext userContext;

  @Bean
  @Primary
  @Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
  public SpringPingService springPingService() {
    return this.booleanSpringPingServiceMap().get(this.userContext.isCassandra());
  }

  @Bean
  public Map<Boolean, SpringPingService> booleanSpringPingServiceMap() {
    return Map.of(
        true, this.springPingCassandraService,
        false, this.springPingJdbcService
    );
  }
}
