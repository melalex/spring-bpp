package com.melalex.bpp.annotation.service.impl;

import org.springframework.stereotype.Component;

import com.melalex.bpp.annotation.service.AnnotatedPingService;
import com.melalex.bpp.annotation.support.db.JdbcStrategy;
import com.melalex.bpp.web.dto.PingDto;

@Component
@JdbcStrategy
public class AnnotatedPingJdbcService implements AnnotatedPingService {

  @Override
  public PingDto ping() {
    return new PingDto("AnnotatedPingJdbcService");
  }
}
