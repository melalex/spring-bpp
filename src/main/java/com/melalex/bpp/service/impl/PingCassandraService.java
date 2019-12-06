package com.melalex.bpp.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.service.PingService;
import com.melalex.bpp.service.cassandra.CassandraStrategy;
import com.melalex.bpp.web.session.UserContext;

@Service
@CassandraStrategy(PingService.class)
public class PingCassandraService extends AbstractPingService {

  public PingCassandraService(final UserContext userContext) {
    super(userContext);
  }

  @Override
  String pingMessage() {
    return "Ping from PingCassandraService.";
  }
}
