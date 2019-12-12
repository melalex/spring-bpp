package com.melalex.bpp.annotation.service.impl;

import org.springframework.stereotype.Component;

import com.melalex.bpp.annotation.service.AnnotatedPingService;
import com.melalex.bpp.annotation.support.db.CassandraStrategy;
import com.melalex.bpp.web.dto.PingDto;

@Component
@CassandraStrategy
public class AnnotatedPingCassandraService implements AnnotatedPingService {

  @Override
  public PingDto ping() {
    return new PingDto("AnnotatedPingCassandraService");
  }
}
