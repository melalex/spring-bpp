package com.melalex.bpp.annotation.service.impl;

import org.springframework.stereotype.Service;

import com.melalex.bpp.annotation.service.AnnotatedWithAbstractAncestorPingService;
import com.melalex.bpp.annotation.support.db.JdbcStrategy;
import com.melalex.bpp.web.session.UserContext;

@Service
@JdbcStrategy(AnnotatedWithAbstractAncestorPingService.class)
public class AnnotatedWithAbstractAncestorPingJdbcService extends
    AbstractAnnotatedWithAbstractAncestorPingService {

  public AnnotatedWithAbstractAncestorPingJdbcService(final UserContext userContext) {
    super(userContext);
  }

  @Override
  String pingMessage() {
    return "Ping from PingJdbcService.";
  }
}
