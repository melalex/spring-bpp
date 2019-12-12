package com.melalex.bpp.annotation.service.impl;

import lombok.AllArgsConstructor;

import com.melalex.bpp.annotation.service.AnnotatedWithAbstractAncestorPingService;
import com.melalex.bpp.web.dto.PingDto;
import com.melalex.bpp.web.session.UserContext;

@AllArgsConstructor
public abstract class AbstractAnnotatedWithAbstractAncestorPingService implements
    AnnotatedWithAbstractAncestorPingService {

  private final UserContext userContext;

  @Override
  public PingDto ping() {
    return new PingDto(this.pingMessage() + " UserContext: " + this.userContext);
  }

  abstract String pingMessage();
}
