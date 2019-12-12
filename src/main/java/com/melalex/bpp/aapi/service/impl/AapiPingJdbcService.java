package com.melalex.bpp.aapi.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.melalex.bpp.aapi.service.AapiPingService;
import com.melalex.bpp.web.dto.PingDto;

@Primary
@Component
public class AapiPingJdbcService implements AapiPingService {

  @Override
  public PingDto ping() {
    return new PingDto("AapiPingJdbcService");
  }
}
