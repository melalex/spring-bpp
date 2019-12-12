package com.melalex.bpp.spring.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.spring.service.SpringPingService;
import com.melalex.bpp.web.dto.PingDto;

@Service
public class SpringPingCassandraService implements SpringPingService {

  @Override
  public PingDto ping() {
    return new PingDto("SpringPingCassandraService");
  }
}
