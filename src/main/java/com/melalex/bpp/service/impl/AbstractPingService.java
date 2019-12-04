package com.melalex.bpp.service.impl;

import lombok.AllArgsConstructor;

import com.melalex.bpp.service.PingService;
import com.melalex.bpp.web.dto.PingDto;
import com.melalex.bpp.web.session.UserContext;

@AllArgsConstructor
public abstract class AbstractPingService implements PingService {

  private final UserContext userContext;

  @Override
  public PingDto ping() {
    return new PingDto(pingMessage() + " UserContext: " + userContext);
  }

  abstract String pingMessage();
}
