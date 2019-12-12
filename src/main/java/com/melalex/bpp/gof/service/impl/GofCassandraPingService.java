package com.melalex.bpp.gof.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.gof.service.GofPingService;
import com.melalex.bpp.web.dto.PingDto;

@Service
public class GofCassandraPingService implements GofPingService {

  @Override
  public PingDto ping() {
    return new PingDto("GofCassandraPingService");
  }
}