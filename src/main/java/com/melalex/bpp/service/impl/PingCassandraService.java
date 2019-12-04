package com.melalex.bpp.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.service.PingService;
import com.melalex.bpp.service.cassandra.CassandraSwitch;
import com.melalex.bpp.web.session.UserContext;

@Service
@CassandraSwitch(baseInterface = PingService.class, match = true)
public class PingCassandraService extends AbstractPingService {

  public PingCassandraService(UserContext userContext) {
    super(userContext);
  }

  @Override
  String pingMessage() {
    return "Ping from PingCassandraService.";
  }
}
