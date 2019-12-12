package com.melalex.bpp.gof.service.impl;

import lombok.AllArgsConstructor;

import com.melalex.bpp.gof.service.GofPingService;
import com.melalex.bpp.gof.support.impl.MapProvider;
import com.melalex.bpp.web.dto.PingDto;
import com.melalex.bpp.web.session.UserContext;

@AllArgsConstructor
public class StrategySelectorGofPingServiceDecorator implements GofPingService {

  private final MapProvider<Boolean, GofPingService> provider;
  private final UserContext userContext;

  @Override
  public PingDto ping() {
    return this.provider.provide(this.userContext.isCassandra()).ping();
  }
}
