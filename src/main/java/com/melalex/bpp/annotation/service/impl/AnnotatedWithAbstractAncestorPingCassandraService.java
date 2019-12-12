package com.melalex.bpp.annotation.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.annotation.service.AnnotatedWithAbstractAncestorPingService;
import com.melalex.bpp.annotation.support.db.CassandraStrategy;
import com.melalex.bpp.web.session.UserContext;

@Service
@CassandraStrategy(AnnotatedWithAbstractAncestorPingService.class)
public class AnnotatedWithAbstractAncestorPingCassandraService extends
    AbstractAnnotatedWithAbstractAncestorPingService {

  public AnnotatedWithAbstractAncestorPingCassandraService(final UserContext userContext) {
    super(userContext);
  }

  @Override
  String pingMessage() {
    return "Ping from PingCassandraService.";
  }
}
