package com.melalex.bpp.aapi.service.impl;

import org.springframework.stereotype.Component;

import com.melalex.bpp.aapi.service.AapiPingService;
import com.melalex.bpp.web.dto.PingDto;

@Component
public class AapiPingCassandraService implements AapiPingService {

  @Override
  public PingDto ping() {
    return new PingDto("AapiPingCassandraService");
  }
}
