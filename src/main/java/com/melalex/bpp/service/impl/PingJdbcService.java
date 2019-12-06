package com.melalex.bpp.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.service.PingService;
import com.melalex.bpp.service.cassandra.JdbcStrategy;
import com.melalex.bpp.web.session.UserContext;

@Service
@JdbcStrategy(PingService.class)
public class PingJdbcService extends AbstractPingService {

  public PingJdbcService(final UserContext userContext) {
    super(userContext);
  }

  @Override
  String pingMessage() {
    return "Ping from PingJdbcService.";
  }
}
