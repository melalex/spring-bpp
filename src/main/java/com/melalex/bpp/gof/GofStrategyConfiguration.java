package com.melalex.bpp.gof;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.melalex.bpp.gof.service.GofPingService;
import com.melalex.bpp.gof.service.impl.GofCassandraPingService;
import com.melalex.bpp.gof.service.impl.GofJdbcPingService;
import com.melalex.bpp.gof.service.impl.StrategySelectorGofPingServiceDecorator;
import com.melalex.bpp.gof.support.impl.MapProvider;
import com.melalex.bpp.web.session.UserContext;

@Configuration
public class GofStrategyConfiguration {

  @Bean
  @Primary
  public GofPingService gofPingService(
      final GofJdbcPingService gofJdbcPingService,
      final GofCassandraPingService gofCassandraPingService,
      final UserContext userContext
  ) {
    final var provider = MapProvider.<Boolean, GofPingService>builder()
        .with(true, gofCassandraPingService)
        .with(false, gofJdbcPingService)
        .defaultValue(gofJdbcPingService)
        .build();

    return new StrategySelectorGofPingServiceDecorator(provider, userContext);
  }
}
